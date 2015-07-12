CREATE TABLE articles (
id SERIAL PRIMARY KEY,
title varchar,
intro varchar,
text varchar,
category varchar);

create table topics (
topicId integer,
word varchar,
weight double precision);

create table users (
id SERIAL PRIMARY KEY,
firstname varchar,
lastname varchar
);

create table topics_articles (
articleId integer,
topicId integer,
weight double precision
);

create table rates (
userId integer,
articleId integer,
rating double precision
);

CREATE INDEX articles_category_index ON articles (category);

