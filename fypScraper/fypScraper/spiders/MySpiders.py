import scrapy
from scrapy.selector import Selector

class DynamicMultiPageSpider(scrapy.Spider):

    def __init__(self, id, title, base_url, scrape_fields):
        self.id = id
        self.name = title
        self.start_urls = [base_url]
        self.scrape_fields = scrape_fields
        for field in scrape_fields:
            if field['Title'] == 'faq_instance_link_0':
                self.faq_instance_link_field_0 = field
            elif field['Title'] == 'faq_instance_link_1':
                self.faq_instance_link_field_1 = field
            elif field['Title'] == 'pagination_link':
                self.pagination_link_field = field
            elif field['Title'] == 'faq_instance_selector':
                self.faq_instance_selector_field = field

    def proccessMenuTwo(self, response):

        if self.faq_instance_link_field_1['Type'] == 'XPath':
             self.faq_instance_link_field_1_response = response.xpath(self.faq_instance_link_field_1['Selector'])
        elif self.faq_instance_link_field_1['Type'] == 'CSS':
            self.faq_instance_link_field_1_response = response.css(self.faq_instance_link_field_1['Selector'])

        for href in self.faq_instance_link_field_1_response:
                yield response.follow(href, self.parse_faqs)

    def parse(self, response):
        self.multiplePagesFlag = False

        if hasattr(self, 'faq_instance_link_field_0'):
            self.multiplePagesFlag = True
            if self.faq_instance_link_field_0['Type'] == 'XPath':
                 self.faq_instance_link_field_0_response = response.xpath(self.faq_instance_link_field_0['Selector'])
            elif self.faq_instance_link_field_0['Type'] == 'CSS':
                self.faq_instance_link_field_0_response = response.css(self.faq_instance_link_field_0['Selector'])


            if hasattr(self, 'faq_instance_link_field_1'):
                for href in self.faq_instance_link_field_0_response:
                    yield response.follow(href, self.proccessMenuTwo)
            else:
                for href in self.faq_instance_link_field_0_response:
                    yield response.follow(href, self.parse_faqs)

        if hasattr(self, 'pagination_link_field'):
            self.multiplePagesFlag = True
            if self.pagination_link_field['Type'] == "XPath":
                next_page = response.xpath(self.pagination_link_field['Selector'])
            elif self.pagination_link_field['Type'] == "CSS":
                next_page = response.css(self.pagination_link_field['Selector'])

            for href in next_page:
                yield response.follow(href, self.parse)

        if not self.multiplePagesFlag:
            yield scrapy.Request(url=self.start_urls[0], callback=self.parse_faqs)


    def parse_faqs(self, response):
        if not hasattr(self, 'faq_instance_selector_field'):
            instanceIterator = response.css('body')
            for faq in instanceIterator:
                extractors = {}
                extractors['projectId'] = self.id
                extractors['url'] = response.url
                for field in self.scrape_fields:
                    if field['Title'] == "question" or field['Title'] == "answer":
                        if field['Type'] == 'XPath':
                            [field['Title']] = faq.xpath(field['Selector']).extract()
                        elif field['Type'] == 'CSS':
                            extractors[field['Title']] = faq.css(field['Selector']).extract()
                yield (extractors)
        else:
            if self.faq_instance_selector_field['Type'] == 'CSS':
                instanceIterator = response.css(self.faq_instance_selector_field['Selector'])
            else:
                instanceIterator = response.xpath(self.faq_instance_selector_field['Selector'])

            for faq in instanceIterator:
                extractors = {}
                extractors['projectId'] = self.id
                extractors['url'] = response.url
                for field in self.scrape_fields:
                    if field['Title'] == "question" or field['Title'] == "answer":
                        if field['Type'] == 'XPath':
                            extractors[field['Title']] = faq.xpath(field['Selector']).extract_first()
                        elif field['Type'] == 'CSS':
                            extractors[field['Title']] = faq.css(field['Selector']).extract()
                yield(extractors)

class SourceSpider(scrapy.Spider):

    def __init__(self, id, title, base_url, scrape_fields, source):
        self.id = id
        self.name = title
        self.start_urls = [base_url]
        self.scrape_fields = scrape_fields
        self.source = source
        for field in scrape_fields:
            if field['Title'] == 'faq_instance_selector':
                self.faq_instance_selector_field = field
                break

    def parse(self, response):

        yield scrapy.Request(url=self.start_urls[0], callback=self.parse_faqs)


    def parse_faqs(self, response):
        sourceSelector = Selector(text=self.source)
        if not hasattr(self, 'faq_instance_selector_field'):
            instanceIterator = sourceSelector.css('body')
            for faq in instanceIterator:
                extractors = {}
                extractors['projectId'] = self.id
                extractors['url'] = self.start_urls[0]
                for field in self.scrape_fields:
                    if field['Title'] == "question" or field['Title'] == "answer":
                        if field['Type'] == 'XPath':
                            [field['Title']] = faq.xpath(field['Selector']).extract()
                        elif field['Type'] == 'CSS':
                            extractors[field['Title']] = faq.css(field['Selector']).extract()
                yield (extractors)
        else:
            if self.faq_instance_selector_field['Type'] == 'CSS':
                instanceIterator = sourceSelector.css(self.faq_instance_selector_field['Selector'])
            else:
                instanceIterator = sourceSelector.xpath(self.faq_instance_selector_field['Selector'])

            for faq in instanceIterator:
                extractors = {}
                extractors['projectId'] = self.id
                extractors['url'] = self.start_urls[0]
                for field in self.scrape_fields:
                    if field['Title'] == "question" or field['Title'] == "answer":
                        if field['Type'] == 'XPath':
                            extractors[field['Title']] = faq.xpath(field['Selector']).extract_first()
                        elif field['Type'] == 'CSS':
                            extractors[field['Title']] = faq.css(field['Selector']).extract()
                yield(extractors)