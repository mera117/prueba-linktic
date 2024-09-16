CREATE TABLE IF NOT EXISTS con_rango_plazo
(
    id SERIAL PRIMARY KEY,
    minimo number  null,
    estado boolean null,
    maximo number null,
    description_plazo varchar(255) null
);