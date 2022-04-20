insert into app_users (first_name, last_name, email, password)
values ('Kosta', 'Krapovski', 'kosta.krapovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Kosta', 'Fortumanov', 'kosta.fortumanov@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Dragan', 'Bozhinoski', 'dragan.bozhinoski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Jovan', 'Kitanov', 'jovan.kitanov@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Gorjan', 'Micevski', 'gorjan.micevski',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Stojance', 'Krstevski', 'stojance.krstevski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Emil', 'Stankov', 'emil.stanok@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Andrej', 'Skendereski', 'andrej.skenderski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('David', 'Trajkovski', 'david.trajkovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Gjorgji', 'Dimeski', 'gjorgji.dimeski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Dimitar', 'Mitrovski', 'dimitar.mitrovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Oliver', 'Aleksovski', 'oliver.aleksovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Stefan', 'Dimitrovski', 'stefan.dimitrovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK'),
       ('Nikola', 'Lazarevski', 'nikola.lazarevski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK');

insert into app_users (first_name, last_name, email, password, is_active, app_user_role)
values ('Admin', 'Admin', 'admin',
        '$2a$10$pvnUPSWNzseMf4v1MqE7F.F0dOyFSvpXTR7XdJGZff.mLbKOcS4Zm', true, 'ADMIN');

insert into tournaments (name, category, location, description, number_of_participants, type, timeline, organizer,start_date)
values ('Kosharka', 'Kosharka','Tetovo Goce Sala', 'Timski turnir itn...',  4, 'TEAM', 'ONGOING', 1, '2022-04-20 03:00:00.000000'),
       ('Ping pong', 'Ping Pong','Skopje Boris Sala', 'Timski turnir itn...', 4, 'INDIVIDUAL', 'COMING_SOON', 1, '2022-04-22 03:00:00.000000'),
       ('Ping pong1', 'Ping Pong1','Gostivar Goce Sala', 'Timski turnir itn...', 4, 'INDIVIDUAL', 'FINISHED', 1, '2022-04-10 03:00:00.000000'),
       ('Kosharka', 'Kosharka', 'Kumanovo Goce Sala', 'Timski turnir itn...', 4, 'TEAM', 'COMING_SOON', 1, '2022-04-22 03:00:00.000000');

insert into teams (name)
values ('KK Tomas'),
       ('KK Feniks'),
       ('KK MZT'),
       ('KK Rabotnichki');

insert into app_user_teams (team_id, app_user_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (3, 8),
       (3, 9),
       (3, 10),
       (3, 11),
       (4, 12),
       (4, 13),
       (4, 14);

insert into team_matches (team1_id, team2_id)
values (1, 3),
       (2, 4);

insert into individual_matches (app_user1_id, app_user2_id)
values (1, 2),
       (3, 4);

insert into team_matches_tournaments (tournament_id, team_match_id)
values (1, 1),
       (1, 2);


insert into individual_matches_tournaments (tournament_id, individual_match_id)
values (3, 1),
       (3, 2);

insert into individual_tournaments (tournament_id, app_user_id)
values (2, 2),
       (2, 3),
       (2, 4);

insert into team_tournaments (tournament_id, team_id)
values (4, 2),
       (4, 3),
       (4, 4);


insert into organizer_requests (title, description, app_user_id)
values ('Tournaments title', 'Tournaments description', 1),
       ('Tournaments title', 'Tournaments description', 2),
       ('Tournaments title', 'Tournaments description', 3),
       ('Tournaments title', 'Tournaments description', 4),
       ('Tournaments title', 'Tournaments description', 5),
       ('Tournaments title', 'Tournaments description', 6),
       ('Tournaments title', 'Tournaments description', 7),
       ('Tournaments title', 'Tournaments description', 8),
       ('Tournaments title', 'Tournaments description', 9),
       ('Tournaments title', 'Tournaments description', 10),
       ('Tournaments title', 'Tournaments description', 11),
       ('Tournaments title', 'Tournaments description', 12),
       ('Tournaments title', 'Tournaments description', 13),
       ('Tournaments title', 'Tournaments description', 14);
