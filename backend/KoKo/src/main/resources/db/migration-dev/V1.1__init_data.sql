insert into app_users (first_name, last_name, email, password, is_active)
values ('Kosta', 'Krapovski', 'kosta.krapovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Kosta', 'Fortumanov', 'kosta.fortumanov@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Dragan', 'Bozhinoski', 'dragan.bozhinoski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Jovan', 'Kitanov', 'jovan.kitanov@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Gorjan', 'Micevski', 'gorjan.micevski',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Stojance', 'Krstevski', 'stojance.krstevski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Emil', 'Stankov', 'emil.stanok@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Andrej', 'Skendereski', 'andrej.skenderski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('David', 'Trajkovski', 'david.trajkovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Gjorgji', 'Dimeski', 'gjorgji.dimeski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Dimitar', 'Mitrovski', 'dimitar.mitrovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Oliver', 'Aleksovski', 'oliver.aleksovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Stefan', 'Dimitrovski', 'stefan.dimitrovski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true),
       ('Nikola', 'Lazarevski', 'nikola.lazarevski@sorsix.com',
        '$2a$10$xDmfApNkwR4cbXXR2VzDNO9sjQsQmbVMPfWk8CahJJO4.PTu8hQqK', true);

-- password: kosta123

insert into app_users (first_name, last_name, email, password, is_active, app_user_role)
values ('Admin', 'Admin', 'admin',
        '$2a$10$pvnUPSWNzseMf4v1MqE7F.F0dOyFSvpXTR7XdJGZff.mLbKOcS4Zm', true, 'ADMIN');

-- password: admin

insert into organizer_requests (title, description, app_user_id)
values ('Tournaments title', 'Tournaments description', 3),
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
