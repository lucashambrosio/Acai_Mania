--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: pedidos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pedidos (
    numero_pedido integer NOT NULL,
    nome_cliente character varying(100),
    tamanho_acai character varying(10),
    acrescimos character varying(200),
    valor_final numeric(10,2)
);


ALTER TABLE public.pedidos OWNER TO postgres;

--
-- Name: pedidos_numero_pedido_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pedidos_numero_pedido_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pedidos_numero_pedido_seq OWNER TO postgres;

--
-- Name: pedidos_numero_pedido_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pedidos_numero_pedido_seq OWNED BY public.pedidos.numero_pedido;


--
-- Name: pedidos numero_pedido; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedidos ALTER COLUMN numero_pedido SET DEFAULT nextval('public.pedidos_numero_pedido_seq'::regclass);


--
-- Data for Name: pedidos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pedidos (numero_pedido, nome_cliente, tamanho_acai, acrescimos, valor_final) FROM stdin;
1	Luiz	Média	Nutella	\N
2	temp	temp	temp	\N
3	temp	temp	temp	\N
4	temp	temp	temp	\N
5	temp	temp	temp	\N
6	temp	temp	temp	\N
7	Lucas	Grande	Valor total dos acréscimos: R$ 4.0	12.00
8	Lucas	Média	Valor total dos acréscimos: R$ 1.0	8.00
9	Lucas	Média	Valor total dos acréscimos: R$ 0.0	0.00
10	Matheus	Média	Valor total dos acréscimos: R$ 3.5	10.50
11	Luan	Grande	Valor total dos acréscimos: R$ 3.0	11.00
12	Laura	Grande	Valor total dos acréscimos: R$ 0.0	0.00
13	Maria Clara	Média	Nenhum acréscimo	0.00
14	Marla	Pequena	Nenhum acréscimo	0.00
\.


--
-- Name: pedidos_numero_pedido_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pedidos_numero_pedido_seq', 14, true);


--
-- Name: pedidos pedidos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedidos
    ADD CONSTRAINT pedidos_pkey PRIMARY KEY (numero_pedido);


--
-- PostgreSQL database dump complete
--

