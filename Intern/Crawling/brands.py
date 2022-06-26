import os
import csv

from bs4 import BeautifulSoup
from urllib.request import urlopen
from urllib.request import urlretrieve

brand_dict = {'LYNN': 'L',
             'LINE': 'N',
             'KENNETH LADY': 'E',
             'KL': 'K',
             'NUVO10': 'V',
             'MOE': 'O',
             'HUIT DE LYNN': 'H',
             'LINE STUDIO ONE': 'J',
             'DEAR K': 'S'}

category_dict = {
    'Outer': ['Down/Padding', 'Coat', 'Jacket', 'Jumper', 'Trench Coat'],
    'Top': ['Blouse', 'Tee', 'Knitwear'],
    'Bottom': ['Skirt', 'Pants', 'Denim'],
    'Dress': ['Dress', 'Jumpsuit']
}

subcategory_dict = {
    'Down/Padding': 'http://bylynn.shop/goods/category.do?cate=1010&brandCd=',
    'Coat': 'http://bylynn.shop/goods/category.do?cate=1020&brandCd=',
    'Jacket': 'http://bylynn.shop/goods/category.do?cate=1030&brandCd=',
    'Jumper': 'http://bylynn.shop/goods/category.do?cate=1040&brandCd=',
    'Trench Coat': 'http://bylynn.shop/goods/category.do?cate=1070&brandCd=',
    'Blouse': 'http://bylynn.shop/goods/category.do?cate=2010&brandCd=',
    'Tee': 'http://bylynn.shop/goods/category.do?cate=2020&brandCd=',
    'Knitwear': 'http://bylynn.shop/goods/category.do?cate=2030&brandCd=',
    'Skirt': 'http://bylynn.shop/goods/category.do?cate=3010&brandCd=',
    'Pants': 'http://bylynn.shop/goods/category.do?cate=3020&brandCd=',
    'Denim': 'http://bylynn.shop/goods/category.do?cate=3030&brandCd=',
    'Dress': 'http://bylynn.shop/goods/category.do?cate=4010&brandCd=',
    'Jumpsuit': 'http://bylynn.shop/goods/category.do?cate=4020&brandCd='
}

with open('Total.csv', 'a', newline='') as f:
    wr = csv.writer(f, quoting=csv.QUOTE_ALL)
    wr.writerow(['Image', 'Brand', 'Category', 'Subcategory'])

def getURL(brandKey, subcategoryKey):
    return subcategory_dict[subcategoryKey] + brand_dict[brandKey]

def checkProduct(soup):
    check = soup.find("div", class_="productlist5")
    return check.find("li", class_="none")

for brand in brand_dict.keys():
    print('<' + brand + '>' + '\n')
    for subcategory in subcategory_dict.keys():
        html = urlopen(getURL(brand, subcategory))
        soup = BeautifulSoup(html, "html.parser")
        
        if checkProduct(soup):
            print("No product in " + subcategory)
            continue                                              # No product

        else:
            imgs = soup.find_all("img", class_="default")
        
            for img in imgs:
                imgName = img["src"].split("/")[-3]
                temp = [imgName, brand, subcategory]
                insertIndex = 2
                infoList = temp[:]
                infoList[insertIndex:insertIndex] = \
                    [key for key, value in category_dict.items() if subcategory in value]
                
                with open('Total.csv', 'a', newline='') as f:   # Write to csv
                    wr = csv.writer(f, quoting=csv.QUOTE_ALL)
                    wr.writerow(infoList)
            
                savePath = os.path.join("./Img/", imgName + ".png")
                urlretrieve(img["src"], savePath)
            
            print(subcategory + " is done")
    print('\n----------------------------\n')