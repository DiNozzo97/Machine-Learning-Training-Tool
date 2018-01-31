// Minimal Client/Server AJAX Communication using golang web-server and JQuery
// Visit: http://127.0.0.1:8080
package main

import (
	"html/template"
	"net/http"

	"google.golang.org/api/oauth2/v2"

	"log"
)

var httpClient = &http.Client{}

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
	tokenInfo, err := validateToken(tokenID)
	if err != nil {
		log.Fatal("tokenInfo: ", err)
	}

	println(tokenInfo.Email)
	println(tokenInfo.UserId)
	println(tokenInfo.AccessType)
	println(tokenID)

	println(tokenInfo)
	println("------------------------------------")

	//w.Header().Set("Content-type", "text/plain")
	//w.Write([]byte(data.Name))
	//println(tokenID)
	return
}

func validateToken(idToken string) (*oauth2.Tokeninfo, error) {
	oauth2Service, err := oauth2.New(httpClient)
	tokenInfoCall := oauth2Service.Tokeninfo()
	tokenInfoCall.IdToken(idToken)

	tokenInfo, err := tokenInfoCall.Do()
	if err != nil {
		return nil, err
	}
	return tokenInfo, nil

}

func main() {

	http.HandleFunc("/", defaultHandler)
	http.HandleFunc("/ajax", ajaxHandler)
	http.ListenAndServe(":8081", nil)
}
