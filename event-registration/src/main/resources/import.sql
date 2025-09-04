INSERT INTO public.users VALUES (true, 1, '$2a$10$sNkKH1w1bs2p5Ed6iG/jc.hGMdhlRE.JgsW8pmPql24AdGTLG4nIG', 'Alice');
INSERT INTO public.users VALUES (true, 2, '$2a$10$0seTNRhaACB8YQeSaLf.B.LdoQtiPEWhTpXqYKdj3RCggOMkmOOgi', 'Bob');
INSERT INTO public.users VALUES (true, 3, '$2a$10$mkb4liXQkCToxTm5vCbzlOFdqLFva4cv44Jds7x1wNRdTaOPa9cam', 'Admin');

INSERT INTO public.authorities VALUES (1, 1, 'ROLE_USER');
INSERT INTO public.authorities VALUES (2, 2, 'ROLE_USER');
INSERT INTO public.authorities VALUES (3, 3, 'ROLE_ADMIN');

INSERT INTO public.events VALUES ('2025-09-15', 1, 'Spring Boot basics.', 'Hyderabad', 'Spring Boot Workshop');
INSERT INTO public.events VALUES ('2025-09-15', 2, 'Kafka basics.', 'Bangalore', 'Kafka Workshop');

INSERT INTO public.registrations VALUES (1, 1, '2025-09-04 21:52:30.911563', 1);
INSERT INTO public.registrations VALUES (2, 2, '2025-09-04 21:53:43.191434', 2);

-- reset sequences so Spring Boot’s JPA won’t try to reuse old IDs
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('authorities_id_seq', (SELECT MAX(id) FROM authorities));
SELECT setval('events_id_seq', (SELECT MAX(id) FROM events));
SELECT setval('registrations_id_seq', (SELECT MAX(id) FROM registrations));