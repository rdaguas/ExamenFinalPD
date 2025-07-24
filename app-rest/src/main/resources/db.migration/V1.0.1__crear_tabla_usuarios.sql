CREATE TABLE todos
(
    id        SERIAL PRIMARY KEY,
    completed BOOLEAN,
    title     VARCHAR(255),
    userId    INTEGER,
    userName  VARCHAR(255)
);
-- INSERT INTO public.usuarios (id, title, completed)
-- VALUES (1, 'delectus aut autem', false),
--        (2, 'quis ut nam facilis et officia qui', true),
--        (3, 'fugiat veniam minus', false);