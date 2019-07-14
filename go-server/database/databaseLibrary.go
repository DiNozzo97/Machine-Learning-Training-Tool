package database

import (
	"log"
	"os"
	"strconv"
	"time"
	// Imports the Google Cloud Datastore client package.
	"cloud.google.com/go/datastore"
	"golang.org/x/net/context"
	"google.golang.org/api/iterator"

	"math/rand"
)

type ScrapeField struct {
	Title    string
	Type     string
	Selector string
}

type Paraphrase struct {
	ParaphraseText string
	User           string
	Timestamp      string
}

type FAQ struct {
	Question    string
	Answer      string `datastore:",noindex"`
	PageURL     string
	Paraphrases []Paraphrase
}

type Project struct {
	Title        string
	BaseURL      string
	Type         string
	ScrapeFields []ScrapeField
	FAQs         []FAQ
	Scraped      bool
	Source       string `datastore:",noindex"`
}

type ProjectListing struct {
	Id    int
	Title string
}

type FaqIndex struct {
	FAQ   FAQ
	index int
}

var client, ctx = setupDB()

func setupDB() (*datastore.Client, context.Context) {
	ctx := context.Background()

	// Set your Google Cloud Platform project ID.
	projectID := os.Getenv("GOOGLE_PROJECT_ID")

	// Creates a client.
	client, err := datastore.NewClient(ctx, projectID)
	if err != nil {
		log.Fatalf("Failed to create client: %v", err)
	}

	return client, ctx
}

func FetchFAQToParaphrase(project *Project, user string) (FAQ, int) {
	faqs := project.FAQs
	if len(faqs) == 0 {
		log.Println("Error, no FAQs were found in Project.")
		return FAQ{}, -1 // -1 value means that no faqs were found in the project
	}
	var indexedFaqs []FaqIndex
	for index, faq := range faqs {
		indexedFaqs = append(indexedFaqs, FaqIndex{faq, index})
	}
newFaq:
	for _, faq := range shuffleFaqs(indexedFaqs) {
		for _, phrase := range faq.FAQ.Paraphrases {
			if phrase.User == user {
				continue newFaq
			}
		}
		return faq.FAQ, faq.index
	}
	return FAQ{}, -2 // -2 value means the user has already paraphrased all of the scraped FAQs
}

func ListProjects() []ProjectListing {
	q := datastore.NewQuery("Project")
	var projects []ProjectListing

	for project := client.Run(ctx, q); ; {
		var x Project
		key, err := project.Next(&x)
		if err == iterator.Done {
			break
		}
		if err != nil {
			// Handle error.
		}
		projects = append(projects, ProjectListing{int(key.ID), x.Title})
	}
	return projects

}

func FetchProject(id string) (*datastore.Key, *Project) {
	idInt, convertErr := strconv.ParseInt(id, 10, 64)

	if convertErr != nil {
		log.Println("Error: Unable to Convert string to int. \n")
		return nil, nil
	}

	key := datastore.IDKey("Project", idInt, nil)
	project := &Project{}

	if err := client.Get(ctx, key, project); err != nil {
		log.Println("Error: Unable to locate project. \n")
		return nil, nil
	}

	return key, project

}

func StoreParaphrase(paraphraseText string, user string, project *Project, faqID int, key *datastore.Key) {
	faqs := project.FAQs
	faq := faqs[faqID]
	currentTime := time.Now()

	timestamp := currentTime.Format("2006-01-02T15:04:05.000Z")

	paraphrase := Paraphrase{paraphraseText, user, timestamp}

	updatedParaphraseArray := append(faq.Paraphrases, paraphrase)
	faq.Paraphrases = updatedParaphraseArray
	faqs[faqID] = faq
	project.FAQs = faqs

	_, err := client.Put(ctx, key, project)

	if err != nil {
		log.Fatal(err)
	}
}

func NewProject(title, baseURL string, projType string, scrapeFields []ScrapeField, source string) {
	var emptyFAQs []FAQ = nil

	newKey := datastore.IncompleteKey("Project", nil)

	project := &Project{title, baseURL, projType, scrapeFields, emptyFAQs, false, source}

	_, err := client.Put(ctx, newKey, project)

	if err != nil {
		log.Println(err)
	}

}

func shuffleFaqs(faqs []FaqIndex) []FaqIndex {
	rand.Seed(time.Now().UnixNano())
	for i := len(faqs) - 1; i > 0; i-- { // Fisher–Yates shuffle
		j := rand.Intn(i + 1)
		faqs[i], faqs[j] = faqs[j], faqs[i]
	}
	return faqs
}
