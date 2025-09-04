INSERT INTO public.authorities VALUES (1, 1, 'ROLE_USER');
INSERT INTO public.authorities VALUES (2, 2, 'ROLE_USER');

INSERT INTO public.events VALUES ('2025-09-15', 1, 'Spring Boot basics.', 'Hyderabad', 'Spring Boot Workshop');
INSERT INTO public.events VALUES ('2025-09-15', 2, 'Kafka basics.', 'Bangalore', 'Kafka Workshop');

INSERT INTO public.registrations VALUES (1, 1, '2025-09-04 21:52:30.911563', 1);
INSERT INTO public.registrations VALUES (2, 2, '2025-09-04 21:53:43.191434', 2);

INSERT INTO public.users VALUES (true, 1, '$2a$10$0seTNRhaACB8YQeSaLf.B.LdoQtiPEWhTpXqYKdj3RCggOMkmOOgi', 'Alice');
INSERT INTO public.users VALUES (true, 2, '$2a$10$mkb4liXQkCToxTm5vCbzlOFdqLFva4cv44Jds7x1wNRdTaOPa9cam', 'Bob');
