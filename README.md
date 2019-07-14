# Machine Learning Training Tool
A tool to scrape Frequently Asked Questions (FAQs) from websites and presents them in a portal that allows users to suggest possible ways of paraphrasing the questions. The purpose of the tool is to help in the data gathering phase of Natural Language Processing (NLP). 

## Acknowledgment
This tool was created as part of a final year project at Middlesex University, under the supervision of Prof. Franco Raimondi ([@fraimondi]( https://github.com/fraimondi )) and in conjunction with [Kare Knowledgeware](https://karehq.com) (formerly Gluru).

## Setup
### Google Datastore
1. Create a Google Cloud Account (if you don't already have one: [here](https://cloud.google.com/free/))
2. Create a new project (and take note of the project id)
3. Create a new Database instance [here](https://console.cloud.google.com/datastore/welcome) - Make sure to use **_Cloud Firestore in Native Mode_**
4. Create a 'Service account key' [here][https://console.cloud.google.com/apis/credentials] with at least the role 'Cloud Datastore User' - and store the resulting JSON     file in a secure location.
5. Set the required environment variables:  
`export GOOGLE_APPLICATION_CREDENTIALS=<path_to_service_account_json_file>`  
`export GOOGLE_PROJECT_ID=<project_id>`

### The Go WebServer
1. Install GoLang [here](https://golang.org/) (it was developed using `go1.9.1`)
2. Navigate to the `/go-server` directory within this repo
3. Run `go get` to install the application dependencies
4. Run `go run main.go` to run the server (runs on port 9090 by default)

### The Python Web Scraper
1. Install Python 3 [here](https://www.python.org/) (it was developed using `Python 3.5.0`)
2. _(optional)_ create a [virtual environment](https://docs.python.org/3/library/venv.html) to isolate modules from the global instance
3. Navigate to the `/fypScraper` directory within this repo
4. Run `pip install -r requirements.txt` to install the application dependencies
5. Configure a [cron job](https://help.ubuntu.com/community/CronHowto) to run the `/fypScraper/fypScraper/spiders/spiderLauncher.py` script at a given interval (at each excecution, the script will find and process any unscraped websites found in the datastore), for example:  
`* * * * * <python3_path> <path_to_repo>/fypScraper/fypScraper/spiders/spiderLauncher.py` will run the scraper to check every minute
