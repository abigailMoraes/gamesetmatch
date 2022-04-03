/*Create table statement for user_info table, dept not included, added is_admin boolean and changed primary key to include both userID*/
CREATE TABLE User(userID int NOT NULL AUTO_INCREMENT, firebase_id varchar(100), name varchar(80), email varchar(319), is_admin int, PRIMARY KEY(userID));

/*Populate user_info table with sample data*/
INSERT INTO User(firebase_id, name, email, is_admin) values ('hcarloni','Heike Carloni', 'hcarloni@gmail.com', 1);
INSERT INTO User(firebase_id, name, email, is_admin) values ('mbaron', 'Michael Baron','mbaron@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('msanders', 'Michelle Sanders', 'msanders@gmail.com', 1);
INSERT INTO User(firebase_id, name, email, is_admin) values ('efiaro', 'Ester Fiaro', 'efiaro@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('pghosh', 'Prabhat Ghosh', 'pghosh@gmail.com',0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('amorales', 'Alberto Morales','amorales@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('echu', 'Eileen Chu', 'echu@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personA', 'Person A', 'pa@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personB', 'Person B', 'pb@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personC', 'Person C', 'pc@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personD', 'Person D', 'pd@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personE', 'Person E', 'pe@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personF', 'Person F', 'pf@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personA', 'Person A', 'pa@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personB', 'Person B', 'pb@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personC', 'Person C', 'pc@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personD', 'Person D', 'pd@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personE', 'Person E', 'pe@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personF', 'Person F', 'pf@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personM', 'Person M', 'pm@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personN', 'Person N', 'pn@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personO', 'Person O', 'po@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personP', 'Person P', 'pp@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personQ', 'Person Q', 'pq@gmail.com', 0);
INSERT INTO User(firebase_id, name, email, is_admin) values ('personR', 'Person R', 'pr@gmail.com', 0);

/*Create table statement for Tournament*/
/*legend*/
/* start_date = start_date of tournament */
/* close_registration_date = date on which registration for tournament will close*/
/* location = location designated for all rounds/matches in the tournament */
/* max_participants = the maximum number of participants allowed to register (to allow admin to  account for capacity, budget, time etc.) */
/* min_participants = the minimum number of participants allowed to register (to allow admin to specify the minimum amount of participants, below which no tournament can be held) */
/* end_date = The end date of the tournament - date of the last match?? */

/*ENUM values - note mysql convention - The index value of the empty string error value is 0. This means that you can use the following SELECT statement to find rows into which invalid ENUM values were assigned so 0 is reserved for ''*/
/* series = an integer that refers to one of four series types 1 - 'Best of 1', 2 - 'Best of 3', 3 - 'Best of 5' and 4 - 'Best of 7' */
/* format = an integer that refers to one of three format types 1 - 'Single-elimination', 2 - 'Double-Elimination', 3 - 'Round-Robin' */
/* match_by = an integer that refers to one of two matching types 1 - 'Randomly' , 2 - 'By skill' */
/*admin_hosts_tournament = int referring to the adminID of the admin that last edited the tournament */
/*status = integer referring to the status of the tournament 0-OpenForRegistration, 1-ClosedRegistration, 2-ScheduleReadyForReview, 3-Ongoing, 4-FinalRound, 5-TournamentOver */


CREATE TABLE Tournament(tournamentID int NOT NULL AUTO_INCREMENT, name varchar(128),  description varchar(150), start_date  DATE,  close_registration_date DATE, location varchar(60), max_participants int, min_participants int,  end_date DATE, prize varchar(60), format int, series int, match_by int, match_duration int, admin_hosts_tournament int, status int, current_round int, PRIMARY KEY(tournamentID),
                       FOREIGN KEY (admin_hosts_tournament) REFERENCES User(userID));

--INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status) values('Mariokart Madness', 'Come join us for some krazy karting! (Individual)', '2022-02-20', '2022-02-19', 'West Atrium room 203', 32, 4, '250$ Steam Gift Card', 1, 1, 1, 30, 1, -1, 0 );
--INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Mariokart Madness', 'Come join us for some krazy karting! (Individual)', '2022-02-20', '2022-02-19', 'West Atrium room 203', 32, 4, '250$ Steam Gift Card', 1, 1, 1, 30, 1, -1, 0 );
--INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Rock, Paper, Scissors', 'Luck or skill? Time to find out!', '2022-03-29', '2022-03-25', 'Room 100', 64, 4, '250$ Steam Gift Card', 2, 3, 1, 70, 1, -1, 0 );
--INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Chess Tournament', 'Fun times', '2022-03-25', '2022-03-25', 'Room 100', 16, 4, '250$ Steam Gift Card', 2, 3, 1, 30, 1, -1, 0 );
--INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Amazon Quizzes', 'Hooray', '2022-03-27', '2022-03-25', 'Room 100', 16, 4, '250$ Steam Gift Card', 2, 3, 1, 30, 1, -1, 0 );
--INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Tournament 2', 'Wee', '2022-03-27', '2022-03-25', 'Room 100', 16, 4, '250$ Steam Gift Card', 2, 2, 1, 30, 1, -1, 0 );

INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Tournament 3', 'test', '2022-03-31', '2022-03-30', 'Room 100', 16, 2, '250$ Steam Gift Card', 2, 2, 1, 30, 1, -1, 0 );

INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Tournament 4', 'test', '2022-03-31', '2022-03-30', 'Room 100', 16, 2, '250$ Steam Gift Card', 2, 1, 1, 30, 1, -1, 0 );
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Tournament 8', 'RR', '2022-04-02', '2022-03-30', 'Room 100', 16, 2, '250$ Steam Gift Card', 1, 1, 1, 1, 1, -1, 0 );
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Tournament 7', 'Single Knockout', '2022-04-01', '2022-03-30', 'Room 100', 64, 2, '250$ Steam Gift Card', 1, 0, 0, 30, 1, -1, 0 );

INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Tournament 11', 'Stress Testing', '2022-04-05', '2022-04-01', 'Room 100', 300, 2, '250$ Steam Gift Card', 1, 3, 0, 180, 1, 0, 0 );
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Tournament 12', 'Stress Testing Single Knockout', '2022-04-05', '2022-04-01', 'Room 100', 300, 2, '250$ Steam Gift Card', 1, 3, 0, 180, 1, 0, 0 );

--ALTER TABLE Tournament ADD match_by int;
--ALTER TABLE Tournament ADD series int;
--ALTER TABLE Tournament ADD current_round int;

ALTER TABLE Tournament RENAME COLUMN tournament_status TO status;

/*Create table statement for Availability*/
CREATE TABLE Availability(userID int, tournamentID int, day_of_week int, availability_string varchar(24), PRIMARY KEY(userID, tournamentID, day_of_week), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));

