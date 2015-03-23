COPY TMP_TECH FROM 'C:\items_tech.csv' DELIMITER ',' CSV HEADER;



INSERT INTO articles (title, intro, text, category ) 
	select tytul, wstep, regexp_replace(regexp_replace (tekst, 'Pozostało znaków: 4000 Zaloguj się Twój podpis: zmień swój podpis | wyloguj się ...lub dodaj opinię anonimowo REGULAMIN.*', ''), ' Tagi:.*', ''), 'tech' from TMP_TECH
	where url ~ '.*,wiadomosc\.html.*' and (wstep is not null or tekst is not null);






