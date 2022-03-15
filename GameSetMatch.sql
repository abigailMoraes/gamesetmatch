/*Create table statement for user_info table, dept not included, added is_admin boolean and changed primary key to include both userID*/
CREATE TABLE User(userID int NOT NULL AUTO_INCREMENT, firebase_id varchar(100), name varchar(80), email varchar(319), is_admin int, PRIMARY KEY(userID));

/*Populate user_info table with sample data*/
INSERT INTO user(firebase_id, name, email, is_admin) values ('hcarloni','Heike Carloni', 'hcarloni@gmail.com', 1);
INSERT INTO user(firebase_id, name, email, is_admin) values ('mbaron', 'Michael Baron','mbaron@gmail.com', 0);
INSERT INTO user(firebase_id, name, email, is_admin) values ('msanders', 'Michelle Sanders', 'msanders@gmail.com', 1);
INSERT INTO user(firebase_id, name, email, is_admin) values ('efiaro', 'Ester Fiaro', 'efiaro@gmail.com', 0);
INSERT INTO user(firebase_id, name, email, is_admin) values ('pghosh', 'Prabhat Ghosh', 'pghosh@gmail.com',0);
INSERT INTO user(firebase_id, name, email, is_admin) values ('amorales', 'Alberto Morales','amorales@gmail.com', 0);
INSERT INTO user(firebase_id, name, email, is_admin) values ('echu', 'Eileen Chu', 'echu@gmail.com', 0);


/*Create table statement for Tournament*/
CREATE TABLE Tournament(tournamentID int NOT NULL AUTO_INCREMENT, name varchar(128), format varchar(60), location varchar(60), description varchar(150), max_participants int, min_participants int, prize varchar(60), start_date  DATE, end_date DATE,  close_registration_date DATE, PRIMARY KEY(tournamentID));

ALTER TABLE Tournament ADD round_duration int;
ALTER TABLE Tournament ADD match_duration int;
ALTER TABLE Tournament ADD number_of_matches int;
ALTER TABLE Tournament ADD type varchar(60);
ALTER TABLE Tournament ADD admin_hosts_tournament int;

INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values ('Mariokart Madness', 'single knockout elimination','West Atrium room 203', 'Come join us for some krazy karting! (Individual)', 32, 4,'250$ Steam Gift Card', '2022-02-20', '2022-04-01', 7200);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values ('Amongus Royale', 'single knockout elimination', 'Auditorium 6, North Wing', 'Come join us for some space murder! (individual)', 10, 6, '50$ Steam Gift Card', '2022-03-04', '2022-04-15', 3600);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values('Spike or Bust Tournament', 'double knockout elimination', 'Volleyball court 3', 'Bump. Set. Spike. All it takes is all you have got! (2 player teams)', 16, 2, '50$ GoSport Canada Gift Card', '2022-03-09', '2022-04-21', 14400);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values ('StreetFighter V Tournament', 'Round Robin', 'Meeting Room 143, Basement', 'Choose your hero and test your skills!(individual)', 8, 4,'50$ GoSport Canada Gift Card', '2022-04-03', '2022-04-27', 4320);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values('Amazon Chess Championship', 'single knockout elimination', 'Meeting Room 13, Basement', 'Come battle your wits against the best and the brightest!', 32, 4, '100$ Amazon Gift Card', '2022-04-05', '2022-04-13', 7200);

/*Create table statement for Availability*/
CREATE TABLE Availability(userID int, tournamentID int, date DATE, availability_binary int, PRIMARY KEY(userID, tournamentID, date), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));

/*Create table statement for Round*/
CREATE TABLE Round_Has(roundID int NOT NULL AUTO_INCREMENT, duration int, type varchar(60), tournamentID int, PRIMARY KEY(roundID), FOREIGN KEY(tournamentID) REFERENCES Tournament(tournamentID));
INSERT INTO Round_Has(duration, type, tournamentID) values (7,'Preliminary',1);
INSERT INTO Round_Has(duration, type, tournamentID) values (7,'Preliminary',1);
INSERT INTO Round_Has(duration, type, tournamentID) values (7,'Semifinal',1);
INSERT INTO Round_Has(duration, type, tournamentID) values (7,'Final',1);

/*Create table statement for Match_Has*/
CREATE TABLE Match_Has(matchID int NOT NULL AUTO_INCREMENT, result int, start_time DATETIME, end_time DATETIME, duration int, roundID int, PRIMARY KEY(matchID), FOREIGN KEY(roundID) REFERENCES Round_Has(roundID));

/*Create table statement for Admin_hosts_tournament*/
CREATE TABLE Admin_hosts_tournament(userID int, tournamentID int, PRIMARY KEY(userID, tournamentID), FOREIGN KEY (userID) REFERENCES user(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));

/*Create table statement for user_involves_match*/
CREATE TABLE User_involves_match(userID int, matchID int, PRIMARY KEY(userID, matchID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (matchID) REFERENCES Match_Has(matchID));

/*Create table User_registers_tournament*/
CREATE TABLE User_registers_tournament(userID int, tournamentID int, skill_level int, PRIMARY KEY (userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES tournament(tournamentID));

/*Create table statement for Invitation Code*/
CREATE TABLE Invitation_Code ( invitationCode varchar(10) NOT NULL, isValid tinyint(1) NOT NULL, createdOn varchar(30) NOT NULL, UNIQUE KEY invitationCode (invitationCode));
INSERT INTO `Invitation_Code`(`invitationCode`,`isValid`,`createdOn`) VALUES('0M2WTV2J84', '0', '2022-03-10 23:16:05');
INSERT INTO `Invitation_Code`(`invitationCode`,`isValid`,`createdOn`) VALUES('L3XAU31X3L', '1', '2022-03-10 23:28:15');
