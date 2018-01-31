// Minimal Client/Server AJAX Communication using golang web-server and JQuery
// Visit: http://127.0.0.1:8080
package main

import (
	"html/template"
	"net/http"
	"fmt"
	"log"
	"encoding/json"
)

type tokenResponse struct {
	iss string
	sub string
	azp string
	aud string
	iat string
	exp string
	email string



}

// Default Request Handler
func defaultHandler(w http.ResponseWriter, r *http.Request) {


	fp := "index.html"
	tmpl, err := template.ParseFiles(fp)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if err := tmpl.Execute(w, nil); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

// AJAX Request Handler
func ajaxHandler(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	tokenID := r.PostForm.Get("tokenID")

	url := fmt.Sprintf("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=%s", tokenID)


	req, err := http.NewRequest("GET", url, nil)

	if err != nil {
		log.Fatal("NewRequest: ", err)
		return
	}
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		log.Fatal("Do: ", err)
		return
	}
	defer resp.Body.Close()

	var record tokenResponse

	// Use json.Decode for reading streams of JSON data
	if err := json.NewDecoder(resp.Body).Decode(&record); err != nil {
		log.Println(err)
	}

	w.Header().Set("Content-type", "text/plain")
	w.Write([]byte(data.Name))
	println(tokenID)
	return
}

func main() {
	http.HandleFunc("/", defaultHandler)
	http.HandleFunc("/ajax", ajaxHandler)
	http.ListenAndServe(":8081", nil)
}