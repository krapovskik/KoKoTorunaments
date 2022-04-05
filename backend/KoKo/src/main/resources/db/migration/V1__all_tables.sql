create table app_users
(
    id            bigserial primary key,
    first_name    text not null,
    last_name     text not null,
    email         text not null,
    password      text not null,
    is_active     boolean default false,
    app_user_role text    default 'PLAYER'
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
    is_active boolean default true
);

create table tournaments
(
    id                     bigserial primary key,
    name                   text    not null,
    category               text    not null,
    number_of_participants integer not null,
    type                   text    not null
);

create table team_matches
(
    id       bigserial primary key,
    winner   integer,
    team1_id bigint not null,
    team2_id bigint not null,
    constraint fk_team_matches_team1_id foreign key (team1_id) references teams (id),
    constraint fk_team_matches_team2_id foreign key (team2_id) references teams (id)
);

create table individual_matches
(
    id           bigserial primary key,
    winner       integer,
    app_user1_id bigint not null,
    app_user2_id bigint not null,
    constraint fk_individual_matches_app_user1_id foreign key (app_user1_id) references app_users (id),
    constraint fk_individual_matches_app_user2_id foreign key (app_user2_id) references app_users (id)
);

create table team_matches_tournaments
(
    id            bigserial primary key,
    tournament_id bigint,
    team_match_id bigint,
    constraint fk_team_matches_tournament_tournament_id foreign key (tournament_id) references tournaments (id),
    constraint fk_team_matches_tournament_team_match_id foreign key (team_match_id) references team_matches (id)
);

create table individual_matches_tournaments
(
    id                  bigserial primary key,
    tournament_id       bigint,
    individual_match_id bigint,
    constraint fk_individual_matches_tournament_tournament_id foreign key (tournament_id) references tournaments (id),
    constraint fk_individual_matches_tournament_individual_match_id foreign key (individual_match_id) references individual_matches (id)
);

create table app_user_teams
(
    id          bigserial primary key,
    team_id     bigint not null,
    app_user_id bigint not null,
    constraint fk_app_user_teams_team_id foreign key (team_id) references teams (id),
    constraint fk_app_user_teams_app_user_id foreign key (app_user_id) references app_users (id)
);

create table organizer_requests
(
    id bigserial primary key,
    title text not null,
    description text not null,
    app_user_id bigint not null,
    constraint fk_organizer_requests_app_user_id foreign key (app_user_id) references app_users (id)
);
