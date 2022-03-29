create table app_users
(
    id         bigserial primary key,
    first_name text not null,
    last_name  text not null,
    email      text not null,
    password   text not null,
    is_valid   boolean default false
);

create table activation_tokens
(
    token       text primary key,
    app_user_id bigint not null,
    constraint fk_activation_tokens_app_user_id foreign key (app_user_id) references app_users (id)
);

create table teams
(
    id       bigserial primary key,
    name     text not null,
    is_valid boolean default true
);

create table tournaments
(
    id                     bigserial primary key,
    name                   text    not null,
    category               text    not null,
    number_of_participants integer not null
);

create table teams_tournaments
(
    tournament_id bigint,
    constraint pk_teams_tournaments primary key (tournament_id),
    constraint fk_teams_tournaments_tournament_id foreign key (tournament_id) references tournaments (id)
);

create table individual_tournaments
(
    tournament_id bigint,
    constraint pk_individual_tournaments primary key (tournament_id),
    constraint fk_individual_tournaments_tournament_id foreign key (tournament_id) references tournaments (id)
);

create table matches
(
    id            bigserial primary key,
    winner        integer,
    tournament_id bigint not null,
    constraint fk_matches_tournament_id foreign key (tournament_id) references tournaments (id)
);

create table teams_matches
(
    match_id bigint,
    constraint pk_teams_matches primary key (match_id),
    constraint fk_teams_matches_match_id foreign key (match_id) references matches (id)
);

create table individual_matches
(
    match_id bigint,
    constraint pk_individual_matches primary key (match_id),
    constraint fk_individual_matches_match_id foreign key (match_id) references matches (id)
);
create table teams_players
(
    team_id    bigint,
    players_id bigint,
    constraint pk_team_has_users primary key (team_id, players_id),
    constraint fk_team_has_users_team_id foreign key (team_id) references teams (id),
    constraint fk_team_has_users_players_id foreign key (players_id) references app_users (id)
);

create table tournaments_teams
(
    tournament_id bigint,
    teams_id      bigint,
    constraint pk_tournament_has_teams primary key (tournament_id, teams_id),
    constraint fk_tournament_has_teams_tournament_id foreign key (tournament_id) references tournaments (id),
    constraint fk_tournament_has_teams_teams_id foreign key (teams_id) references teams (id)
);

create table tournaments_players
(
    tournament_id bigint,
    players_id    bigint,
    constraint pk_tournament_has_users primary key (tournament_id, players_id),
    constraint fk_tournament_has_users_tournament_id foreign key (tournament_id) references tournaments (id),
    constraint fk_tournament_has_users_players_id foreign key (players_id) references app_users (id)
);
