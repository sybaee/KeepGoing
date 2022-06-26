import os
import time
import json
import argparse
import numpy as np
import pandas as pd

from collections import Counter
from itertools import product, combinations

class Extraction(object):
    def __init__(self, **init_args):
        self.dict_nan_attrs = {
            'TOP': {'FIT': ['PANTS_FIT', 'SKIRT_FIT', 'SUIT-JACKET_FIT'],
                    'LENGTH': ['OUTER_LENGTH', 'PANTS_LENGTH']},
            'BOTTOM': {'FIT': ['DRESS_FIT', 'CASUAL_TOP_FIT', 'SUIT-JACKET_FIT'],
                       'LENGTH': ['OUTER_LENGTH', 'SLEEVES_LENGTH'],
                       'SLEEVE_DETAIL': ['DROP_SHOULDER']},
            'TOP_L': {'FIT': ['PANTS_FIT', 'SKIRT_FIT', 'SUIT-JACKET_FIT']},
            'SHOES': {'FIT': []}, 'BAG': {'FIT': []}
        }

        self.dict_color = {
            '0': '0_IVORY', '1': '1_WHITE', '2': '2_LIGHT_GREY', '3': '3_GREY',
            '4': '4_ASH', '5': '5_BLACK', '6': '6_RED', '7': '7_CORAL',
            '8': '8_ORANGE', '9': '9_SALMON_PINK', 'A': 'A_BEIGE',
            'B': 'B_YELLOWISH_BROWN', 'C': 'C_BRICK', 'D': 'D_BROWN', 
            'E': 'E_YELLOW', 'F': 'F_LEMON', 'G': 'G_MUSTARD', 'H': 'H_KHAKI',
            'I': 'nan', 'J': 'J_OLIVE', 'K': 'K_APPLE_GREEN', 'L': 'L_LIGHT_GREEN',
            'M': 'M_GREEN', 'N': 'N_ROYAL_BLUE', 'O': 'nan', 'P': 'P_BLUE', 
            'Q': 'Q_SKY_BLUE', 'R': 'R_NAVY', 'S': 'S_VIOLET', 'T': 'T_LAVENDA',
            'U': 'U_PURPLE', 'V': 'V_ORCHID', 'W': 'W_MAGENTA', 'X': 'X_PINK',
            'Y': 'Y_LIGHT_PINK', 'Z': 'Z_WINE'
        }

        self.dict_class = {
            'TOP': ['C&S', 'SHIRTS', 'ONEPIECE', 'SWEATER', 'SHIRT', 
                    'BLOUSE&SHIRTS', 'ONEPIECE ', 'SWEATER '], 
            'BOTTOM': ['PANTS', 'SKIRT'], 
            'OUTER': ['COAT', 'DOWNPADDING', 'ROBE', 'FUR', 
                      'JACKET&JUMPER', 'LEATHER', 'SUIT'], 
            'BAG': ['BAG', 'BAG '], 'SHOES': ['SHOES'], 'TOP_L': ['VEST']
        }

        self.nan_cols = ['color', 'gender', 'shopDiv', 'brandName', 'releaseYear', 
                         'season', 'clo_class', 'FIT', 'TEXTURE', 'PATTERN', 
                         'LENGTH', 'DECORATION', 'DETAIL', 'COLLAR_DETAIL', 
                         'POCKET_DETAIL', 'SLEEVE_DETAIL']

        self.clothes = ['OUTER', 'TOP', 'BOTTOM', 'SHOES', 'BAG', 'TOP_L']
        self.attrs = ['COLOR', 'CLASS', 'FIT', 'PATTERN', 'TEXTURE']

        self.clothes_attrs = ['{}_{}'.format(c, a)
                              for c, a in product(self.clothes, self.attrs)
                              if c not in self.clothes[3:5] or a not in self.attrs[2:]]

        self.file_new_attrs = init_args['file_new_attrs']

        print('Reading data...')
        self.get_dict_attrs()

        self.file_pos = init_args['file_pos']
        self.file_neg_new = init_args['file_neg_new']
        
        if self.file_pos.split('.')[-1] == 'csv':
            self.df_pos = pd.read_csv(self.file_pos)

            self.file_pos_old_brand = init_args['file_pos_old_brand']
            with open(self.file_pos_old_brand) as f:
                self.pos_old_brand = set(json.load(f))
            
        if self.file_pos.split('.')[-1] == 'json':
            with open(self.file_pos) as f:
                self.pos_data = json.load(f)

            self.df_pos = self.preprocess_outfit(self.pos_data)
            self.pos_old_brand = set()
        
        with open(self.file_neg_new) as f:
            self.neg_new_data = json.load(f)

        if 'file_neg_old' in init_args:
            self.file_neg_old = init_args['file_neg_old']
            self.file_neg_old_brand = init_args['file_neg_old_brand']

            self.neg_old_data = pd.read_csv(self.file_neg_old)
            with open(self.file_neg_old_brand) as f:
                self.neg_old_brand = set(json.load(f))

        else:
            self.neg_old_data = pd.DataFrame()
            self.neg_old_brand = set()
        
        self.df_neg = pd.concat([self.neg_old_data, 
                                 self.preprocess_outfit(self.neg_new_data)],
                                 ignore_index=True)
        
        self.file_gen = init_args['file_gen'] if 'file_gen' in init_args else ''
        
        self.venn_colors = ['#F4ACB7', '#9D8189', '#C4E6FF']

        self.fig_path = init_args['fig_path'] if 'fig_path' in init_args else '.'

        self.set_cols = ['Positive', 'Negative']
        self.rule_cols = ['ITEM', 'ATTR']

        self.clothes_map = {i: self.clothes[i] for i in range(len(self.clothes))}
        self.attrs_map = {i: self.attrs[i] for i in range(len(self.attrs))}
        
        self.clothes_combination = {2: {'TB': self.clothes[1:3]}}

        for i in range(3, len(self.clothes)+1):
            self.clothes_combination[i] = {}
            for components in combinations(self.clothes, i):
                if all(x in components for x in self.clothes_combination[2]['TB']):
                    combi = list(components)
                    abbr = ''.join([compo[0] if compo != 'TOP_L' else compo[-1] 
                                    for compo in combi])
                    self.clothes_combination[i][abbr] = combi
        
        self.composition = [compo for compo in self.get_all_dict_values(
                                                    self.clothes_combination)]
        
        print('Processing...')
        self.dict_rules = {'Positive': self.extract_rules(self.df_pos),
                           'Negative': self.extract_rules(self.df_neg)}

        self.multi_indexes = ['Duplicated', 'Attributes', 'Shrunk', 'Label']
        self.dict_neg_unique_rules_all = {'BRAND_RULE': [], 'ITEM_RULE': []}
        
    def prepare_gen_outfit(self):
        if self.file_gen.split('.')[-1] == 'csv':
            self.df_gen = pd.read_csv(self.file_gen)
        
        elif self.file_gen.split('.')[-1] == 'json':
            with open(self.file_gen) as f:
                self.gen_data = json.load(f)

            self.df_gen = self.preprocess_outfit(self.gen_data, output_tf=True)

        else:
            return

        self.dict_rules['Generation'] = self.extract_rules(self.df_gen)
        self.df_gen_passed_all = self.df_gen.copy()

    def prepare_common_attrs(self, list_standard, num_common=3):
        self.standard = list_standard
        self.standard_abbr = ''.join([compo[0] if compo != 'TOP_L' else compo[-1] 
                                      for compo in list_standard])

        self.attrs_std_cols = [c_a for c_a in self.clothes_attrs 
            if '_'.join(c_a.split('_')[:-1]) in self.standard]

        self.df_pos_attrs_std = self.df_pos[self.attrs_std_cols]
        self.df_neg_attrs_std = self.df_neg[self.attrs_std_cols]

        self.common_only_std = self.count_common_attributes(
            self.dict_rules['Negative'], extract=True, inclusion='only')
        self.common_except_std = self.count_common_attributes(
            self.dict_rules['Negative'], extract=True, inclusion='except')
        self.common_all_std = self.count_common_attributes(
            self.dict_rules['Negative'], extract=True, inclusion='all')

        self.num_common = num_common
        self.common_from_num_except_std = {
            attrs: dupe for attrs, dupe in self.common_except_std.items() 
            if dupe >= self.num_common}

        self.common_from_num_all_std = dict(
            self.common_only_std, **self.common_from_num_except_std)

        self.common_pos_only_std = self.count_common_attributes(
            self.dict_rules['Positive'], extract=True, inclusion='only')

    def get_key_by_value(self, dictionary, value):
        return next((k for k, v in dictionary.items() if v == value), None)

    def get_all_dict_values(self, dictionary):
        for value in dictionary.values():
            if type(value) is dict:
                yield from self.get_all_dict_values(value)
            else:
                yield value

    def find_second(self, string, substring):
        return string.find(substring, string.find(substring)+1)

    def extract_rules(self, df):
        df_rules = pd.DataFrame()

        df_rules[self.rule_cols[0]] = pd.Series(df[self.clothes].values.tolist())
        df_rules[self.rule_cols[1]] = pd.Series(df[self.clothes_attrs].values.tolist())
        
        return df_rules

    def get_clothes_abbreviations(self, exception=[]):
        abbrs = []
        for i in range(2, len(self.clothes)+1): ## 2: TB ~ 6: OTBSBL
            if i not in exception:
                abbrs.extend(list(self.clothes_combination[i].keys()))
                
        return abbrs

    def get_only_attr_set(self, dict_common, progress=False):
        prev = len(dict_common)
        inter_count = 0
        for attrs in self.common_pos_only_std.keys():
            try:
                del dict_common[attrs]
                inter_count += 1
            except:
                pass
        
        if progress:
            print(prev, '-', inter_count, '=' , len(dict_common))

    def get_color(self, _dict):
        _color = _dict.get('color', np.nan)
        if _color in self.dict_color:
            return self.dict_color[_color]
        return _color

    def make_class(self, dict_value):
        class_value = dict_value['itemClass'][0]['galCate02']
        class_sub_value = dict_value['itemClass'][0]['galCate03']
        if class_sub_value in ['SWEATER_CARDIGAN', 'SWEATER_VEST']:
            class_value = 'VEST'
            
        return next((class_name for class_name, class_subs in self.dict_class.items() 
                    if class_value in class_subs), 'ETC')

    def set_attr(self, dict_json):
        for skuCode in dict_json.keys():
            _class = dict_json[skuCode]['clo_class']
            if _class in self.dict_nan_attrs:
                for _attr in self.dict_nan_attrs[_class]:
                    if 'attribute' in dict_json[skuCode]:
                        for _dict in dict_json[skuCode]['attribute']:
                            if _dict['att01'] == _attr: 
                                list_attrs = self.dict_nan_attrs[_class][_attr]
                                if list_attrs == [] or _dict['att02'] in list_attrs:
                                    _dict['att01'] = np.nan, 
                                    _dict['att02'] = np.nan,
                                    _dict['attVal'] = np.nan

        return dict_json

    def set_key(self, dict_json):
        for skuCode in dict_json.keys():
            if 'attribute' in dict_json[skuCode]:
                for _dict in dict_json[skuCode]['attribute']:
                    dict_json[skuCode][_dict['att01']] = _dict['attVal']

        return dict_json

    def preprocess_item_json(self, data):
        dict_item = {}
        for _dict in data:
            if 'galCategory' in _dict.keys():
                _dict['itemClass'] = _dict['galCategory']
                del _dict['galCategory']
                
            # 색상이 2개 이상 리스트로 구성된 경우 첫번째 것만 사용, 
            # 또한 빈 리스트로 된 것은 ""값 할당
            if 'color' in _dict:
                if len(_dict['color']) > 0:
                    _dict['color'] = _dict['color'][0]
                    
                if _dict['color'] == []:
                    _dict['color'] = np.nan
                                    
            # 색 전처리 / SSF(자사)이지만 key값이 없거나 color값이 공백인 경우에는 색 할당
            _dict['color'] = self.get_color(_dict)
            
            # 복종 전처리 
            _dict['clo_class'] = self.make_class(_dict)
    
            if _dict['itemClass'][0]['galCate03'] == '':
                _dict['itemClass'][0]['galCate03'] = np.nan
                
            # 빈폴 골프를 제외한 빈폴은 하나로 합치고, 빈폴 골프는 그대로 둠
            # (BEANPOLE LADIES, BEANPOLE MEN, BEANPOLE OUTDOOR, BEANPOLE SPORT)
            item_brand = _dict['brandName']
            if 'BEANPOLE' in item_brand and 'BEANPOLE GOLF' != item_brand:
                _dict['brandName'] = 'BEANPOLE'
                                
            if _dict['shopDiv'] == 'SSF':
                if 'KUHO' in item_brand: # KUHO와 KUHO PLUS도 하나로 합침
                    _dict['brandName'] = 'KUHO'
        
                if '8S' in item_brand: # 8S로 시작하는 친구들은 8SECONDS라고 명명한 뒤 갯수를 셈
                    _dict['brandName'] = '8SECONDS'

            dict_item[_dict['skuCode']] = _dict
        
        dict_item = self.set_attr(dict_item)
        dict_item = self.set_key(dict_item)
        
        for skuCode in dict_item:
            if 'gender' in dict_item[skuCode]: # 성별 전처리
                _gender = dict_item[skuCode]['gender']
                if _gender.upper() in ['WOMEN', 'WOMEM']: gender_name = 'WOMEN'
                elif _gender.upper() == 'MEN': gender_name = 'MEN'  
                elif _gender.upper() == 'UNISEX': gender_name = 'CMNUSE' 
                else: gender_name = np.nan

            else:
                gender_name = np.nan
                
            dict_item[skuCode]['gender'] = gender_name
                
            # 키가 존재하지 않는 것은 키를 달아주면서 nan값을 부여
            for col in self.nan_cols:
                if col not in dict_item[skuCode]:
                    if col == 'attribute':
                        dict_item[skuCode][col] = [
                            {'att01': np.nan, 'att02': np.nan, 'attVal': np.nan}]

                    else:
                        dict_item[skuCode][col] = np.nan

                if dict_item[skuCode][col] in ['', 'nan', 'NAN']:
                    dict_item[skuCode][col] = np.nan
            
        return dict_item

    def get_dict_attrs(self):
        with open(self.file_new_attrs) as f:
            self.natts = json.load(f)['data']

        self.dict_attrs = self.preprocess_item_json(self.natts)

    def preprocess_outfit(self, outfit_data, output_tf=False):
        outfit_key = 'outfit' if output_tf else 'items'
        skuCode_key = 'sku_code' if output_tf else 'skuCode'

        outfit_data_pinch = [*outfit_data]
        for _outfit in outfit_data_pinch:
            for _item in _outfit[outfit_key]:
                skuCode = _item[skuCode_key]
                if skuCode not in self.dict_attrs:
                    outfit_data.remove(_outfit)
                    break
        
        if output_tf:
            self.gen_data = [*outfit_data]
            
        df_outfit = pd.DataFrame(outfit_data, columns=[outfit_key])
        df_outfit['outfit_all'] = df_outfit[outfit_key].apply(
            lambda _items: [_dict[skuCode_key] for _dict in _items])

        for _class in self.clothes:
            df_outfit[_class] = df_outfit['outfit_all'].apply(
                lambda _outfits: [skuCode for skuCode in _outfits 
                                if self.dict_attrs[skuCode]['clo_class'] == _class]).str[0]
            
            for _attr in self.attrs:
                col_name = '{}_{}'.format(_class, _attr)
                if _attr == 'CLASS':
                    df_outfit[col_name] = df_outfit[_class].apply(
                        lambda skuCode: col_name + '_' + str(
                            self.dict_attrs[skuCode]['itemClass'][0]['galCate03']).upper() 
                            if str(skuCode) != 'nan' else col_name + '_' + 'NOT_EXIST')
                    
                elif col_name in self.clothes_attrs:
                    _key = 'color' if _attr == 'COLOR' else (
                        'brandName' if _attr == 'BRAND' else _attr)
                    
                    df_outfit[col_name] = df_outfit[_class].apply(
                        lambda skuCode: col_name + '_' + str(
                            self.dict_attrs[skuCode][_key]).upper()
                            if str(skuCode) != 'nan' else col_name + '_' + 'NOT_EXIST')
        
        # 복종이 존재하는데 값이 없는 경우는 NAN이지만 복종이 없는 경우는 NOT_EXIST라고 명명
        df_outfit = df_outfit.replace('', 'NAN').fillna('NAN')
            
        return df_outfit.drop(df_outfit.columns[[0, 1]], axis=1)

    def draw_venn_diagram(self, title, sets, labels, colors, save=False, file_name='venn'):
        plt.figure(figsize=(10, 10))
        plt.title(title, fontsize=22)
        
        ven = venn2 if len(sets) == 2 else venn3

        vd = ven(sets, set_labels=labels, set_colors=colors, alpha=0.8)
        
        for text in vd.set_labels:
            text.set_fontsize(18)
        for text in vd.subset_labels:
            if text != None: text.set_fontsize(15)
        
        if save:
            plt.savefig('{}/{}.jpg'.format(self.fig_path, file_name), bbox_inches='tight')
        
        plt.show()

    def get_cardinality(self, a, b, c=False, sets=False, rule='ITEM', 
                        mention=False, save=False, file_name='venn'):
        if type(a) == dict:
            a = pd.DataFrame(a.keys(), columns=[self.rule_cols[1]])
            a[self.rule_cols[1]]= a[self.rule_cols[1]].apply(
                lambda attrs: attrs.split(', '))

        if type(b) == dict:
            b = pd.DataFrame(b.keys(), columns=[self.rule_cols[1]])
            b[self.rule_cols[1]]= b[self.rule_cols[1]].apply(
                lambda attrs: attrs.split(', '))

        func = lambda x: ', '.join(x)
        rule_a = a[rule].apply(func)
        rule_b = b[rule].apply(func)
        
        venns = [set(rule_a), set(rule_b)]
        inters_1 = a[rule_a.isin(rule_b) == True]
        
        if type(c) != bool:
            if type(c) == dict:
                c = pd.DataFrame(c.keys(), columns=[self.rule_cols[1]])
                c[self.rule_cols[1]]= c[self.rule_cols[1]].apply(
                    lambda attrs: attrs.split(', '))

            sets = self.set_cols
            rule_c = c[rule].apply(func)

            venns.append(set(rule_c))

            inters_2 = b[rule_b.isin(rule_c)]
            inters_3 = c[rule_c.isin(rule_a)]
            inters_4 = inters_2[inters_2[rule].apply(func).isin(
                inters_3[rule].apply(func))]
        
        if mention:
            if type(c) != bool:
                print('A: {}, B: {}, C: {}\n'.format(sets[0], sets[1], sets[2]))
                print('n(A) = {:^10d}'.format(len(a)))
                print('n(B) = {:^10d}'.format(len(b)))
                print('n(C) = {:^10d}\n'.format(len(c)))

                print('n(A ∩ B) = {:^10d}'.format(len(inters_1)))
                print('n(B ∩ C) = {:^10d}'.format(len(inters_2)))
                print('n(C ∩ A) = {:^10d}\n'.format(len(inters_3)))

                print('n(A ∩ B ∩ C) = {:^10d}\n'.format(len(inters_4)))

                print('n(A U B U C) = {:^10d}'.format(len(a) + len(b) + len(c)
                                                      - len(inters_1)
                                                      - len(inters_2)
                                                      - len(inters_3)
                                                      + len(inters_4)))
                
            else:
                print('A: {}, B: {}\n'.format(sets[0], sets[1]))
                print('n(A) = {:^10d}'.format(len(a)))
                print('n(B) = {:^10d}\n'.format(len(b)))

                print('n(A ∩ B)  = {:^10d}\n'.format(len(inters_1)))

                print('n(A U B)  = {:^10d}'.format(len(a) + len(b) - len(inters_1)))
                
            self.draw_venn_diagram('Unique {} Rules'.format(rule), venns, 
                                   sets, self.venn_colors[:len(sets)],
                                   save, file_name)
        
        if type(c) != bool:
            return inters_1, inters_2, inters_3, inters_4
        return inters_1

    def item_composition_func(self):
        return lambda row: list(map(self.clothes_map.get, 
            filter(lambda item: row[item] != 'NAN', range(len(row)))))

    def attr_composition_func(self):
        return lambda row: list(filter(
            lambda attr: (attr.split('_')[0] in self.standard) 
                if attr[:5] != self.clothes[-1] 
                else (self.clothes[-1] in self.standard), row))

    def remove_attributes_func(self, indexes):
        return lambda row: list(filter(
            lambda compo: compo not in [row[i] for i in indexes], row))

    def extract_composition(self, df, inclusion='only'):        
        df_item_compo = df[self.rule_cols[0]].apply(self.item_composition_func())

        if inclusion == 'only': ## 딱 그 조합만 뽑기
            return df_item_compo.apply(lambda compo: compo == self.standard)

        elif inclusion == 'except': ## 그 조합만 빼고 그 조합을 포함하는 조합 뽑기
            return df_item_compo[df_item_compo.apply(
                lambda compo: compo != self.standard)].apply(
                    lambda compo: all(clothes in compo for clothes in self.standard))

        return df_item_compo.apply( ## 그 조합을 포함하는 모든 조합 뽑기
                lambda compo: all(clothes in compo for clothes in self.standard))

    def count_clothes_combination(self, inclusion='all'):
        for i, (rule, set_name) in enumerate(product(self.rule_cols[:1], self.set_cols)):
            print(rule, set_name)

            df_item_compo = self.dict_rules[set_name][
                self.rule_cols[0]].apply(self.item_composition_func())

            df_curr_count = df_item_compo[self.extract_composition(
                self.dict_rules[set_name], inclusion)].apply(str).value_counts()

            df_count_sets = pd.merge(df_count_sets, df_curr_count, 
                                     left_index=True, right_index=True,
                                     how='outer') if i != 0 else df_curr_count

        df_count_sets.columns = self.set_cols
        df_count_sets.fillna(0, inplace=True)
        df_count_sets.loc['Total'] = df_count_sets.apply(sum)

        return df_count_sets

    def count_common_attributes(self, df, extract=False, inclusion='only'):
        if extract:
            if inclusion == 'except':
                df_item_compo = df[self.rule_cols[0]].apply(self.item_composition_func())
                df = df[df_item_compo.apply(lambda compo: compo != self.standard)]

            df = df[self.extract_composition(df, inclusion)]

        df_standard_attrs = df[self.rule_cols[1]].apply(self.attr_composition_func())

        return dict(Counter(df_standard_attrs.apply(lambda x: ', '.join(x))).most_common())

    def get_attrs_map(self, data, inclusion='only'): ## dict_common
        if type(data) != dict:
            df = self.extract_composition(data, inclusion)
            df_standard_attrs = df[self.rule_cols[1]].apply(self.attr_composition_func())
            
            dict_common = dict(Counter(df_standard_attrs.apply(
                lambda x: ', '.join(x))).most_common())

            dict_common = self.count_common_attributes
        else:
            dict_common = data

        self.common_attrs_map = {(cnt, attrs): {chr(65+i): attr 
                                    for i, attr in enumerate(attrs.split(', '))} 
                                        for attrs, cnt in dict_common.items()}
        
        return self.common_attrs_map
        
    def get_common_attributes_distribution(self, dict_common, inclusion='only',
                                           draw=False):
        df_count = pd.DataFrame(dict_common.values(), columns=['Count'])

        if draw:
            fig, ax = plt.subplots(1, 2, figsize=(20, 8))
            
            subtitle = 'with ONLY' if inclusion == 'only' else (
                'including' if inclusion == 'except' else 'with ALL')
            standard_abbr = ''.join([compo[0] if compo != 'TOPL' else compo[-1] 
                                    for compo in self.standard])
            fig_title = 'Negative {} combination {} {}'.format(
                self.rule_cols[1], subtitle, standard_abbr)
            fig.suptitle(fig_title, fontsize=20, y=0.95)
            ax[0] = sns.histplot(x=df_count['Count'], ax=ax[0])
            ax[1] = sns.kdeplot(x=df_count['Count'], ax=ax[1])

            plt.savefig('{}/{}.jpg'.format(self.fig_path, fig_title.replace(' ', '_')), 
                        bbox_inches='tight')
            plt.show()

        df_count = df_count['Count'].value_counts().sort_index()

        return df_count.append(pd.Series(df_count.sum(), index=['Sum']))

    def get_common_attributes_by_count(self, dict_common, count):
        return [attr for attr, cnt in dict_common.items() if cnt == count]

    def count_shrunk_attributes(self, df_attr_1, df_attr_2, attrs, 
                                dupe_count, progress=True):
        def get_attributes_sum(shrink, attrs_reduced, attrs_cols):
            df_attr_1_removed = df_attr_1[attrs_cols]
            removed_count_1 = sum(df_attr_1_removed.isin(
                attrs_reduced).all(axis=1))

            if shrink > 0:
                df_attr_2_removed = df_attr_2[attrs_cols]
                removed_count_2 = sum(df_attr_2_removed.isin(
                    attrs_reduced).all(axis=1))

            else:
                removed_count_2 = dupe_count

            ratio_1 = (removed_count_1 - max_count_1
                ) / len_total_1 if shrink > 1 else removed_count_1 / len_total_1
            ratio_2 = (removed_count_2 - max_count_2
                ) / len_total_2 if shrink > 1 else removed_count_2 / len_total_2

            return removed_count_1, removed_count_2, ratio_1, ratio_2

        len_total_1 = len(df_attr_1); len_total_2 = len(df_attr_2)
        
        dict_count_1 = {}; dict_count_2 = {}
        dict_attrs_1 = {}; dict_attrs_2 = {}
        for i in range(1, self.max_shrink+1):
            if progress:
                print('Duplicated: {} Shrink: {}'.format(dupe_count, i))

            dict_count_1[i] = {}; dict_count_2[i] = {}
            dict_attrs_1[i] = {}; dict_attrs_2[i] = {}
            for attrs_combi in combinations(attrs, i):
                attrs_shrunk = [*attrs]
                removed_indexes = []

                if i == 1 or any(a in list(dict_attrs_2[i-1].keys()) 
                                 for a in combinations(attrs_combi, i-1)):
                    if i == 1:
                        max_count_1 = 0; max_count_2 = dupe_count
                    else:
                        max_cand_1 = []; max_cand_2 = []
                        for prev_attrs_combi in combinations(attrs_combi, i-1):
                            try:
                                max_cand_1.append(dict_count_1[i-1][prev_attrs_combi])
                                max_cand_2.append(dict_count_2[i-1][prev_attrs_combi])
                            except:
                                pass

                        max_count_1 = max(max_cand_1); max_count_2 = max(max_cand_2)
                    
                    for attr in attrs_combi:
                        attrs_shrunk.remove(attr)
                        removed_indexes.append(attrs.index(attr))

                    attrs_std_shrk_cols = [*self.attrs_std_cols]
                    for index in removed_indexes:
                        attrs_std_shrk_cols.remove(self.attrs_std_cols[index])

                    removed_count_1, removed_count_2, ratio_1, ratio_2 = \
                        get_attributes_sum(i, attrs_shrunk, attrs_std_shrk_cols)

                    if (removed_count_2 > max_count_2) and (
                        ratio_2 >= self.ratio_threshold) and (
                        ratio_1 <= ratio_2 / self.times_threshold):

                        dict_count_1[i][attrs_combi] = removed_count_1
                        dict_count_2[i][attrs_combi] = removed_count_2
                        
                        dict_attrs_1[i][attrs_combi] = ratio_1
                        dict_attrs_2[i][attrs_combi] = ratio_2

        if all(dict_attrs_1[shrink] == {} for shrink in dict_attrs_1.keys()):
            original_count_1, original_count_2, \
                original_ratio_1, original_ratio_2 = \
                    get_attributes_sum(0, attrs, self.attrs_std_cols)
            
            if original_ratio_2 >= self.ratio_threshold and \
                original_ratio_1 <= original_ratio_2 / self.times_threshold:

                dict_count_1[0] = {'Original': original_count_1}
                dict_count_2[0] = {'Original': original_count_2}
                
                dict_attrs_1[0] = {'Original': original_ratio_1}
                dict_attrs_2[0] = {'Original': dupe_count / len_total_2}
                            
        return dict_attrs_1, dict_attrs_2, dict_count_1, dict_count_2

    def get_shrink_dictionary(self, dict_common_all, progress=True):     
        self.dict_pos_attrs_ratio_std = {}
        self.dict_neg_attrs_ratio_std = {}
        self.dict_pos_attrs_count_std = {}
        self.dict_neg_attrs_count_std = {}
        for i, (attrs, dupe_cnt) in enumerate(dict_common_all.items()):
            if self.count_all or progress:
                print('\n{} / {}'.format(i+1, len(dict_common_all)))
            
            if dupe_cnt in self.dupe_counts or self.count_all:
                attrs_standard = attrs.split(', ')
                self.dict_pos_attrs_ratio_std[attrs
                    ], self.dict_neg_attrs_ratio_std[attrs
                    ], self.dict_pos_attrs_count_std[attrs
                    ], self.dict_neg_attrs_count_std[attrs
                    ] = self.count_shrunk_attributes(
                        self.df_pos_attrs_std, self.df_neg_attrs_std, 
                        attrs_standard, dupe_cnt, progress)

    def get_shrink_dataframe(self, dict_common, progress=True):
        def concat_shrink_dataframe():
            df_shrink = pd.DataFrame(
                {'Positive': list(self.dict_pos_attrs_ratio_std[attrs][shrk].values()),
                 'Negative': list(self.dict_neg_attrs_ratio_std[attrs][shrk].values())},
                 index=[[dupe]*len_index, [attrs]*len_index, 
                        [shrk]*len_index, attr_labels])

            self.df_pos_neg_shrink = pd.concat(
                [self.df_pos_neg_shrink, df_shrink], axis=0)

        self.df_pos_neg_shrink = pd.DataFrame()
        for attrs, dupe in dict_common.items():
            if dupe in self.dupe_counts or self.count_all:
                if progress:
                    print('Duplicated:', dupe)
                    print(attrs)
                if 0 in self.dict_neg_attrs_ratio_std[attrs].keys():  
                    shrk = 0; len_index = 1; attr_labels = ['Original']
                    concat_shrink_dataframe()          
                    
                else:
                    for shrk in range(1, self.max_shrink+1):
                        attr_labels = []
                        for attr_combi in self.dict_neg_attrs_ratio_std[attrs][shrk].keys():
                            attr_labels.append(
                                ''.join([self.get_key_by_value(
                                    self.common_attrs_map[(dupe, attrs)], value) 
                                        for value in attr_combi]))

                        len_index = len(attr_labels)
                        concat_shrink_dataframe()

        self.df_pos_neg_shrink.index.names = self.multi_indexes

        return self.df_pos_neg_shrink

    def draw_shrink_barh(self, df, dupe_count, attrs, shrk):
        plt.figure()
        plt.rcParams.update({'font.size': 13})
        
        subtitle = ' '.join(['{}: {}\n'.format(k, v) 
                        for k, v in self.common_attrs_map[(dupe_count, attrs)].items()])
        title = '{} Duplicated {} Shrunk Ratio\n\n{}'.format(
            dupe_count, shrk, subtitle)
        
        ax = df.plot.barh(figsize=(30, 25))
        ax.set_title(title, pad=10, fontdict={'fontsize': 20})
        ax.yaxis.set_tick_params(labelsize=15)
        
        attrs_name = '_'.join([attr[self.find_second(attr, '_')+1:] 
                                for attr in attrs.split(', ')])
        plt.savefig('{}/{}_dupe_{}_shrunk_param_{}_{}.jpg'.format(
            self.fig_path, dupe_count, shrk, self.param_num, attrs_name), bbox_inches='tight')
        plt.show()

    def get_shrink_barh(self, irlvt_labels): ## irlvt_labels를 standard를 가지고 만들어낼 수 있지 않을까?
        self.df_pos_neg_shrink = self.df_pos_neg_shrink.loc[
            ~self.df_pos_neg_shrink.index.get_level_values('Label').isin(irlvt_labels)]
        self.df_pos_neg_shrink.reset_index(inplace=True)

        dupes = self.df_pos_neg_shrink[self.multi_indexes[0]].unique()
        for dupe in dupes:
            df_shrink_dupe = self.df_pos_neg_shrink.loc[
                self.df_pos_neg_shrink.Duplicated == dupe]
            attrs = df_shrink_dupe.Attributes.unique()
            for attr in attrs:
                df_shrink_attrs = df_shrink_dupe.loc[df_shrink_dupe.Attributes == attr]
                shrks = df_shrink_attrs.Shrunk.unique()
                for shrk in shrks:
                    df_shrink_shrk = df_shrink_attrs.loc[
                        df_shrink_attrs.Shrunk == shrk, 
                        ['Label', 'Positive', 'Negative']].set_index('Label')
                    self.draw_shrink_barh(df_shrink_shrk, dupe, attr, shrk)

    def get_unique_rules(self):
        def unique_labels(labels):
            if all(len(label) == 1 for label in labels):
                return labels
            
            set_labels = set(labels)
            set_labels_removed = set()
            for i in range(len(labels)):
                label = labels[i]
                len_label = len(label)
                for lb_1 in combinations(label, len_label-1):
                    prev_label_cand = ''.join(lb_1)
                    if prev_label_cand in set_labels:
                        set_labels_removed.add(prev_label_cand)
                
                if len_label > 1:
                    for lb_2 in combinations(label, len_label-2):
                        prev_prev_label_cand = ''.join(lb_2)
                        if prev_prev_label_cand in set_labels:
                            set_labels_removed.add(prev_prev_label_cand)
            
            return list(set_labels - set_labels_removed)

        self.df_neg_unique_rules = pd.DataFrame()
        self.df_pos_neg_shrink.reset_index(inplace=True)
        dupes = self.df_pos_neg_shrink[self.multi_indexes[0]].unique()
        for dupe in dupes:
            df_shrink_dupe = self.df_pos_neg_shrink.loc[
                self.df_pos_neg_shrink[self.multi_indexes[0]] == dupe]
            attrs = df_shrink_dupe[self.multi_indexes[1]].unique()
            for attr in attrs:
                df_shrink_attrs = df_shrink_dupe.loc[
                    df_shrink_dupe[self.multi_indexes[1]] == attr]
                list_labels = list(df_shrink_attrs[self.multi_indexes[-1]].unique())
                list_unique_labels = unique_labels(list_labels)

                df_unique_labels = df_shrink_attrs.loc[
                    df_shrink_attrs[self.multi_indexes[-1]].isin(list_unique_labels)]
                self.df_neg_unique_rules = pd.concat(
                    [self.df_neg_unique_rules, df_unique_labels], ignore_index=True)

        self.df_neg_unique_rules_indexed = self.df_neg_unique_rules.set_index(
            self.multi_indexes) ##
        self.df_neg_unique_rules.reset_index(drop=True, inplace=True)

        return self.df_neg_unique_rules

    def get_brand_rule(self, outfit_data, output_tf=False):
        set_brand = set()

        outfit_key = 'outfit' if output_tf else 'items'
        skuCode_key = 'sku_code' if output_tf else 'skuCode'

        outfit_data_pinch = [*outfit_data]
        for _outfit in outfit_data_pinch:
            for _item in _outfit[outfit_key]:
                skuCode = _item[skuCode_key]
                if skuCode not in self.dict_attrs:
                    outfit_data.remove(_outfit)
                    break
                    
        for _outfit in outfit_data:
            for _item in _outfit[outfit_key]:
                skuCode = _item[skuCode_key]
                set_brand.add(self.dict_attrs[skuCode]['brandName'])

        return set_brand

    def get_unique_neg_brand(self):        
        self.neg_brand_all = self.get_brand_rule(
            self.neg_new_data) | (self.neg_old_brand)

        if self.pos_old_brand:
            return self.neg_brand_all - self.pos_old_brand
        return self.neg_brand_all - self.get_brand_rule(self.pos_data)

    def get_neg_unique_rules(self, progress=True):
        if self.dict_neg_unique_rules_all['BRAND_RULE'] == []:
            print('\nNegative Unique BRAND Rules')

            self.dict_neg_unique_rules_all['BRAND_RULE'].extend(
                list(self.get_unique_neg_brand()))
        
            print('\nCount:', len(self.dict_neg_unique_rules_all['BRAND_RULE']))

        print('\nNegative Unique ITEM Rules')

        list_attrs = self.df_neg_unique_rules[self.multi_indexes[1]].unique()

        shrk_count = 0
        for attrs in list_attrs: ## Neg rule에서 labels 뺀 속성 조합 추가
            df_neg_unique_labels = self.df_neg_unique_rules.loc[
                self.df_neg_unique_rules[self.multi_indexes[1]] == attrs, 
                [self.multi_indexes[0], self.multi_indexes[-1]]]

            dupe = df_neg_unique_labels[self.multi_indexes[0]].unique()[0]
            labels = df_neg_unique_labels[self.multi_indexes[-1]].unique()

            dict_neg_labels = self.common_attrs_map[(dupe, attrs)]
            for label in labels:
                attrs_removed = [dict_neg_labels[l] if l not in [*label] else '*'
                                 for l in dict_neg_labels]

                dict_neg_unique_abbr = {'range': self.standard, 
                                        'value': attrs_removed}
                self.dict_neg_unique_rules_all['ITEM_RULE'].append(dict_neg_unique_abbr)
                shrk_count += 1
                
        if progress:
            print(self.df_neg_unique_rule_count.iloc[-1, 0], '=', shrk_count, sep='\t')

    def count_shrink_rules(self, set_name):
        if set_name == 'unique':
            df_rules = self.df_neg_unique_rules
            col_name = 'Negative Unique'

            # print('\nOriginal Only {} Attributes Rules: {}'.format(
            # ''.join([compo[0] if compo != 'TOP_L' else compo[-1] 
            #                         for compo in self.standard]),
            #                         len(self.common_only_std)))

        if set_name == 'generation':
            df_rules = self.df_gen_shrink
            col_name = 'Removed'

        list_len_labels = sorted(list(df_rules[self.multi_indexes[2]].unique()))
        list_values = [0] * (len(list_len_labels) + 1)
        
        df_rules_indexed = df_rules.set_index(self.multi_indexes)
        
        df_rule_count = pd.DataFrame(
            {'{} {} Rule Count'.format(col_name, self.standard_abbr): list_values},
            index=['{}개 제외'.format(i) for i in list_len_labels] + ['계'])
        
        for i in range(len(list_len_labels)):
            try:
                counted = len(
                    df_rules_indexed.xs(list_len_labels[i], level=2)
                    ) if set_name == 'unique' else df_rules_indexed.xs(
                        list_len_labels[i], level=2).iloc[:, 0].sum()

                df_rule_count.iloc[i, 0] = counted
                    
            except:
                pass

        df_rule_count.iloc[-1, 0] = df_rule_count.iloc[:, 0].sum()
        
        if set_name == 'unique':
            print('\nNeg Unique {} Rule Count\n'.format(self.standard_abbr))
            self.df_neg_unique_rule_count = df_rule_count.copy()
        if set_name == 'generation':
            print('\nGen Matched {} Rule Count\n'.format(self.standard_abbr))
            self.df_gen_shrink_count = df_rule_count.copy()

        print(df_rule_count, '\n')

        return df_rule_count

    def count_gen_shrunk(self, progress=True):
        self.count_attrs = list(set(
            self.df_neg_unique_rules.set_index(self.multi_indexes[:2]).index))

        self.dict_gen_attrs_ratio_std = {}
        self.dict_gen_attrs_orig_count = {}
        total_time = time.time()
        for i, (dupe, attrs) in enumerate(sorted(self.count_attrs)):
            self.dict_gen_attrs_ratio_std[attrs] = {}
            self.dict_gen_attrs_orig_count[attrs] = {}

            list_attrs = attrs.split(', ')

            df_dupe_attrs = self.df_neg_unique_rules_indexed.xs(
                (dupe, attrs), level=(0, 1))
            shrks = df_dupe_attrs.index.get_level_values(
                self.multi_indexes[2]).unique()

            for shrk in shrks:
                self.dict_gen_attrs_ratio_std[attrs][shrk] = {}
                self.dict_gen_attrs_orig_count[attrs][shrk] = {}

                labels = df_dupe_attrs.xs(shrk).index.get_level_values(
                    self.multi_indexes[-1])
                for label in labels:
                    if progress:
                        print(label, end='\t')
                    start_time = time.time()

                    attrs_shrunk = [*list_attrs]
                    attrs_std_shrk_cols = [*self.attrs_std_cols]
                    if label != 'Original':
                        removed_indexes = []
                        attrs_removed = [self.common_attrs_map[
                            (dupe, attrs)][l] for l in label]
                        for attr in attrs_removed:
                            attrs_shrunk.remove(attr)
                            removed_indexes.append(list_attrs.index(attr))
                            
                        for index in removed_indexes:
                            attrs_std_shrk_cols.remove(self.attrs_std_cols[index])

                    else:
                        attrs_removed = [label]
                    
                    gen_pruned = self.df_gen_passed[
                        attrs_std_shrk_cols].isin(attrs_shrunk).all(axis=1)
                    gen_shrink_count = sum(gen_pruned)

                    self.df_gen_passed = self.df_gen_passed[~gen_pruned]
                    
                    self.dict_gen_attrs_ratio_std[attrs][shrk][tuple(attrs_removed)
                        ] = gen_shrink_count
                    
                    if progress:
                        print('Time taken:', round(time.time() - start_time, 2))

        print('------------------------')
        print('Total Time taken:', round((time.time() - total_time) / 60, 4), 'min')                      

    def get_gen_shrink_dataframe(self, progress=True):
        self.df_gen_shrink = pd.DataFrame()
        for dupe, attrs in self.count_attrs:
            for shrk in self.dict_gen_attrs_ratio_std[attrs].keys():
                attr_labels = []
                if shrk != 0:
                    for attr_combi in self.dict_gen_attrs_ratio_std[attrs][shrk].keys():
                        attr_labels.append(''.join([self.get_key_by_value(
                            self.common_attrs_map[(dupe, attrs)], value) 
                                for value in attr_combi]))
                else:
                    attr_labels.append('Original')

                len_index = len(attr_labels)
                df_shrink = pd.DataFrame(
                    {'Removed Generation': list(self.dict_gen_attrs_ratio_std[attrs][shrk].values())},
                    index=[[dupe]*len_index, [attrs]*len_index, 
                            [shrk]*len_index, attr_labels])

                df_shrink.index.names = self.multi_indexes
                self.df_gen_shrink = pd.concat(
                    [self.df_gen_shrink, df_shrink], axis=0)

        self.df_gen_shrink.reset_index(inplace=True)

        return self.df_gen_shrink


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-s', '--standard', type=str, 
                        default='TOP,BOTTOM', 
                        help='Standard clothes set to extract')
    parser.add_argument('-m', '--max_shrink', type=int, default=4,
                        help='Max shrink number')
    parser.add_argument('-r', '--ratio_threshold', type=float, default=3 * (10 ** -4))
    parser.add_argument('-t', '--times_threshold', type=int, default=4)
    parser.add_argument('-l', '--len_abbr', type=int, default=3)

    parser.add_argument('--attrs', type=str, help='Attributes information data')
    parser.add_argument('--pos', type=str, help='Positive json data')
    parser.add_argument('--neg_new', type=str, help='New ver. Negative json data')
    parser.add_argument('--neg_old', type=str, help='Old ver. Negative csv data')
    parser.add_argument('--neg_old_brand', type=str, help='Old ver. Negative brand data')

    parser.add_argument('--rule_file', type=str, help='Negative rule output directory')

    ## To test, just leave them alone
    parser.add_argument('-d', '--dupe_counts', nargs='+', type=int, default=[22])
    parser.add_argument('-c', '--count_all', type=bool, default=True)
    parser.add_argument('-p', '--param_num', type=str, default='1')

    args = parser.parse_args()      

    ## Default Setting
    e = Extraction(file_new_attrs=args.attrs, file_pos=args.pos, 
                   file_neg_new=args.neg_new, file_neg_old=args.neg_old,
                   file_neg_old_brand=args.neg_old_brand)
    
    e.rule_path = args.rule_file
    e.max_shrink = args.max_shrink
    e.ratio_threshold = args.ratio_threshold
    e.times_threshold = args.times_threshold
    e.dupe_counts = args.dupe_counts
    e.count_all = args.count_all
    e.param_num = args.param_num

    rule_time_taken = 0
    e.df_neg_unique_rule_count_all = pd.DataFrame()
    print('Portioning...')
    for list_standard in e.composition:
        e.standard_abbr = ''.join([compo[0] if compo != 'TOP_L' else compo[-1] 
                                   for compo in list_standard])
        if len(e.standard_abbr) <= args.len_abbr: # TB, OTB, TBS
            print('--------------------------------------------')
            print(e.standard_abbr)
            print('--------------------------------------------')

            e.prepare_common_attrs(list_standard)

            ## Get Rid of Intersection with Positive Only Standard ATTR Set
            e.get_only_attr_set(e.common_from_num_all_std)
            e.get_only_attr_set(e.common_only_std)

            common_attrs_map = e.get_attrs_map(e.common_from_num_all_std)

            ## Negative All Portioning
            start_time = time.time()

            e.get_shrink_dictionary(e.common_from_num_all_std, progress=False)
            e.get_shrink_dataframe(e.common_from_num_all_std, progress=False)
            
            portioning_time = time.time() - start_time
            rule_time_taken += portioning_time

            if not e.df_pos_neg_shrink.empty:
                start_time = time.time()

                e.get_unique_rules()
                e.count_shrink_rules('unique')
                e.get_neg_unique_rules()
                
                sifting_time = time.time() - start_time
                rule_time_taken += sifting_time

                e.df_neg_unique_rule_count_all = pd.concat(
                    [e.df_neg_unique_rule_count_all, e.df_neg_unique_rule_count])

    print('\nPortioning Negative Rule Taken:', round(rule_time_taken / 60, 4), 'min')

    save_dir = '/'.join(e.rule_path.split('/')[:-1])
    if not os.path.isdir(save_dir):
        os.makedirs(save_dir, exist_ok=True)

    ## Save Outputs
    with open(e.rule_path, 'w') as f:
        json.dump(e.dict_neg_unique_rules_all, f, ensure_ascii=False, indent=4)