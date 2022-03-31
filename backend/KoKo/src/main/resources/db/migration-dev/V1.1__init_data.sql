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

insert into tournaments (name, category, number_of_participants, type)
values
       ('Kosharka', 'Kosharka', 4,'TEAM'),
       ('Ping pong', 'Ping Pong', 4,'INDIVIDUAL');

insert into teams (name)
values
       ('KK Tomas'),
       ('KK Feniks'),
       ('KK MZT'),
       ('KK Rabotnichki');

insert into app_user_teams (team_id, app_user_id)
values
       (1,1),
       (1,2),
       (1,3),
       (1,4),
       (2,5),
       (2,6),
       (2,7),
       (3,8),
       (3,9),
       (3,10),
       (3,11),
       (4,12),
       (4,13),
       (4,14);

insert into team_matches (team1_id, team2_id)
values
       (1,3),
       (2,4);

insert into individual_matches (app_user1_id, app_user2_id)
values
       (1,2),
       (3,4);

insert into team_matches_tournaments (tournament_id, team_match_id)
values
       (1,1),
       (1,2);

insert into individual_matches_tournaments (tournament_id, individual_match_id)
values
       (2,1),
       (2,2);

