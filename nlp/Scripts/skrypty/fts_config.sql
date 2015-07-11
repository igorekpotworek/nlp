ALTER TABLE articles ADD COLUMN vector TSVECTOR;

UPDATE articles SET vector=to_tsvector('public.polish', title || ' ' || intro || ' ' || text);

CREATE INDEX articles_gin ON articles USING GIN (vector);

CREATE TRIGGER TS_ARTICLES BEFORE INSERT OR UPDATE ON articles 
FOR EACH ROW EXECUTE PROCEDURE tsvector_update_trigger(vector,'public.polish',title, intro, text);

select * from articles where vector @@ to_tsquery('public.polish', 'komorowski') limit 1;
select count(*) from articles where vector @@ plainto_tsquery('public.polish', 'Pozostało znaków: 4000 Zaloguj się Twój podpis');