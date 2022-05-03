create view players_in_individual_tournament as
SELECT distinct au.id as app_user_id, t.id as tournament_id, au.first_name as first_name, au.last_name as last_name
FROM individual_matches_tournaments imt
         JOIN tournaments t on t.id = imt.tournament_id
         JOIN individual_matches im ON imt.individual_match_id = im.id
         JOIN app_users au on im.app_user1_id = au.id or im.app_user2_id = au.id;


create view players_in_team_tournament as
SELECT distinct au.id         as app_user_id,
                t2.id         as team_id,
                t.id          as tournament_id,
                au.first_name as first_name,
                au.last_name  as last_name
FROM team_matches_tournaments tmt
         JOIN tournaments t on t.id = tmt.tournament_id
         JOIN team_matches tm ON tmt.team_match_id = tm.id
         JOIN teams t2 on t2.id = tm.team1_id or t2.id = tm.team2_id
         JOIN app_user_teams aut on t2.id = aut.team_id
         JOIN app_users au on aut.app_user_id = au.id;


create view teams_in_tournament as
SELECT distinct t2.id as team_id, t.id as tournament_id, t2.name as name
FROM team_matches_tournaments tmt
         JOIN tournaments t on t.id = tmt.tournament_id
         JOIN team_matches tm on tm.id = tmt.team_match_id
         JOIN teams t2 on tm.team1_id = t2.id or tm.team2_id = t2.id;
