COPY TMP_TECH FROM 'D:\items_tech.csv' DELIMITER ',' CSV HEADER;
COPY TMP_POLITICS FROM 'D:\items_politics.csv' DELIMITER ',' CSV HEADER;
COPY TMP_HEALTH FROM 'D:\items_health.csv' DELIMITER ',' CSV HEADER;


INSERT INTO articles (title, intro, text, category ) 
	select tytul, wstep, tekst, 'TECH' from TMP_TECH
	where url ~ '.*,wiadomosc\.html.*' and (wstep is not null and wstep!='') or (tekst is not null and tekst!='');

INSERT INTO articles (title, intro, text, category ) 
	select tytul, intro, tekst, 'POLITICS' from TMP_POLITICS
	where url ~ '.*,wiadomosc\.html.*' and (intro is not null and into!='') or (tekst is not null and tekst!='');
	
INSERT INTO articles (title, intro, text, category ) 
	select tytul, wstep, tekst, 'HEALTH' from TMP_HEALTH
	where ((wstep is not null and wstep!='') or (tekst is not null and tekst!=''));
	
	
UPDATE articles SET text=regexp_replace (text, ' Tagi:.*', '');
UPDATE articles SET text=regexp_replace (text, 'Pozostało znaków: 4000 Zaloguj się Twój podpis: zmień swój podpis | wyloguj się ...lub dodaj opinię anonimowo REGULAMIN.*', '');
UPDATE articles SET text=regexp_replace (text, '(fot\. .*)', '');

