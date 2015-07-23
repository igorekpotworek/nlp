#!/bin/bash

curl -XPUT http://localhost:9200/articles/?pretty=true -d '{
        "mappings": {
                "article" : {
                        "properties": {
                                "ID": { "type": "long" },
                                "TITLE": { "type": "string" },
                                "TEXT": { "type": "string" },
                                "INTRO": { "type": "string" },
                                "CATEGORY": { "type": "string", "index": "not_analyzed" }
                        },
                        "index_analyzer": "polish",
                        "search_analyzer": "polish"
                }
        }
}'

curl -XPUT 'localhost:9200/_river/articles/_meta' -d '{
    "type" : "jdbc",
    "jdbc" : {
        "url" : "jdbc:phoenix:localhost",
        "user" : "postgres",
        "password" : "postgres",
        "sql" : "select a.id as \"_id\", a.id as ID, a.title as TITLE, a.text as TEXT, a.intro as INTRO, a.category as CATEGORY from articles as a",
        "index" : "articles",
        "type" : "article",
		"schedule" : "0 0/30 0-23 ? * *"
    }
}'