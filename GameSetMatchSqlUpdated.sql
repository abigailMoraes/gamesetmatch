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

INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status) values('Mariokart Madness', 'Come join us for some krazy karting! (Individual)', '2022-02-20', '2022-02-19', 'West Atrium room 203', 32, 4, '250$ Steam Gift Card', 1, 1, 1, 30, 1, -1, 0 );
INSERT INTO Tournament(name, description, start_date, close_registration_date, location, max_participants, min_participants, prize, format, series, match_by, match_duration, admin_hosts_tournament, status, current_round) values('Mariokart Madness', 'Come join us for some krazy karting! (Individual)', '2022-02-20', '2022-02-19', 'West Atrium room 203', 32, 4, '250$ Steam Gift Card', 1, 1, 1, 30, 1, -1, 0 );

ALTER TABLE Tournament ADD match_by int;
ALTER TABLE Tournament ADD series int;
ALTER TABLE Tournament ADD current_round int;
ALTER TABLE Round_Has ADD roundNumber int;

/*Create table statement for Availability*/
CREATE TABLE Availability(userID int, tournamentID int, day_of_week int, availability_string varchar(24), PRIMARY KEY(userID, tournamentID, day_of_week), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));

INSERT INTO Availability values (1, 1, 1, "000000001110000000000000");
INSERT INTO Availability values (1, 1, 2, "000000000000000000000000");
INSERT INTO Availability values (1, 1, 3, "000000000000000000000000");
INSERT INTO Availability values (1, 1, 4, "000000000000000000000000");
INSERT INTO Availability values (1, 1, 5, "000000000000000000000000");
INSERT INTO Availability values (1, 1, 6, "000000000000000000000000");
INSERT INTO Availability values (1, 1, 7, "000000000000000000000000");

INSERT INTO Availability values (2, 1, 1, "001000001110000100000000");
INSERT INTO Availability values (2, 1, 2, "000000000000000000000000");
INSERT INTO Availability values (2, 1, 3, "000000000000000000000000");
INSERT INTO Availability values (2, 1, 4, "000000000000000000000000");
INSERT INTO Availability values (2, 1, 5, "000000000000000000000000");
INSERT INTO Availability values (2, 1, 6, "000000000000000000000000");
INSERT INTO Availability values (2, 1, 7, "000000000000000000000000");

INSERT INTO Availability values (3, 1, 1, "000000001000001110000000");
INSERT INTO Availability values (3, 1, 2, "000000000000000000000000");
INSERT INTO Availability values (3, 1, 3, "000000000000000000000000");
INSERT INTO Availability values (3, 1, 4, "000000000000000000000000");
INSERT INTO Availability values (3, 1, 5, "000000000000000000000000");
INSERT INTO Availability values (3, 1, 6, "000000000000000000000000");
INSERT INTO Availability values (3, 1, 7, "000000000000000000000000");

INSERT INTO Availability values (4, 1, 1, "000010000000001110000000");
INSERT INTO Availability values (4, 1, 2, "000000000000000000000000");
INSERT INTO Availability values (4, 1, 3, "000000000000000000000000");
INSERT INTO Availability values (4, 1, 4, "000000000000000000000000");
INSERT INTO Availability values (4, 1, 5, "000000000000000000000000");
INSERT INTO Availability values (4, 1, 6, "000000000000000000000000");
INSERT INTO Availability values (4, 1, 7, "000000000000000000000000");

INSERT INTO Availability values (5, 1, 1, "000000001000001110000000");
INSERT INTO Availability values (5, 1, 2, "000000000000000000000000");
INSERT INTO Availability values (5, 1, 3, "000000000000000000000000");
INSERT INTO Availability values (5, 1, 4, "000000000000000000000000");
INSERT INTO Availability values (5, 1, 5, "000000000000000000000000");
INSERT INTO Availability values (5, 1, 6, "000000000000000000000000");
INSERT INTO Availability values (5, 1, 7, "000000000000000000000000");

INSERT INTO Availability values (6, 1, 1, "001000000000000000000000");
INSERT INTO Availability values (6, 1, 2, "000000000000000000000000");
INSERT INTO Availability values (6, 1, 3, "000000000000000000000000");
INSERT INTO Availability values (6, 1, 4, "000000000000000000000000");
INSERT INTO Availability values (6, 1, 5, "000000000000000000000000");
INSERT INTO Availability values (6, 1, 6, "000000000000000000000000");
INSERT INTO Availability values (6, 1, 7, "000000000000000000000000");

/*Create table statement for Round*/
CREATE TABLE Round_Has(roundID int NOT NULL AUTO_INCREMENT, roundNumber int, tournamentID int, start_date DATETIME, end_date DATETIME, PRIMARY KEY(roundID), FOREIGN KEY(tournamentID) REFERENCES Tournament(tournamentID));
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,1,'2022/02/20','2022/02/27');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,1,'2022/03/05','2022/03/21');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,2,'2022/04/05','2022/04/12');
INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,2,'2022/04/10','2022/04/22');

/*is_conflict* is an int to represent whether the players have a conflict in their attendance responses (i.e. one player can attend while the other cannot)
/*Create table statement for Match_Has*/
CREATE TABLE Match_Has(matchID int NOT NULL AUTO_INCREMENT, start_time DATETIME, end_time DATETIME, roundID int, is_conflict int, userID_1 int, userID_2 int, PRIMARY KEY(matchID), FOREIGN KEY(roundID) REFERENCES Round_Has(roundID), FOREIGN KEY(userID_1) REFERENCES User(userID), FOREIGN KEY(userID_2) REFERENCES User(userID));
/*Create table statement for user_involves_match*/
CREATE TABLE User_involves_match(userID int, matchID int, results varchar(40), attendance varchar(40), PRIMARY KEY(userID, matchID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (matchID) REFERENCES Match_Has(matchID));
/*No sample data needed for User_involves_match since it will autopopulate after the trigger below is created*/

/*Create the following triggers before populating Match_Has*/
/*The first trigger autopopulates user_involves_match whenever there is an insert event in match_has*/
/*The second trigger deletes the relevant entries in user_involves_match whenever there is a delete row event in match_has*/
DELIMITER $$

CREATE TRIGGER update_user_involves_match
	AFTER INSERT
    ON Match_Has FOR EACH ROW
BEGIN
    INSERT INTO User_involves_match VALUES(NEW.userID_1, NEW.matchID, 'Pending', 'TBD');
    INSERT INTO User_involves_match VALUES(NEW.userID_2, NEW.matchID, 'Pending', 'TBD');
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
