import json
from time import sleep
import requests
import os
from bs4 import BeautifulSoup

requests.packages.urllib3.disable_warnings()
sess = requests.Session()
sess.headers.setdefault('User-Agent',
                        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.41')
sess.headers.setdefault('Host', 'club.xywy.com')
sess.headers.setdefault('Sec-Ch-Ua-Platform', '"Windows"')
sess.headers.setdefault('Sec-Ch-Ua-Mobile', '?0')
sess.headers.setdefault('Dnt', '1')
import csv


def get_all_links():
    """
    获取二级目录下所有链接,将保存所有链接
    :return:
    """
    url_id_list = {
        "288": 500,
        "289": 500,
        "697": 295,
        "655": 253,
        "859": 1}

    for url_id in url_id_list.keys():
        for i in range(1, url_id_list[url_id] + 1):
            res = sess.get("https://club.xywy.com/list_{}_all_{}.htm".format(url_id, i), verify=False)
            res.encoding = res.apparent_encoding
            soup = BeautifulSoup(res.text, 'html.parser')
            link_list = soup.find_all("a", attrs={"class": "fl th"})
            for link_list_find in link_list:
                link_list_find_href = link_list_find.get('href')
                link_list_find_text = link_list_find.text
                url = "https://club.xywy.com" + link_list_find_href
                # link_dict = {
                #     "{}".format(link_list_find_text): link_list_find_href
                # }
                write_info = f"{link_list_find_text},{url}\n"
                with open(f"{url_id}.txt", "a", encoding="utf-8") as f:
                    f.write(write_info)
                print(url_id, i, link_list_find_text, url)
            sleep(1)


def all_info_to_txt(file):
    """
    读取所有链接,并爬取保存
    :param file:
    :return:
    """

    csv_info = r'D:\python项目\爬虫\寻医问药\all_{}.csv'.format(file[-7:-4])
    csv_file = open(csv_info,'w', newline='', encoding='utf-8')
    writer = csv.writer(csv_file)
    with open(file, "r", encoding="utf-8") as f:
        a = f.readlines()
        for num, i in enumerate(a, 1):
            url = i.split(",")[-1]
            url = url.replace("\n", "")
            try:
                res = sess.get(url, verify=False)
                res.encoding = res.apparent_encoding
                soup = BeautifulSoup(res.text, 'html.parser')

                # 二级分类
                type = soup.find("div", attrs={"class": "clearfix content menu-Breadcrumb"})
                type = type.find_all("a")[-1].text

                # 标题
                wt_title = soup.find("h3", attrs={"class": "fl"}).text
                # 内容
                wt_info = soup.find("div", attrs={"class": "details-con clearfix"}).text
                wt_info = wt_info.strip()
                # 时间
                wt_time = soup.find("div", attrs={"class": "user-infor clearfix"})
                wt_time = wt_time.find_all("span")[3].text
                # 答案
                if soup.find("div", attrs={"class": "replay-content-box clearfix"}):
                    daan = soup.find("div", attrs={"class": "replay-content-box clearfix"}).text.strip()
                else:
                    daan = ""
                write_info = f"妇产科,{type},{wt_title},{wt_time},{wt_info},{daan}\n"
            except Exception:
                print("出错")
                with open(file + "_errpr.txt", "a", encoding="utf-8") as f:
                    f.write(url)
            else:
                writer.writerow(['妇产科', type, wt_title, wt_info, wt_time, daan])
                print(file[-7:-4], num, url, wt_title, wt_time)
        csv_file.close()


def get_other_url():
    """
    词典库爬取
    :return:
    """
    url = "https://club.xywy.com/juhe/fuchanke"
    res = sess.get(url, verify=False)
    res.encoding = res.apparent_encoding
    soup = BeautifulSoup(res.text, 'html.parser')
    info = soup.find("div", attrs={"class": "ejri mb10 mt15"})
    info = info.find_all("li")

    csv_file = open('妇产科_词典库.csv', 'w', newline='', encoding='utf-8')
    writer = csv.writer(csv_file)
    writer.writerow(['妇产科_词典库'])
    for i in info:
        writer.writerow([i.text])
    csv_file.close()



def all_txt_file_to_csv():
    """
    将所有信息合并成csv文件
    :return:
    """
    filePath = r"/寻医问药/all_csv"
    csv_file = open(r'/寻医问药/妇产科_所有信息.csv', 'w', newline='', encoding='utf-8')
    writer = csv.writer(csv_file)
    writer.writerow(['科室', "二级科室", "标题", "问题", "提问时间", "答案"])
    x_list = os.listdir(filePath)
    for file in x_list:
        file = filePath + "\\" + file
        print(file)
        with open(file,"r",encoding="utf-8") as csvfile:
            csv_reader = csv.reader(csvfile)  # 使用csv.reader读取csvfile中的文件
            # header = next(csv_reader)        # 读取第一行每一列的标题
            for row in csv_reader:  # 将csv 文件中的数据保存到data中
                writer.writerow(row)

    csv_file.close()


if __name__ == "__main__":
    get_other_url()  # 词典库爬取
    get_all_links()  # 获取所有链接
    # 多线程爬取所有内容
    from threading import Thread
    filePath = r"D:\python项目\爬虫\寻医问药\all_link"
    x_list = os.listdir(filePath)
    threads = []

    for i in x_list:
        file = filePath + "\\" + i
        print(file)
        threads.append(Thread(target=all_info_to_txt, args=(file,)))
        threads[-1].start()
    for thread in threads:
        thread.join()

    all_txt_file_to_csv() # 合并文件
