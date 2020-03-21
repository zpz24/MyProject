from collections import Counter
import re
from flask import Flask, jsonify, request
from newsapi import NewsApiClient
from newsapi.newsapi_exception import NewsAPIException

application = Flask(__name__)

newsapi = NewsApiClient(api_key='28fde6b7925a4b46855abf38ab112187')


def clear(json, num):
    count = 0
    after = []
    sign = 0
    for member in json['articles']:
        if num == count:
            break

        if member is None:
            continue

        for k in member:
            if k == 'source':
                if member[k]['id'] is None or member[k]['name'] is None or member[k]['id'] == "" \
                        or member[k]['name'] == "" or member[k]['id'] == "null" or member[k]['name'] == "null":
                    sign = 1
                    break
            else:
                if member[k] is None or member[k] == '':
                    sign = 1
                    break
        if sign == 1:
            sign = 0
        else:
            sign = 0
            count += 1
            after.append(member)

    json['articles'] = after
    return json


def filt(file):
    articles = file["articles"]
    flag = 0
    li = []
    for article in articles:
        if article is None:
            continue
        for key in article:
            if key == "source":
                if article[key]["name"] == "" or article[key]["name"] is None or article[key]["name"] == "null":
                    flag = 1
                    break
            if article[key] is None or article[key] == "" or article[key] == "null":
                flag = 1
                break
        if flag == 1:
            flag = 0
        else:
            li.append(article)

    file["articles"] = li
    return file


f = open('stopwords_en.txt')
lines = f.readlines()
word_list = []
for line in lines:
    word_list.append(line.strip())


def cloud(json):
    articles = json['articles']
    words = []
    # append split words of title into an array
    for ar in articles:
        for w in ar['title'].split():
            if w.lower() in word_list:
                continue
            words.append(w)

    for i, word in enumerate(words):
        words[i] = re.sub(r'[^A-Za-z0-9]', '', word)
        if words[i] == '':
            words.remove(words[i])
    dic = dict(Counter(words).most_common(30))
    data = []
    for key, value in dic.items():
        data.append({"word": key, "size": str(value)})

    return data


@application.route('/', methods=['GET'])
def get_index():
    return application.send_static_file('front.html')


@application.route('/api/news/<source>', methods=['GET'])
def get_head(source):
    if source == 'top':
        try:
            top_headlines = newsapi.get_top_headlines(language='en', page_size=30)
            row = cloud(top_headlines)
            top_headlines['count'] = row
            top_headlines = clear(top_headlines, 5)

        except NewsAPIException as er:
            return jsonify(er.exception)
    else:
        try:
            top_headlines = newsapi.get_top_headlines(sources=source, language='en', page_size=30)
            top_headlines = clear(top_headlines, 4)

        except NewsAPIException as er:
            return jsonify(er.exception)

    return jsonify(top_headlines)


@application.route('/api/source/initial', methods=['GET'])
def init_source():
    sources = newsapi.get_sources(language="en", country="us")
    elements = sources["sources"]
    li1 = []
    li2 = []
    for element in elements:
        if element["name"] not in li2 and element["id"] is not None and element["name"] is not None:
            li2.append(element["name"])
            li1.append(dict(id=element["id"], name=element["name"]))
    sources["sources"] = li1
    return jsonify(sources)


@application.route('/api/source/aim', methods=['GET'])
def get_source():
    sources = newsapi.get_sources(language="en", country="us")
    elements = sources["sources"]
    li1 = []
    li2 = []
    cate = request.args.get("category")
    for element in elements:
        if element["category"] == cate:
            if element["name"] not in li2 and element["id"] is not None and element["name"] is not None:
                li2.append(element["name"])
                li1.append(dict(id=element["id"], name=element["name"]))

    sources["sources"] = li1
    return jsonify(sources)


@application.route('/api/news/search', methods=['GET'])
def search():
    key = request.args.get("keyword")
    start_date = request.args.get("st_date")
    end_date = request.args.get("ed_date")
    src = request.args.get("source")

    if src == "all":
        try:
            all_news = newsapi.get_everything(q=key, from_param=start_date, to=end_date, language='en',
                                              page_size=30, sort_by="publishedAt")
        except NewsAPIException as er:
            return jsonify(er.exception)

    else:
        try:
            all_news = newsapi.get_everything(q=key, from_param=start_date, to=end_date, sources=src, language='en',
                                              page_size=30, sort_by="publishedAt")
        except NewsAPIException as er:
            return jsonify(er.exception)
    news = filt(all_news)
    articles = news["articles"]
    li = []
    index = 0
    for i in range(len(articles)):
        if index == 15:
            break
        li.append(articles[index])
        index += 1
    news["articles"] = li
    return jsonify(news)


if __name__ == '__main__':
    application.run()