INSERT INTO Availability values (1, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (1, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (1, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (1, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (1, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (1, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (1, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (2, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (2, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (2, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (2, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (2, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (2, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (2, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (3, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (3, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (3, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (3, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (3, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (3, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (3, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (4, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (4, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (4, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (4, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (4, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (4, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (4, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (5, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (5, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (5, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (5, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (5, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (5, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (5, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (6, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (6, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (6, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (6, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (6, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (6, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (6, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (7, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (7, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (7, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (7, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (7, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (7, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (7, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (8, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (8, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (8, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (8, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (8, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (8, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (8, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (9, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (9, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (9, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (9, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (9, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (9, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (9, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (10, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (10, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (10, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (10, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (10, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (10, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (10, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (11, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (11, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (11, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (11, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (11, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (11, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (11, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (12, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (12, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (12, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (12, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (12, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (12, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (12, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (13, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (13, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (13, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (13, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (13, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (13, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (13, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (14, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (14, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (14, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (14, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (14, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (14, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (14, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (15, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (15, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (15, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (15, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (15, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (15, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (15, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (16, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (16, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (16, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (16, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (16, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (16, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (16, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (17, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (17, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (17, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (17, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (17, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (17, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (17, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (18, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (18, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (18, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (18, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (18, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (18, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (18, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (19, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (19, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (19, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (19, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (19, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (19, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (19, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (20, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (20, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (20, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (20, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (20, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (20, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (20, 9, 6, "000001000000100001100000");

INSERT INTO Availability values (21, 9, 0, "000111111100000000000000");
INSERT INTO Availability values (21, 9, 1, "001010000000100111110000");
INSERT INTO Availability values (21, 9, 2, "000100000011100000000110");
INSERT INTO Availability values (21, 9, 3, "000110000000000000111100");
INSERT INTO Availability values (21, 9, 4, "000001111100110000000000");
INSERT INTO Availability values (21, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (21, 9, 6, "000001111000000011100000");

INSERT INTO Availability values (22, 9, 0, "000001000000111000000000");
INSERT INTO Availability values (22, 9, 1, "000001110000100001010000");
INSERT INTO Availability values (22, 9, 2, "001100000000000000001110");
INSERT INTO Availability values (22, 9, 3, "000100000000011000110000");
INSERT INTO Availability values (22, 9, 4, "000001000000111000000000");
INSERT INTO Availability values (22, 9, 5, "011000000000011000000000");
INSERT INTO Availability values (22, 6, 6, "000001000000100001100000");

INSERT INTO Availability values (23, 6, 0, "000111111100000000000000");
INSERT INTO Availability values (23, 6, 1, "001010000000100111110000");
INSERT INTO Availability values (23, 6, 2, "000100000011100000000110");
INSERT INTO Availability values (23, 6, 3, "000110000000000000111100");
INSERT INTO Availability values (23, 6, 4, "000001111100110000000000");
INSERT INTO Availability values (23, 6, 5, "011000000000011000000000");
INSERT INTO Availability values (23, 6, 6, "000001111000000011100000");

INSERT INTO Availability values (24, 6, 0, "000001000000111000000000");
INSERT INTO Availability values (24, 6, 1, "000001110000100001010000");
INSERT INTO Availability values (24, 6, 2, "001100000000000000001110");
INSERT INTO Availability values (24, 6, 3, "000100000000011000110000");
INSERT INTO Availability values (24, 6, 4, "000001000000111000000000");
INSERT INTO Availability values (24, 6, 5, "011000000000011000000000");
INSERT INTO Availability values (24, 6, 6, "000001000000100001100000");

INSERT INTO Availability values (25, 6, 0, "000001000000111000000000");
INSERT INTO Availability values (25, 6, 1, "000001110000100001010000");
INSERT INTO Availability values (25, 6, 2, "001100000000000000001110");
INSERT INTO Availability values (25, 6, 3, "000100000000011000110000");
INSERT INTO Availability values (25, 6, 4, "000001000000111000000000");
INSERT INTO Availability values (25, 6, 5, "011000000000011000000000");
INSERT INTO Availability values (25, 6, 6, "000001000000100001100000");


INSERT INTO Availability values (1, 8, 0, "000001000000111000000000");
INSERT INTO Availability values (1, 8, 1, "000001110000100001010000");
INSERT INTO Availability values (1, 8, 2, "000000000000000000000000");
INSERT INTO Availability values (1, 8, 3, "000100000000011000110000");
INSERT INTO Availability values (1, 8, 4, "000001000000110000000000");
INSERT INTO Availability values (1, 8, 5, "011000000000000000000000");
INSERT INTO Availability values (1, 8, 6, "000000000000100001100000");

INSERT INTO Availability values (2, 8, 0, "000111111100000000000000");
INSERT INTO Availability values (2, 8, 1, "001010000000100111110000");
INSERT INTO Availability values (2, 8, 2, "000100000011100000000110");
INSERT INTO Availability values (2, 8, 3, "000110000000000000111100");
INSERT INTO Availability values (2, 8, 4, "000001111100110000000000");
INSERT INTO Availability values (2, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (2, 8, 6, "000001111000000011100000");

INSERT INTO Availability values (3, 8, 0, "000111111100000000000000");
INSERT INTO Availability values (3, 8, 1, "001010000000100111110000");
INSERT INTO Availability values (3, 8, 2, "000100000011100000000110");
INSERT INTO Availability values (3, 8, 3, "000110000000000000111100");
INSERT INTO Availability values (3, 8, 4, "000001111100110000000000");
INSERT INTO Availability values (3, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (3, 8, 6, "000001111000000011100000");

INSERT INTO Availability values (4, 8, 0, "000001000000111000000000");
INSERT INTO Availability values (4, 8, 1, "000001110000100001010000");
INSERT INTO Availability values (4, 8, 2, "001100000000000000001110");
INSERT INTO Availability values (4, 8, 3, "000100000000011000110000");
INSERT INTO Availability values (4, 8, 4, "000001000000111000000000");
INSERT INTO Availability values (4, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (4, 8, 6, "000001000000100001100000");

INSERT INTO Availability values (5, 8, 0, "000111111100000000000000");
INSERT INTO Availability values (5, 8, 1, "001010000000100111110000");
INSERT INTO Availability values (5, 8, 2, "000100000011100000000110");
INSERT INTO Availability values (5, 8, 3, "000110000000000000111100");
INSERT INTO Availability values (5, 8, 4, "000001111100110000000000");
INSERT INTO Availability values (5, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (5, 8, 6, "000001111000000011100000");

INSERT INTO Availability values (6, 8, 0, "000001000000111000000000");
INSERT INTO Availability values (6, 8, 1, "000001110000100001010000");
INSERT INTO Availability values (6, 8, 2, "001100000000000000001110");
INSERT INTO Availability values (6, 8, 3, "000100000000011000110000");
INSERT INTO Availability values (6, 8, 4, "000001000000111000000000");
INSERT INTO Availability values (6, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (6, 8, 6, "000001000000100001100000");

INSERT INTO Availability values (7, 8, 0, "000111111100000000000000");
INSERT INTO Availability values (7, 8, 1, "001010000000100111110000");
INSERT INTO Availability values (7, 8, 2, "000100000011100000000110");
INSERT INTO Availability values (7, 8, 3, "000110000000000000111100");
INSERT INTO Availability values (7, 8, 4, "000001111100110000000000");
INSERT INTO Availability values (7, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (7, 8, 6, "000001111000000011100000");

INSERT INTO Availability values (8, 8, 0, "000001000000111000000000");
INSERT INTO Availability values (8, 8, 1, "000001110000100001010000");
INSERT INTO Availability values (8, 8, 2, "001100000000000000001110");
INSERT INTO Availability values (8, 8, 3, "000100000000011000110000");
INSERT INTO Availability values (8, 8, 4, "000001000000111000000000");
INSERT INTO Availability values (8, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (8, 8, 6, "000001000000100001100000");

INSERT INTO Availability values (9, 8, 0, "000111111100000000000000");
INSERT INTO Availability values (9, 8, 1, "001010000000100111110000");
INSERT INTO Availability values (9, 8, 2, "000100000011100000000110");
INSERT INTO Availability values (9, 8, 3, "000110000000000000111100");
INSERT INTO Availability values (9, 8, 4, "000001111100110000000000");
INSERT INTO Availability values (9, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (9, 8, 6, "000001111000000011100000");

INSERT INTO Availability values (10, 8, 0, "000001000000111000000000");
INSERT INTO Availability values (10, 8, 1, "000001110000100001010000");
INSERT INTO Availability values (10, 8, 2, "001100000000000000001110");
INSERT INTO Availability values (10, 8, 3, "000100000000011000110000");
INSERT INTO Availability values (10, 8, 4, "000001000000111000000000");
INSERT INTO Availability values (10, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (10, 8, 6, "000001000000100001100000");

INSERT INTO Availability values (11, 8, 0, "000111111100000000000000");
INSERT INTO Availability values (11, 8, 1, "001010000000100111110000");
INSERT INTO Availability values (11, 8, 2, "000100000011100000000110");
INSERT INTO Availability values (11, 8, 3, "000110000000000000111100");
INSERT INTO Availability values (11, 8, 4, "000001111100110000000000");
INSERT INTO Availability values (11, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (11, 8, 6, "000001111000000011100000");

INSERT INTO Availability values (12, 8, 0, "000001000000111000000000");
INSERT INTO Availability values (12, 8, 1, "000001110000100001010000");
INSERT INTO Availability values (12, 8, 2, "001100000000000000001110");
INSERT INTO Availability values (12, 8, 3, "000100000000011000110000");
INSERT INTO Availability values (12, 8, 4, "000001000000111000000000");
INSERT INTO Availability values (12, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (12, 8, 6, "000001000000100001100000");

INSERT INTO Availability values (13, 8, 0, "000111111100000000000000");
INSERT INTO Availability values (13, 8, 1, "001010000000100111110000");
INSERT INTO Availability values (13, 8, 2, "000100000011100000000110");
INSERT INTO Availability values (13, 8, 3, "000110000000000000111100");
INSERT INTO Availability values (13, 8, 4, "000001111100110000000000");
INSERT INTO Availability values (13, 8, 5, "011000000000011000000000");
INSERT INTO Availability values (13, 8, 6, "000001111000000011100000");

/*Create table statement for Round*/
CREATE TABLE Round_Has(roundID int NOT NULL AUTO_INCREMENT, roundNumber int, tournamentID int, start_date DATETIME, end_date DATETIME, PRIMARY KEY(roundID), FOREIGN KEY(tournamentID) REFERENCES Tournament(tournamentID));
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,1,'2022/02/20','2022/02/27');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,1,'2022/03/05','2022/03/21');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,2,'2022/04/05','2022/04/12');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,2,'2022/04/10','2022/04/22');

ALTER TABLE Round_Has ADD roundNumber int;


/*is_conflict* is an int to represent whether the players have a conflict in their attendance responses (i.e. one player can attend while the other cannot)
/*Create table statement for Match_Has*/
CREATE TABLE Match_Has(matchID int NOT NULL AUTO_INCREMENT, start_time DATETIME, end_time DATETIME, roundID int, is_conflict int, userID_1 int, userID_2 int, PRIMARY KEY(matchID), FOREIGN KEY(roundID) REFERENCES Round_Has(roundID), FOREIGN KEY(userID_1) REFERENCES User(userID), FOREIGN KEY(userID_2) REFERENCES User(userID));

/*Create table statement for user_involves_match*/
CREATE TABLE User_involves_match(userID int, matchID int, results int, attendance varchar(40), PRIMARY KEY(userID, matchID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (matchID) REFERENCES Match_Has(matchID));

/*No sample data needed for User_involves_match since it will autopopulate after the trigger below is created*/
/* Notes on user_involves_match */
/* The results field is initialized as the string value, "Pending", this field can be updated by a user with one of the following values: */
/* "Win" - user has won the match, "Loss" - user lost the match, "Draw" - players tied */
/* Currently users may only update the results of a match if the corresponding results value is "TBD" */
/* I can change this for admin/general users */
/* The attendance field is initialized aas the string value, "TBD", this field can be updated by a user with one of the following values: */
/* "Yes" - user will attend the match, "No" - user cannot attend the match */


/*Create the following triggers before populating Match_Has*/
/*The first trigger autopopulates user_involves_match whenever there is an insert event in match_has*/
/*The second trigger deletes the relevant entries in user_involves_match whenever there is a delete row event in match_has*/
DELIMITER $$

CREATE TRIGGER update_user_involves_match
	AFTER INSERT
    ON Match_Has FOR EACH ROW
BEGIN
    INSERT INTO User_involves_match VALUES(NEW.userID_1, NEW.matchID, -1, 'TBD');
    INSERT INTO User_involves_match VALUES(NEW.userID_2, NEW.matchID, -1, 'TBD');
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER delete_user_involves_match_entries
    BEFORE DELETE
    ON Match_Has FOR EACH ROW
BEGIN
    DELETE FROM User_involves_match WHERE User_involves_match.userID = OLD.userID_1;
    DELETE FROM User_involves_match WHERE User_involves_match.userID = OLD.userID_2;
END$$
DELIMITER ;


/*sample data for Match_Has, Note: duration is in minutes */
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID, is_conflict, userID_1, userID_2) values ('2022/02/20 11:00:00','2022/02/20 11:30:00',30,1,0,1,2);
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID, is_conflict, userID_1, userID_2) values ('2022/02/20 12:00:00','2022/02/20 12:30:00',30,1,0,3,4);
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID,is_conflict, userID_1, userID_2) values ('2022/02/22 12:00:00','2022/02/20 12:30:00',30,1,0,5,6);
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID,is_conflict, userID_1, userID_2) values ('2022/02/25 11:00:00','2022/02/25 11:30:00',30,1,0,1,2);
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID,is_conflict, userID_1, userID_2) values ('2022/03/05 12:00:00','2022/03/05 12:30:00',30,2,0,3,6);
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID,is_conflict, userID_1, userID_2) values ('2022/03/05 12:30:00','2022/03/05 13:00:00',30,2,0,1,2);
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID,is_conflict, userID_1, userID_2) values ('2022/03/14 14:30:00','2022/03/14 15:00:00',30,2,0,1,3);
-- INSERT INTO Match_Has(start_time,end_time,duration,roundID,is_conflict, userID_1, userID_2) values ('2022/03/21 15:30:00','2022/03/21 16:00:00',30,2,0,2,4);

/*Create table statement for Admin_hosts_tournament*/
CREATE TABLE Admin_hosts_tournament(userID int, tournamentID int, PRIMARY KEY(userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));


/*Create table User_registers_tournament*/
CREATE TABLE User_registers_tournament(userID int, tournamentID int, skill_level int, PRIMARY KEY (userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (1,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (2,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (3,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (4,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (5,9,0);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (6,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (7,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (8,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (9,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (10,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (11,9,0);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (12,9,0);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (13,9,0);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (14,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (15,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (16,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (17,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (18,9,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (19,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (20,9,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (21,6,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (22,6,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (23,6,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (24,6,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (25,6,1);

/* STRESS TEST */
DELIMITER $$

CREATE PROCEDURE CreateRegistrants(
)
BEGIN
    
    DECLARE counter INT DEFAULT 0;
    DECLARE availabilityCounter INT DEFAULT 0;
    SET @LENGTH = 24;
    SET @CharPool = '01';
	SET @PoolLength = Length(@CharPool);
    SET @t_id = 11;

    WHILE counter < 256 DO
        SET availabilityCounter = 0;
        SET @id = LAST_INSERT_ID();
		INSERT INTO User(firebase_id, name, email, is_admin) values ('abc', CONCAT('Player ', @id) , CONCAT(@id, '@gmail.com'), 0);
        WHILE availabilityCounter < 7 DO
			SET @LoopCount = 0;
			SET @RandomString = '';
			WHILE (@LoopCount < @Length) DO
				SELECT @RandomString = @RandomString + 
				SUBSTRING(@Charpool, RAND() * @PoolLength + 1, 1);
				INSERT INTO Availability values (@id, @t_id, availabilityCounter, @RandomString);
				SELECT @LoopCount = @LoopCount + 1;
            END WHILE;
        END WHILE;
		INSERT INTO User_registers_tournament values (@id,11,1,RAND() * 3);
        SET counter = counter + 1;
    END WHILE;

END$$

DELIMITER ;

DROP PROCEDURE CreateRegistrants;
CALL CreateRegistrants();

DELIMITER $$

CREATE PROCEDURE CreateRegistrantsStress(
)
BEGIN
    
    DECLARE counter INT DEFAULT 0;
    DECLARE availabilityCounter INT DEFAULT 0;

    WHILE counter < 256 DO
        SET @id = LAST_INSERT_ID();
        INSERT INTO User(firebase_id, name, email, is_admin) values ('abc', CONCAT('Player ', @id) , CONCAT(@id, '@gmail.com'), 0);
        INSERT INTO Availability values (@id, 11, 0, "111111111111111111111111");
        INSERT INTO Availability values (@id, 11, 1, "111111111111111111111111");
        INSERT INTO Availability values (@id, 11, 2, "111111111111111111111111");
        INSERT INTO Availability values (@id, 11, 3, "111111111111111111111111");
        INSERT INTO Availability values (@id, 11, 4, "111111111111111111111111");
        INSERT INTO Availability values (@id, 11, 5, "111111111111111111111111");
        INSERT INTO Availability values (@id, 11, 6, "111111111111111111111111");
        INSERT INTO User_registers_tournament values (@id,11,1,0);
        SET counter = counter + 1;
    END WHILE;

END$$

DELIMITER ;

DROP PROCEDURE CreateRegistrantsStress;
CALL CreateRegistrantsStress();

/*Create table statement for Invitation Code*/
CREATE TABLE Invitation_Code ( invitationCode varchar(10) NOT NULL, isValid tinyint(1) NOT NULL, createdOn varchar(30) NOT NULL, UNIQUE KEY invitationCode (invitationCode));
--INSERT INTO Invitation_Code(invitationCode,isValid,createdOn) VALUES('0M2WTV2J84', '0', '2022-03-10 23:16:05');
--INSERT INTO Invitation_Code(invitationCode,isValid,createdOn) VALUES('L3XAU31X3L', '1', '2022-03-10 23:28:15');

--SET SQL_SAFE_UPDATES = 0;
--
--/* ALL DELETE STATEMENTS */
--
--DELETE FROM Match_has;
--ALTER TABLE match_has auto_increment=1;
--DELETE FROM User;

