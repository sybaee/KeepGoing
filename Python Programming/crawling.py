"""
Description: naver weather webpage의 weather information을 이용하여 당일부터 일주일간의 날씨 알아보기
"""

import urllib.request

def process_webpage():
    webpage = urllib.request.urlopen(url) # HTTP Response라는 data object가 return
    out = open(fname, "w")
    for line in webpage:
        line = str(line) # 나라별 언어가 달라서 bytes로 저장되어 있던 걸 string으로 data type 바꾸기
        out.write(line.strip() + "\n")
    webpage.close()
    out.close()

def extract_date(line): # 2가지 방법으로 쓰여 있는 날짜를 찾아내기
    if "<span>(" in line:
        tdate = line[line.find("(") + 1 : line.find(")")]
    else:
        skip_len = len("<span>")
        start_idx = line.find("<span>") + skip_len
        tdate = line[start_idx : line.find("<", start_idx)]
        tdate = tdate.strip(".")
        tdate = tdate.replace(".", "/") # 달/일 표현형으로 통일하기
        
    return tdate

def extract_temperature(line): # 온도 찾기
    skip_len = len('<span class="temp">')
    start_idx = line.find('<span class="temp">') + skip_len
    end_idx = line.find("<", start_idx)
    temp = line[start_idx : end_idx]
    return temp

def print_weather():
    f = open('montp.html', "r")
    min_flag = True
    for line in f:
        if '<th scope="col"' in line:
            wdate = extract_date(line)
            dates.append(wdate)
        elif '<th scope="row"' in line:
            wdate = extract_date(line)
            dates.append(wdate)

        if '<li class="nm">' in line:
            temp = extract_temperature(line)
            if min_flag == True:
                min_tp.append(temp) # 오전 기온 
                min_flag = False
            else:
                max_tp.append(temp) # 오후 기온
                min_flag = True

    for i in range(len(dates)):
        print("%s:\t%5s ~ %5s" % (dates[i], min_tp[i], max_tp[i])) # 당일부터 일주일간의 최저 기온과 최고 기온 나열

    f.close()

def main():
    process_webpage()
    print_weather()

url = "http://weather.naver.com/rgn/cityWetrCity.nhn?cityRgnCd=CT007023"
fname = "./montp.html" # ./ = cwd (current working directory)

dates, min_tp, max_tp = [], [], []

main()