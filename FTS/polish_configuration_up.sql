CREATE TEXT SEARCH CONFIGURATION public.polish ( COPY = pg_catalog.english );
CREATE TEXT SEARCH DICTIONARY polish_ispell (
        TEMPLATE = ispell,
        DictFile = polish, 
        AffFile = polish, 
        StopWords = polish 
);

CREATE TEXT SEARCH DICTIONARY polish_synonym (
        TEMPLATE = synonym,
        SYNONYMS = polish 
);

CREATE TEXT SEARCH DICTIONARY polish_thesaurus (
        TEMPLATE = thesaurus,
        DictFile = polish,
        Dictionary = polish_ispell
);

ALTER TEXT SEARCH CONFIGURATION polish
ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, word, hword, hword_part
WITH polish_thesaurus, polish_synonym, polish_ispell, simple;