import scrapy

class EventSpider(scrapy.Spider):
    name = 'event'

    start_urls = ['http://www.mdx.ac.uk/events']

    def parse(self, response):
        for href in response.css('.listing-event ul li a'):
            yield response.follow(href, self.parse_event)

        for href in response.css('.next a'):
            yield response.follow(href, self.parse)


    def parse_event(self, response):
        def extract_with_xpath(query):
            return response.selector.xpath(query).extract_first().strip()


        yield {
            'title': extract_with_xpath('//h1/text()'),
            'start_date': extract_with_xpath('(//span[@class="event-description"])[1]/text()'),
            'start_time': extract_with_xpath('(//span[@class="event-description"])[2]/text()'),
            'location': extract_with_xpath('(//span[@class="event-description"])[3]/p/text()'),
            'end_date': extract_with_xpath('(//span[@class="event-description"])[4]/text()'),
            'end_time': extract_with_xpath('(//span[@class="event-description"])[5]/text()')
        }

