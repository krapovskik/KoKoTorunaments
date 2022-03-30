create view players_in_individual_tournament as
SELECT au.first_name, au.last_name
FROM individual_matches_tournaments imt
         LEFT JOIN individual_matches im ON imt.individual_match_id = im.id
         LEFT JOIN app_users au ON im.app_user1_id = au.id AND im.app_user2_id = au.id;

create view players_in_team_tournament as
SELECT au.first_name, au.last_name
FROM team_matches_tournament tmt
         LEFT JOIN team_matches tm ON tmt.team_match_id = tm.id
         LEFT JOIN teams t ON tm.team1_id = t.id AND tm.team2_id = t.id
         LEFT JOIN user_teams ut ON t.id = ut.team_id
         LEFT JOIN app_users au ON ut.app_user_id = au.id;

create view teams_in_tournament as
SELECT t.name
FROM team_matches_tournament tmt
         LEFT JOIN team_matches tm ON tmt.team_match_id = tm.id
         LEFT JOIN teams t ON tm.team1_id = t.id AND tm.team2_id = t.id;

