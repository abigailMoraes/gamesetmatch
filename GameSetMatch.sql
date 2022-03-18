CREATE TABLE User(userID int NOT NULL AUTO_INCREMENT, company_id varchar(100), name varchar(80), email varchar(319), is_admin int, PRIMARY KEY(userID));

/*Populate user_info table with sample data*/
INSERT INTO User(company_id, name, email, is_admin) values ('hcarloni','Heike Carloni', 'hcarloni@gmail.com', 1);


/*Create table statement for Tournament*/
CREATE TABLE Tournament(tournamentID int NOT NULL AUTO_INCREMENT, name varchar(128), format varchar(60), location varchar(60), description varchar(150), max_participants int, min_participants int, prize varchar(60), start_date  DATE, end_date DATE,  close_registration_date DATE,PRIMARY KEY(tournamentID));

ALTER TABLE Tournament ADD match_duration int;
ALTER TABLE Tournament ADD number_of_matches int;
ALTER TABLE Tournament ADD type varchar(60);
ALTER TABLE Tournament ADD round_duration int;
ALTER TABLE Tournament ADD admin_hosts_tournament int;
ALTER TABLE Tournament ADD status int;



INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values ('Mariokart Madness', 'single knockout elimination','West Atrium room 203', 'Come join us for some krazy karting! (Individual)', 32, 4,'250$ Steam Gift Card', '2022-02-20', '2022-04-01', 7200);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values ('Amongus Royale', 'single knockout elimination', 'Auditorium 6, North Wing', 'Come join us for some space murder! (individual)', 10, 6, '50$ Steam Gift Card', '2022-03-04', '2022-04-15', 3600);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values('Spike or Bust Tournament', 'double knockout elimination', 'Volleyball court 3', 'Bump. Set. Spike. All it takes is all you have got! (2 player teams)', 16, 2, '50$ GoSport Canada Gift Card', '2022-03-09', '2022-04-21', 14400);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values ('StreetFighter V Tournament', 'Round Robin', 'Meeting Room 143, Basement', 'Choose your hero and test your skills!(individual)', 8, 4,'50$ GoSport Canada Gift Card', '2022-04-03', '2022-04-27', 4320);
INSERT INTO Tournament(name, format, location, description, max_participants, min_participants, prize, start_date, close_registration_date, round_duration) values('Amazon Chess Championship', 'single knockout elimination', 'Meeting Room 13, Basement', 'Come battle your wits against the best and the brightest!', 32, 4, '100$ Amazon Gift Card', '2022-04-05', '2022-04-13', 7200);


CREATE TABLE User_registers_tournament(userID int, tournamentID int, skill_level int, PRIMARY KEY (userID, tournamentID), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));

/*Create table statement for Availability*/
CREATE TABLE Availability(userID int, tournamentID int, date DATE, availability_binary int, PRIMARY KEY(userID, tournamentID, date), FOREIGN KEY (userID) REFERENCES User(userID), FOREIGN KEY (tournamentID) REFERENCES Tournament(tournamentID));
