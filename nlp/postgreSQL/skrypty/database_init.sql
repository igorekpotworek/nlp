COPY WIADOMOSCI FROM 'C:\items_wiadomosci.csv' DELIMITER ',' CSV HEADER;
COPY SPORT FROM 'C:\items_SPORT.csv' DELIMITER ',' CSV HEADER;


INSERT INTO ARTYKULY_SPORT (tytul, wstep, tekst) 
	select tytul, wstep, regexp_replace (tekst, 'Pozostało znaków: 4000 Zaloguj się Twój podpis: zmień swój podpis | wyloguj się ...lub dodaj opinię anonimowo REGULAMIN.*', '') from SPORT;



