create table games (id bigint identity, name varchar(50) unique, type varchar(50));

create table prices (game_type varchar(50) not null, amount_in_cents bigint, min_days integer, loyalty_points integer, primary key (game_type));

create table users (id bigint identity, name varchar(100) unique);

create table rentals (id bigint identity, rental_days integer, rental_date date not null, returned_date date, returned boolean default false, game_id bigint not null, user_id bigint not null, loyalty_points integer, foreign key (game_id) references games(id), foreign key (user_id) references users(id));
create index index_user_id on rentals(user_id);
create index index_game_id on rentals(game_id);