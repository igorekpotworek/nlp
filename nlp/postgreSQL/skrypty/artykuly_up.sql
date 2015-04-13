CREATE TABLE TMP_TECH (
_cached_page_id VARCHAR,
_template integer,
_type VARCHAR,
tekst VARCHAR,
tytul VARCHAR,
url VARCHAR,
wstep VARCHAR
);

CREATE TABLE TMP_SPORT (
_cached_page_id VARCHAR,
_template integer,
_type VARCHAR,
tekst VARCHAR,
tytul VARCHAR,
url VARCHAR,
wstep VARCHAR
);

CREATE TABLE TMP_POLITICS (
_cached_page_id VARCHAR,
_template integer,
_type VARCHAR,
intro VARCHAR,
tekst VARCHAR,
tytul VARCHAR,
url VARCHAR
);

CREATE TABLE TMP_HEALTH(
_cached_page_id VARCHAR,
_template integer,
_type VARCHAR,
tekst VARCHAR,
tytul VARCHAR,
url VARCHAR,
wstep VARCHAR
);


CREATE TABLE articles (
id SERIAL,
title varchar,
intro varchar,
text varchar,
category varchar);

create table topics (
id integer);

create table topics_words (
topicId integer,
word varchar,
weight double precision);

CREATE INDEX articles_category_index ON articles (category);


