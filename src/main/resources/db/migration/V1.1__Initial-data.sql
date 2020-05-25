insert into prices (game_type, amount_in_cents, min_days, loyalty_points) values ('NEW_RELEASE', 400, 0, 2);
insert into prices (game_type, amount_in_cents, min_days, loyalty_points) values ('STANDARD', 300, 3, 1);
insert into prices (game_type, amount_in_cents, min_days, loyalty_points) values ('CLASSIC', 300, 5, 1);

insert into games (name, type) values ('No Man''s Sky', 'NEW_RELEASE');
insert into games (name, type) values ('Resident Evil 6', 'STANDARD');
insert into games (name, type) values ('Fall Out 4', 'STANDARD');
insert into games (name, type) values ('Fall Out 3', 'CLASSIC');

insert into users (name) values ('Juan');
insert into users (name) values ('Jose');
insert into users (name) values ('Pedro');