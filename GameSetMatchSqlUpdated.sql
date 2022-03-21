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


/*Create table statement for Tournament*/
CREATE TABLE Tournament(tournamentID int NOT NULL AUTO_INCREMENT, name varchar(128),  description varchar(150), start_date  DATE,  close_registration_date DATE, location varchar(60), max_participants int, min_participants int,  end_date DATE, prize varchar(60), format varchar(60), type varchar(60), match_by_skill int, match_duration long, number_of_matches int, round_duration int, admin_hosts_tournament int, status int, PRIMARY KEY(tournamentID),
                       FOREIGN KEY (admin_hosts_tournament) REFERENCES User(userID));

INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, type, match_by_skill, match_duration, round_duration, admin_hosts_tournament, status) values('Mariokart Madness', 'Come join us for some krazy karting! (Individual)', '2022-02-20', '2022-02-19', 'West Atrium room 203', 32, 4, '250$ Steam Gift Card', 'BEST_OF_1','SINGLE_KNOCKOUT',1,30, 7, 1, -1 );
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, type, match_by_skill, match_duration, round_duration, admin_hosts_tournament, status) values('Amongus Royale', 'Come join us for some space murder! (individual)','2022-03-04', '2022-03-03','Auditorium 6, North Wing',  10, 6, '50$ Steam Gift Card', 'BEST_OF_3', 'SINGLE_KNOCKOUT',0,30, 7, 3, 0);
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, type, match_by_skill, match_duration, round_duration, admin_hosts_tournament, status) values('Spike or Bust Tournament', 'Bump. Set. Spike. All it takes is all you have got! (2 player teams)',  '2022-03-09', '2022-03-08',  'Volleyball court 3', 16, 2, '50$ GoSport Canada Gift Card', 'BEST_OF_5', 'DOUBLE_KNOCKOUT', 1,30, 7, 1, 0);
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, type, match_by_skill, match_duration, round_duration, admin_hosts_tournament, status) values('StreetFighter V Tournament', 'Choose your hero and test your skills!(individual)', '2022-04-03', '2022-04-02', 'Meeting Room 143,Basement',  8, 4,  '50$ GoSport Canada Gift Card', 'BEST_OF_7' ,'ROUND_ROBIN',0,30, 7, 3, 0);
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, type, match_by_skill, match_duration, round_duration, admin_hosts_tournament, status) values('Amazon Chess Championship', 'Come battle your wits against the best and the brightest!', '2022-04-05', '2022-04-04',  'Meeting Room 13, Basement', 32, 4,  '100$ Amazon Gift Card', 'BEST_OF_1'  ,'SINGLE_KNOCKOUT',1, 7, 30, 1, 0);

/*Create table statement for Availability*/
CREATE TABLE Availability(userID int, tournamentID int, date DATE, availability_binary int, PRIMARY KEY(userID, tournamentID, date), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));

/*Create table statement for Round*/
CREATE TABLE Round_Has(roundID int NOT NULL AUTO_INCREMENT, roundNumber int, tournamentID int, start_date DATE, end_date DATE, PRIMARY KEY(roundID), FOREIGN KEY(tournamentID) REFERENCES Tournament(tournamentID));
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,1,'2022/02/20','2022/02/27');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,1,'2022/03/05','2022/03/21');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,2,'2022/04/05','2022/04/12');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,2,'2022/04/10','2022/04/22');

/*Create table statement for Match_Has*/
CREATE TABLE Match_Has(matchID int NOT NULL AUTO_INCREMENT, start_time DATETIME, end_time DATETIME, duration int, roundID int, PRIMARY KEY(matchID), FOREIGN KEY(roundID) REFERENCES Round_Has(roundID));
/*sample data for Match_Has, Note: duration is in minutes */
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/02/20 11:00:00','2022/02/20 11:30:00',30,1);
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/02/20 12:00:00','2022/02/20 12:30:00',30,1);
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/02/22 12:00:00','2022/02/20 12:30:00',30,1);
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/02/25 11:00:00','2022/02/25 11:30:00',30,1);
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/03/05 12:00:00','2022/03/05 12:30:00',30,2);
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/03/05 12:30:00','2022/03/05 13:00:00',30,2);
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/03/14 14:30:00','2022/03/14 15:00:00',30,2);
 INSERT INTO Match_Has(start_time,end_time,duration,roundID) values ('2022/03/21 15:30:00','2022/03/21 16:00:00',30,2);

/*Create table statement for Admin_hosts_tournament*/
CREATE TABLE Admin_hosts_tournament(userID int, tournamentID int, PRIMARY KEY(userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));



/*Create table statement for user_involves_match*/
CREATE TABLE User_involves_match(userID int, matchID int, results varchar(40), attendance varchar(40), PRIMARY KEY(userID, matchID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (matchID) REFERENCES Match_Has(matchID));
/*sample data for User_involves_match*/
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (1, 1, 'win', 'No');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (1, 4, 'loss', 'No');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (1, 6, 'TBD', 'No');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (1, 7, 'TBD', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (2, 1, 'loss', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (2, 4, 'win', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (2, 6, 'TBD', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (3, 2, 'win', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (3, 5, 'loss', 'No');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (3, 7, 'TBD', 'No');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (4, 2, 'TBD', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (5, 3, 'TBD', 'No');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (6, 3, 'TBD', 'No');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (6, 5, 'TBD', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (2, 8, 'TBD', 'Yes');
INSERT INTO User_involves_match(userID, matchID, results, attendance) values (4, 8, 'TBD', 'Yes');



/*Create table User_registers_tournament*/
CREATE TABLE User_registers_tournament(userID int, tournamentID int, skill_level int, PRIMARY KEY (userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES tournament(tournamentID));
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (1,1,3);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (2,1,3);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (3,1,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (4,1,2);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (5,1,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (6,1,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (1,2,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (2,2,1);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (3,2,3);
INSERT INTO User_registers_tournament(userID, tournamentID, skill_level) values (6,2,3);

/*Create table statement for Invitation Code*/
CREATE TABLE Invitation_Code ( invitationCode varchar(10) NOT NULL, isValid tinyint(1) NOT NULL, createdOn varchar(30) NOT NULL, UNIQUE KEY invitationCode (invitationCode));
INSERT INTO Invitation_Code(invitationCode,isValid,createdOn) VALUES('0M2WTV2J84', '0', '2022-03-10 23:16:05');
INSERT INTO Invitation_Code(invitationCode,isValid,createdOn) VALUES('L3XAU31X3L', '1', '2022-03-10 23:28:15');
