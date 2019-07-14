# Imports the Google Cloud client library
from google.cloud import datastore
import datetime

# Instantiates a client
client = datastore.Client()

def createProject(title, base_URL, proj_type):
    # The kind for the new entity
    complete_key = client.key('Project')

    project = datastore.Entity(key=complete_key)

    project.update({
        'Title': title,
        'BaseURL': base_URL,
        'Type': proj_type,
        'ScrapeFields': [],
        'FAQs': [],
        'Scraped': False,
    })

    client.put(project)

    project_id = project.key.id_or_name
    return project_id

def addFAQToProject(project_id, question_text, answer_text, page_URL):
    if (isinstance(project_id, str)):
        project_id = int(project_id)
    key = client.key('Project', project_id)
    project = client.get(key)

    faq = datastore.Entity(None, exclude_from_indexes=['Answer'])

    faq.update({
        'Question': question_text,
        'Answer': answer_text,
        'PageURL': page_URL,
        'Paraphrases': []
    })

    try:
        project['FAQs'].append(faq)
    except KeyError:
        project['FAQs'] = [faq]

    client.put(project)

    faq_id = len(project['FAQs']) - 1

    return [project_id, faq_id]


def addParaphraseToFAQ(project_id, faq_id, paraphrase_text, user):
    key = client.key('Project', project_id)
    project = client.get(key)

    paraphrase = datastore.Entity()

    paraphrase.update({
        'ParaphraseText': paraphrase_text,
        'User': user,
        'Timestamp': datetime.datetime.utcnow().isoformat("T") + "Z"
    })

    try:
        project['FAQs'][faq_id]['Paraphrases'].append(paraphrase)
    except KeyError:
        project['FAQs'][faq_id]['Paraphrases'] = [paraphrase]

    client.put(project)

    paraphrase_id = len(project['FAQs'][faq_id]['Paraphrases']) - 1

    return [project_id, faq_id, paraphrase_id]

def prettyList():
    query = client.query(kind='Project')
    results = list(query.fetch())
    for project in results:
        print(project["Title"])
        try:
            for faq in project["FAQs"]:
                print("- " + faq["Question"])
                try:
                    for paraphrase in faq["Paraphrases"]:
                        print("-- " + paraphrase["ParaphraseText"])
                except KeyError:
                    pass
        except KeyError:
            pass

def getUnscrapedProjects():
    query = client.query(kind='Project')
    query.add_filter('Scraped', '=', False)
    results = query.fetch()
    return results

def markAsScraped(project_id):
    key = client.key('Project', project_id)
    project = client.get(key)

    project['Scraped'] = True
    
    client.put(project)