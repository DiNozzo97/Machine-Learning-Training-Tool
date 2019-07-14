package main

import (
	"encoding/csv"
	"html/template"
	"log"
	"net/http"
	"strconv"

	"./database"
)

var user = "user@user.com"

func trainingHandler(w http.ResponseWriter, r *http.Request) {
	projectID, ok := r.URL.Query()["project"]

	if !ok || len(projectID) < 1 {
		log.Println("Url Param 'project' is missing")

		return
	}

	key, project := database.FetchProject(projectID[0])

	faq, faqID := database.FetchFAQToParaphrase(project, user)

	if r.Method != "GET" {
		r.ParseForm()
		largestField, convertErr := strconv.ParseInt(r.Form["largestFieldId"][0], 10, 64)
		if convertErr != nil {
			log.Println("Error: Unable to Parse largestFieldId string as int.")
		}

		for i := 0; i <= int(largestField); i++ {
			paraphraseText := r.Form["paraphraseText-"+strconv.Itoa(i)][0]
			faqID, convertErr := strconv.ParseInt(r.Form["faqID-"+strconv.Itoa(i)][0], 10, 64)
			if convertErr != nil {
				log.Println("Error: Unable to Parse faqID string as int.")
			}

			if paraphraseText != "" {
				database.StoreParaphrase(paraphraseText, user, project, int(faqID), key)
			}

		}

	}

	type trainData struct {
		Error               string
		FAQ                 database.FAQ
		FAQID               int
		ProjectList         []database.ProjectListing
		CurrentProjectTitle string
	}

	var data trainData
	switch faqID {
	case -1:
		data = trainData{"No FAQs were found, please contact the administrator.", faq, faqID, database.ListProjects(), project.Title}

	case -2:
		data = trainData{"You have already paraphrased all of the FAQs we found in this project!", faq, faqID, database.ListProjects(), project.Title}
	default:
		data = trainData{"", faq, faqID, database.ListProjects(), project.Title}
	}

	t, _ := template.ParseFiles("train.gtpl")
	t.Execute(w, data)
}

func add(a, b int) int {
	return a + b
}

func resultHandler(w http.ResponseWriter, r *http.Request) {
	projectID, ok := r.URL.Query()["project"]

	if !ok || len(projectID) < 1 {
		log.Println("Url Param 'project' is missing")

		return
	}

	_, project := database.FetchProject(projectID[0])

	var data = struct {
		FAQs                []database.FAQ
		ProjectList         []database.ProjectListing
		CurrentProjectTitle string
		CurrentProjectID    string
	}{project.FAQs, database.ListProjects(), project.Title, projectID[0]}

	funcMap := template.FuncMap{"add": add}
	t, _ := template.New("result.gtpl").Funcs(funcMap).ParseFiles("result.gtpl")

	t.Execute(w, data)
}

func newProjectHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {

		var data = struct {
			ProjectList         []database.ProjectListing
			CurrentProjectTitle string
		}{database.ListProjects(), "<Select Project>"}

		t, _ := template.ParseFiles("newProject.gtpl")
		t.Execute(w, data)
	} else {
		r.ParseForm()
		title := r.Form["projectTitle"][0]
		url := r.Form["projectURL"][0]
		projType := r.Form["projectType"][0]
		instanceSelector := r.Form["selectorPath-faq_instance_selector"][0]

		var scrapeFields []database.ScrapeField
		var source string = ""

		if projType == "single-level-menu" || projType == "two-level-menu" {
			scrapeFields = append(scrapeFields, database.ScrapeField{"faq_instance_link_0", r.Form["selectorType-faq_instance_link_0"][0], r.Form["selectorPath-faq_instance_link_0"][0]})
			if projType == "two-level-menu" {
				scrapeFields = append(scrapeFields, database.ScrapeField{"faq_instance_link_1", r.Form["selectorType-faq_instance_link_1"][0], r.Form["selectorPath-faq_instance_link_1"][0]})
			}
		}

		if len(r.Form["paginationCheckbox"]) > 0 {
			scrapeFields = append(scrapeFields, database.ScrapeField{"pagination_link", r.Form["selectorType-pagination_link"][0], r.Form["selectorPath-pagination_link"][0]})
		}

		if instanceSelector != "" {
			scrapeFields = append(scrapeFields, database.ScrapeField{"faq_instance_selector", r.Form["selectorType-faq_instance_selector"][0], instanceSelector})
		}

		if len(r.Form["useSourceCheckbox"]) > 0 {
			source = r.Form["pageSource"][0]
		}

		scrapeFields = append(scrapeFields, database.ScrapeField{"question", r.Form["selectorType-question"][0], r.Form["selectorPath-question"][0]})
		scrapeFields = append(scrapeFields, database.ScrapeField{"answer", r.Form["selectorType-answer"][0], r.Form["selectorPath-answer"][0]})

		database.NewProject(title, url, projType, scrapeFields, source)

		var data = struct {
			ProjectList         []database.ProjectListing
			CurrentProjectTitle string
		}{database.ListProjects(), "<Select Project>"}

		t, _ := template.ParseFiles("newProjectSuccess.gtpl")
		t.Execute(w, data)

	}
}

func mainMenuHandler(w http.ResponseWriter, r *http.Request) {
	var data = struct {
		ProjectList         []database.ProjectListing
		CurrentProjectTitle string
	}{database.ListProjects(), "<Select Project>"}

	t, _ := template.ParseFiles("mainMenu.gtpl")
	t.Execute(w, data)
}

func csvHandler(w http.ResponseWriter, r *http.Request) {
	projectID, ok := r.URL.Query()["project"]

	if !ok || len(projectID) < 1 {
		log.Println("Url Param 'project' is missing")

		return
	}

	_, project := database.FetchProject(projectID[0])

	w.Header().Set("Content-Type", "text/csv")
	w.Header().Set("Content-Disposition", "attachment;filename="+project.Title+".csv")
	wr := csv.NewWriter(w)
	err := wr.Write([]string{"Question", "Paraphrase", "User", "DateTime", "Answer", "Source"})
	if err != nil {
		http.Error(w, "Error sending csv: "+err.Error(), http.StatusInternalServerError)
		return
	}

	for _, faq := range project.FAQs {
		if len(faq.Paraphrases) == 0 {
			record := []string{faq.Question, "", "", "", faq.Answer, faq.PageURL}
			err := wr.Write(record)
			if err != nil {
				http.Error(w, "Error sending csv: "+err.Error(), http.StatusInternalServerError)
				return
			}
		} else {
			for _, paraphrase := range faq.Paraphrases {
				record := []string{faq.Question, paraphrase.ParaphraseText, paraphrase.User, paraphrase.Timestamp, faq.Answer, faq.PageURL}
				err := wr.Write(record)
				if err != nil {
					http.Error(w, "Error sending csv: "+err.Error(), http.StatusInternalServerError)
					return

				}
			}

		}
	}

	wr.Flush()
}

func main() {
	fs := http.FileServer(http.Dir("static"))
	http.Handle("/static/", http.StripPrefix("/static/", fs))
	http.HandleFunc("/", mainMenuHandler) // setting router rule
	http.HandleFunc("/newProject", newProjectHandler)
	http.HandleFunc("/train", trainingHandler)
	http.HandleFunc("/result", resultHandler)
	http.HandleFunc("/generateCSV", csvHandler)

	err := http.ListenAndServe(":9090", nil) // setting listening port
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}
