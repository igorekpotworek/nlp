COPY TMP FROM 'C:\items_wiadomosci.csv' DELIMITER ',' CSV HEADER;

INSERT INTO ARTYKULY_WIADOMOSCI (tytul, wstep, tekst) 
	select tytul, intro, regexp_replace (tekst, 'Pozostało znaków: 4000 Zaloguj się Twój podpis: zmień swój podpis | wyloguj się ...lub dodaj opinię anonimowo REGULAMIN.*', '') from TMP;



