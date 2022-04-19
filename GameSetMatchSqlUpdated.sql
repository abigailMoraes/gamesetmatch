/*Create table statement for user_info table, dept not included, added is_admin boolean and changed primary key to include both userID*/
CREATE TABLE User(userID int NOT NULL AUTO_INCREMENT, firebase_id varchar(100), name varchar(80), email varchar(319), is_admin int, PRIMARY KEY(userID));

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

ALTER TABLE Tournament ADD round_start_date DATE;

/*Create table statement for Availability*/
CREATE TABLE Availability(userID int, tournamentID int, day_of_week int, availability_string varchar(24), PRIMARY KEY(userID, tournamentID, day_of_week), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));

/*Create table statement for Round*/
CREATE TABLE Round_Has(roundID int NOT NULL AUTO_INCREMENT, roundNumber int, tournamentID int, start_date DATETIME, end_date DATETIME, PRIMARY KEY(roundID), FOREIGN KEY(tournamentID) REFERENCES Tournament(tournamentID));
--INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,1,'2022/02/20','2022/02/27');
--INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,1,'2022/03/05','2022/03/21');
--INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (1,2,'2022/04/05','2022/04/12');
--INSERT INTO Round_Has(roundNumber, tournamentID, start_date, end_date) values (2,2,'2022/04/10','2022/04/22');

--ALTER TABLE Round_Has ADD roundNumber int;

/*match_status is an int to represent whether the players have a conflict in their attendance responses (i.e. one player can attend while the other cannot) */
/*Create table statement for Match_Has*/
CREATE TABLE Match_Has(matchID int NOT NULL AUTO_INCREMENT, start_time DATETIME, end_time DATETIME, roundID int,  match_status int, userID_1 int, userID_2 int, PRIMARY KEY(matchID), FOREIGN KEY(roundID) REFERENCES Round_Has(roundID), FOREIGN KEY(userID_1) REFERENCES User(userID), FOREIGN KEY(userID_2) REFERENCES User(userID));

/* if you don't want to reacreate the table*/
--ALTER TABLE Match_Has DROP Column is_conflict;
--ALTER TABLE Match_Has ADD COLUMN match_status int;
ALTER TABLE Match_Has ADD isPublished int;

/* if have the column as boolean*/
--ALTER TABLE Match_Has MODIFY COLUMN isPublished int;


/*Create table statement for user_involves_match*/
CREATE TABLE User_involves_match(userID int, matchID int, results int, attendance varchar(40), PRIMARY KEY(userID, matchID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (matchID) REFERENCES Match_Has(matchID));

/* No sample data needed for User_involves_match since it will autopopulate after the trigger below is created */
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

/*Create table statement for Admin_hosts_tournament*/
        CREATE TABLE Admin_hosts_tournament(userID int, tournamentID int, PRIMARY KEY(userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));


/*Create table User_registers_tournament*/
        CREATE TABLE User_registers_tournament(userID int, tournamentID int, skill_level int, PRIMARY KEY (userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));
        ALTER TABLE User_registers_tournament ADD player_status int;
/*Create table statement for Invitation Code*/

        CREATE TABLE Invitation_Code (invitation_code varchar(10) NOT NULL, is_valid bit(1) NOT NULL, created_on varchar(30) NOT NULL, UNIQUE KEY invitationCode (invitation_code));
/*sample data for Invitation Code*/
        INSERT INTO Invitation_Code(invitation_code, is_valid, created_on) VALUES('0M2WTV2J84', 0, '2022-03-10 23:16:05');
        INSERT INTO Invitation_Code(invitation_code, is_valid, created_on) VALUES('L3XAU31X3L', 1, CURRENT_TIMESTAMP);

        SET SQL_SAFE_UPDATES = 0;
/* ALL DELETE STATEMENTS */

--         DELETE FROM Match_Has;
--         ALTER TABLE Match_Has auto_increment=1;
--         DELETE FROM User;
