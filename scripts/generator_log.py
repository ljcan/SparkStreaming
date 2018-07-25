#coding=UTF-8

import random
import time

url_path=[
    "class/112.html",
    "class/125.html",
    "class/128.html",
    "class/145.html",
    "learn/158",
    "learn/142",
    "class/131.html",
    "class/130.html",
    "course/list"
]
ip_slices=[132,154,168,192,110,25,48,46,187,125,29,64,47,159,164,138,22,59]

http_references=[
    "https://www.duba.com/?f={query}",
    "https://www.baidu.com/s?wd={query}",
    "https://www.sogou.com/web?query={query}",
    "https://cn.bing.com/search?q={query}",
    "https://search.yahoo.com/search?p={query}"
]

search_keyword=[
    "Spark SQL实战",
    "Spark Streaming实战",
    "Storm实战",
    "大数据基础",
    "大数据面试",
    "机器学习"
]

status=[200,404,500]

def sample_log():
    return random.sample(url_path,1)[0]
def sample_ip():
    slices= random.sample(ip_slices,4)
    return ".".join([str(item) for item in slices])

def sample_references():
    if random.uniform(0,1)>0.2:
        return "-"

    refer_str=random.sample(http_references,1)
    query_str=random.sample(search_keyword,1)
    return refer_str[0].format(query=query_str[0])

def sample_status():
    return random.sample(status,1)[0]

def generator_log(count=10):
    time_str=time.strftime("%Y-%m-%d %H:%M:%S",time.localtime())
    f=open("/opt/project/data/access.log","w+")
    while count>=1:
        query_log="{ip}\t{time}\t\"GET /{url} HTTP/1.1\"\t{status}\t{referer}".format(time=time_str,url=sample_log(),ip=sample_ip(),referer=sample_references(),status=sample_status())
        f.write(query_log+"\n")
        print query_log
        count=count-1


if __name__=='__main__':
    generator_log(100)


