# Zoomers-AWS-Backend
- brew install spring-boot
- brew install maven
- brew install mysql

To run:
mvn spring-boot:run

Download:
- mysql workbench
- mysql server
- Create user table 

``` CREATE TABLE `User` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `phone_num` varchar(45) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `discriminator` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `phoneNum_UNIQUE` (`phone_num`),
  UNIQUE KEY `employee_id_UNIQUE` (`employee_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;```