# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

import dbConnection as db
from scrapy.exceptions import DropItem


class FAQPipeline(object):

    def process_item(self, item, spider):
        question = ''.join(item['question']).replace("\r","").replace("\n","").replace("\t","").replace("\xa0", " ").strip()
        answer = ''.join(item['answer']).replace("\r","").replace("\n","").replace("\t","").replace("\xa0", " ").strip()
        if question != "" and answer != "" :
            db.addFAQToProject(item['projectId'], question, answer, item['url'])
            return item
        else:
            raise DropItem("Missing either a question or an answer!")