CREATE TABLE articles (
id INTEGER PRIMARY KEY,
title VARCHAR,
intro VARCHAR,
text VARCHAR,
category VARCHAR
);

create table topics (
topicId INTEGER NOT NULL,
word VARCHAR NOT NULL,
weight DOUBLE,
CONSTRAINT pk PRIMARY KEY (topicId , word)

);

create table topics_articles (
articleId INTEGER NOT NULL, 
topicId INTEGER NOT NULL,
weight DOUBLE,
CONSTRAINT pk PRIMARY KEY (articleId , topicId)
);

create table users (
id INTEGER PRIMARY KEY,
firstname VARCHAR,
lastname VARCHAR
);

create table rates (
userId INTEGER NOT NULL,
articleId INTEGER NOT NULL,
rating DOUBLE,
CONSTRAINT pk PRIMARY KEY (userId , articleId)
);

CREATE SEQUENCE articles_seq START WITH 1000000 ;
CREATE SEQUENCE users_seq;