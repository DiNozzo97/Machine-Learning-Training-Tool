// Sample datastore-quickstart fetches an entity from Google Cloud Datastore.
package main

import (
	"fmt"
	"log"
	"os"

	// Imports the Google Cloud Datastore client package.
	"cloud.google.com/go/datastore"
	"golang.org/x/net/context"
)

type Task struct {
	Description string
}

func main() {
	os.Setenv("GOOGLE_APPLICATION_CREDENTIALS", "/Users/timbo/Documents/Uni Work/finalYearProject/final-year-project-5b8758c566ba.json")


	ctx := context.Background()

	// Set your Google Cloud Platform project ID.
	projectID := "final-year-project-183322"

	// Creates a client.
	client, err := datastore.NewClient(ctx, projectID)
	if err != nil {
		log.Fatalf("Failed to create client: %v", err)
	}

	// Sets the kind for the new entity.
	kind := "Task"
	// Sets the name/ID for the new entity.
	name := "sampletask2"
	// Creates a Key instance.
	taskKey := datastore.NameKey(kind, name, nil)

	// Creates a Task instance.
	task := Task{
		Description: "Buy cheese",
	}

	// Saves the new entity.
	if _, err := client.Put(ctx, taskKey, &task); err != nil {
		log.Fatalf("Failed to save task: %v", err)
	}

	fmt.Printf("Saved %v: %v\n", taskKey, task.Description)

}