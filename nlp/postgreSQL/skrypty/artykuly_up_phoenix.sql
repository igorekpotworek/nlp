CREATE TABLE articles (
id INTEGER PRIMARY KEY,
title VARCHAR,
intro VARCHAR,
text VARCHAR,
category VARCHAR
);

create table topics (
topicId INTEGER,
word VARCHAR,
weight DOUBLE,
CONSTRAINT pk PRIMARY KEY (topicId , word)

);

create table topics_articles (
articleId INTEGER,
topicId INTEGER,
weight DOUBLE,
CONSTRAINT pk PRIMARY KEY (articleId , topicId)
);

create table users (
id SERIAL PRIMARY KEY,
firstname VARCHAR,
lastname VARCHAR
);

create table rates (
userId INTEGER,
articleId INTEGER,
rating DOUBLE,
CONSTRAINT pk PRIMARY KEY (userId , articleId)
);

CREATE SEQUENCE articles_seq START WITH 1000000 ;
CREATE SEQUENCE users_seq;