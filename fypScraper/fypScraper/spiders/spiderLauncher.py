from scrapy.crawler import CrawlerProcess
from MySpiders import DynamicMultiPageSpider, SourceSpider
import sys
import os
from pathlib import Path
from scrapy.utils.project import get_project_settings

scriptPath = os.path.split(os.path.dirname(os.path.realpath(__file__)))[0]
sys.path.append(scriptPath)
import dbConnection as db

projects = db.getUnscrapedProjects()
process = CrawlerProcess(get_project_settings())

for project in projects:
    id = project.key.id_or_name
    title = project['Title']
    base_url = project['BaseURL']
    scrape_fields = project['ScrapeFields']
    source = project['Source']
    db.markAsScraped(id)

    if source == "":
        process.crawl(DynamicMultiPageSpider, id=id, title=title, base_url=base_url, scrape_fields=scrape_fields)
    else:
        process.crawl(SourceSpider, id=id, title=title, base_url=base_url, scrape_fields=scrape_fields, source=source)

    projects = db.getUnscrapedProjects()
process.start()


