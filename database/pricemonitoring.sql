DROP SCHEMA IF EXISTS `pricemonitoring`;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `pricemonitoring` DEFAULT CHARACTER SET utf8;
USE `pricemonitoring` ;

CREATE TABLE IF NOT EXISTS `brands` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `contact_number` VARCHAR(19) NOT NULL,
  `unp` INT NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `shops` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(150) NOT NULL,
  `contact_number` VARCHAR(19) NOT NULL,
  `open_time` TIME NOT NULL,
  `close_time` TIME NOT NULL,
  `type` ENUM('GASTRONOME', 'HYPERMARKET', 'SUPERMARKET') NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  `brands_id` INT NOT NULL,
  PRIMARY KEY (`id`, `brands_id`),
  INDEX `fk_shops_brands1_idx` (`brands_id` ASC) VISIBLE,
  CONSTRAINT `fk_shops_brands1`
    FOREIGN KEY (`brands_id`)
    REFERENCES `brands` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` ENUM('PRODUCTS', 'HOUSEHOLD_CHEMICALS', 'STATIONERY', 'BAGS', 'SHOES') NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `subcategories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` ENUM('DAIRY_PRODUCTS', 'FRUITS', 'CHICKEN', 'LAUNDRY', 'DISHWASHER', 'KITCHEN', 'NOTEBOOKS', 'STATIONERY', 'FOR_CREATIVITY',
  'BACKPACKS', 'WAIST_BAGS', 'SANDALS', 'SNEAKERS') NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  `categories_id` INT NOT NULL,
  PRIMARY KEY (`id`, `categories_id`),
  INDEX `fk_subcategories_categories1_idx` (`categories_id` ASC) VISIBLE,
  CONSTRAINT `fk_subcategories_categories1`
    FOREIGN KEY (`categories_id`)
    REFERENCES `categories` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `goods` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `producer` VARCHAR(50) NOT NULL,
  `country` VARCHAR(35) NOT NULL,
  `description` VARCHAR(500) NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  `subcategories_id` INT NOT NULL,
  PRIMARY KEY (`id`, `subcategories_id`),
  INDEX `fk_goods_subcategories1_idx` (`subcategories_id` ASC) VISIBLE,
  CONSTRAINT `fk_goods_subcategories1`
    FOREIGN KEY (`subcategories_id`)
    REFERENCES `subcategories` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(50) NOT NULL,
  `password` VARCHAR(250) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `first_name` VARCHAR(50) NOT NULL,
  `registration_date` DATE NOT NULL,
  `mobile_number` VARCHAR(19) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `birthday` DATETIME NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `prices` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `price` DECIMAL(8,2) NOT NULL,
  `status` ENUM('ACTUAL', 'HISTORY', 'DELETED') NOT NULL,
  `goods_id` INT NOT NULL,
  `shops_id` INT NOT NULL,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`id`, `goods_id`, `shops_id`, `users_id`),
  INDEX `fk_prices_goods1_idx` (`goods_id` ASC) VISIBLE,
  INDEX `fk_prices_shops1_idx` (`shops_id` ASC) VISIBLE,
  INDEX `fk_prices_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_prices_goods1`
    FOREIGN KEY (`goods_id`)
    REFERENCES `goods` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prices_shops1`
    FOREIGN KEY (`shops_id`)
    REFERENCES `shops` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prices_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `ratings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `assortment` INT NOT NULL,
  `quality_of_service` INT NOT NULL,
  `prices` INT NOT NULL,
  `date` DATE NOT NULL,
  `shops_id` INT NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`id`, `shops_id`, `users_id`),
  INDEX `fk_ratings_shops1_idx` (`shops_id` ASC) VISIBLE,
  INDEX `fk_ratings_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_ratings_shops1`
    FOREIGN KEY (`shops_id`)
    REFERENCES `shops` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ratings_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `status` ENUM('DELETED', 'ACTUAL') NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `tokens` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(250) NOT NULL,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`id`, `users_id`),
  INDEX `fk_tokens_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_tokens_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `users_has_roles` (
  `users_id` INT NOT NULL,
  `roles_id` INT NOT NULL,
  PRIMARY KEY (`users_id`, `roles_id`),
  INDEX `fk_users_has_roles_roles1_idx` (`roles_id` ASC) VISIBLE,
  INDEX `fk_users_has_roles_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_roles_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_roles_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `brands` (`name`,`email`,`contact_number`,`unp`, `status`)
VALUES ('Gippo','bwd@willesden.by','+375 (29) 723-23-23',101568881,'ACTUAL'),
('Perekrestok','erema.info@gmail.com','+375 (17) 511-63-59',234374258,'ACTUAL'),
('Belmarket','info@belma.by','+375 (29) 348-04-25',191178504,'ACTUAL'),
('Kopeechka','info@dobronom.by','+375 (29) 144-70-00',192364753,'ACTUAL'),
('Rodny kut','rodnykut@oblpo.by','+375 (33) 622-27-27',590959404,'ACTUAL');

INSERT INTO `shops` (`address`,`contact_number`,`open_time`,`close_time`,`type`, `status`,`brands_id`)
VALUES ('ul. Mikrorajon 1, 4a, Zaslavl','+375 (17) 511-05-65','9:00','23:00','HYPERMARKET','ACTUAL',1),
('ul. Sovetskaya, 48, Zaslavl','+375 (29) 356-25-81','8:00','22:00','SUPERMARKET','ACTUAL',2),
('per. Studeneckij, 7, Zaslavl','+375 (17) 535-55-29','9:00','23:00','HYPERMARKET','ACTUAL',3),
('per. Studeneckij, 5, Zaslavl','+375 (17) 511-27-18','8:00','22:00','SUPERMARKET','ACTUAL',4),
('ul. Sovetskaya, 81b, Zaslavl','+375 (29) 732-32-63','9:00','22:00','SUPERMARKET','ACTUAL',4),
('ul. Sovetskaya, 98, Zaslavl','+375 (17) 511-32-27','9:00','21:00','SUPERMARKET','ACTUAL',5),
('ul. Velikaya, 37, Zaslavl','+375 (29) 273-26-36','9:00','19:00','GASTRONOME','ACTUAL',5);

INSERT INTO `categories` (`name`, `status`)
VALUES ('PRODUCTS','ACTUAL'),
('HOUSEHOLD_CHEMICALS','ACTUAL'),
('STATIONERY','ACTUAL');

INSERT INTO `subcategories` (`name`, `status`,`categories_id`)
VALUES ('DAIRY_PRODUCTS','ACTUAL',1),
('FRUITS','ACTUAL',1),
('CHICKEN','ACTUAL',1),
('LAUNDRY','ACTUAL',2),
('DISHWASHER','ACTUAL',2),
('KITCHEN','ACTUAL',2),
('NOTEBOOKS','ACTUAL',3),
('STATIONERY','ACTUAL',3),
('FOR_CREATIVITY','ACTUAL',3);

INSERT INTO `goods` (`name`,`producer`,`country`,`description`, `status`,`subcategories_id`)
VALUES ('yogurt','Babushkina krynka','Belarus','130 gr','ACTUAL',1),
('yogurt','Babushkina krynka','Belarus','350 gr','ACTUAL',1),
('yogurt','Bellakt','Belarus','450 gr','ACTUAL',1),
('mango','Egypt','Egypt','Ready to eat','ACTUAL',2),
('apples','ImportFrut','Poland','red','ACTUAL',2),
('apples','Agrokombinat Dzerzhinskij','Belarus','green','ACTUAL',2),
('chicken','Agrokombinat Dzerzhinskij','Belarus','cooled','ACTUAL',3),
('chicken','Agrokombinat Dzerzhinskij','Belarus','frozen','ACTUAL',3),
('chicken','Pticefabrika Druzhba','Belarus','frozen','ACTUAL',3),
('washing powder Color','Ariel','Russia','automat, 6 kg','ACTUAL',4),
('washing powder','Ariel','Russia','automat, 3 kg','ACTUAL',4),
('Washing gel Color','Ariel','Russia','automat, 1,95 l','ACTUAL',4),
('Dishwasher tablets','Somat','Russia','All in One Max, 100 pieses','ACTUAL',5),
('Salt for dishwashers','Somat','Russia','special, 3 kg','ACTUAL',5),
('Salt for dishwashers','Finish','Poland','special, 3 kg','ACTUAL',5),
('Dishwashing liquid','Sorti','Russia','Lemon, 900 ml','ACTUAL',6),
('Dishwashing liquid','Fairy','Russia','Bergamot, 900 ml','ACTUAL',6),
('Dishwashing liquid','Fairy','Russia','Lavender, 450 ml','ACTUAL',6),
('Notebook','Erich Krause','Russia','cell, 80 sheet','ACTUAL',7),
('Notebook','Erich Krause','Russia','cell, 48 sheet','ACTUAL',7),
('Notebook','Dobrushskaya bumazhnaya fabrika','Belarus','cell, 48 sheet','ACTUAL',7),
('Gel pen set Neon','Darvish','Belarus','12 pieses','ACTUAL',8),
('Glitter Gel Pen Set','Darvish','Belarus','6 pieses','ACTUAL',8),
('pen','Darvish','Belarus','black','ACTUAL',8),
('Plasticine wax','Multi-Pulti','Russia','12 pieses','ACTUAL',9),
('Modeling dough','Cvetik','Russia','6 pieses','ACTUAL',9),
('Plasticine wax','Cvetik','Russia','6 pieses','ACTUAL',9);

INSERT INTO `users` (`login`,`password`, `last_name`,`first_name`,`registration_date`,`mobile_number`,`email`,`birthday`, `status`)
VALUES ('anna','$2a$12$JqyaEKjEGydxkF4ILBswYe.PnCHXLaT3fZdbb35LDc2XKnjwrsgry','Крутько','Анна','2022-04-29','+375 (29) 453-23-30','fwu@yandex.ru','1988-07-20','ACTUAL'),
('lena','$2a$12$dTWkMUKXphYSQyuDe2nkluzlNaYHQOofGkllT.vCuiX8fdc45/eVy','Евтухова','Елена','2022-05-12','+375 (33) 483-22-09','evalena@gmail.com','1998-12-02','ACTUAL'),
('genka','$2a$12$4lWIdkpWsVqGgIc.Y2F7nObJ6wVVEeTOgcCEs2MhjKnqH7Xcfi0Dq','Любенчук','Гена','2022-05-15','+375 (29) 812-11-90','genka86@yandex.ru','1986-09-19','ACTUAL'),
('egor','$2a$12$2mhJwXLuUjJOelcAMmJ8fem78s/GpgMBNbIc76PSl47HIN3K6ask.','Печик','Егор','2022-05-16','+375 (33) 732-32-44','edik20@gmail.com','1993-07-05','ACTUAL'),
('natasha','$2a$12$4Zag4kw.QvjAhHODqs3WyePZvDBXGzeKJlC/F10ozyJqbFZGHZBSy','Печик','Наталья','2022-05-16','+375 (29) 823-43-37','pechiknastja@gmail.com','1994-05-17','ACTUAL'),
('admin','$2a$12$stUkDsV2XTWZOjtcyo2iBOj34L8HWRoWVUwVgBw7CVfAtR4xdFTdm','Горчук','Андрей','2022-04-28','+375 (29) 564-45-39','gorinich@gmail.com','1996-09-02','ACTUAL');

INSERT INTO `prices` (`date`,`price`,`status`,`goods_id`,`shops_id`,`users_id`)
VALUES ('2022-04-28',0.94,'HISTORY',1,1,6),
('2022-04-28',2.87,'HISTORY',2,1,6),
('2022-04-28',3.22,'HISTORY',3,1,6),
('2022-04-28',12.99,'ACTUAL',4,1,6),
('2022-04-28',3.54,'ACTUAL',5,1,6),
('2022-04-28',2.94,'ACTUAL',6,1,6),
('2022-04-28',5.39,'HISTORY',7,1,6),
('2022-04-28',5.44,'HISTORY',8,1,6),
('2022-04-28',5.75,'HISTORY',9,1,6),
('2022-04-28',60.35,'ACTUAL',10,1,6),
('2022-04-28',35.85,'ACTUAL',11,1,6),
('2022-04-28',64.95,'ACTUAL',12,1,6),
('2022-04-28',65.90,'ACTUAL',13,1,6),
('2022-04-28',18.55,'ACTUAL',14,1,6),
('2022-04-28',21.30,'ACTUAL',15,1,6),
('2022-04-28',6.45,'ACTUAL',16,1,6),
('2022-04-28',8.12,'ACTUAL',17,1,6),
('2022-04-28',3.20,'ACTUAL',18,1,6),
('2022-04-28',2.30,'ACTUAL',19,1,6),
('2022-04-28',1.35,'ACTUAL',20,1,6),
('2022-04-28',0.99,'ACTUAL',21,1,6),
('2022-04-28',10.15,'ACTUAL',22,1,6),
('2022-04-28',5.99,'HISTORY',23,1,6),
('2022-04-28',0.45,'HISTORY',24,1,6),
('2022-04-28',14.58,'ACTUAL',25,1,6),
('2022-04-28',5.48,'ACTUAL',26,1,6),
('2022-04-28',6.79,'ACTUAL',27,1,6),
('2022-05-01',0.98,'HISTORY',1,1,1),
('2022-05-01',2.92,'HISTORY',2,1,1),
('2022-05-01',3.32,'HISTORY',3,1,1),
('2022-05-01',5.45,'HISTORY',7,1,1),
('2022-05-01',5.50,'HISTORY',8,1,1),
('2022-05-01',5.80,'HISTORY',9,1,1),
('2022-05-14',1.05,'ACTUAL',1,1,2),
('2022-05-14',2.99,'ACTUAL',2,1,2),
('2022-05-14',3.45,'ACTUAL',3,1,2),
('2022-05-16',5.50,'ACTUAL',7,1,3),
('2022-05-16',5.79,'ACTUAL',8,1,2),
('2022-05-18',5.95,'ACTUAL',9,1,4),
('2022-05-18',6.25,'ACTUAL',23,1,2),
('2022-05-18',0.49,'ACTUAL',24,1,4),
('2022-04-28',1.12,'HISTORY',1,3,6),
('2022-04-28',2.59,'HISTORY',2,3,6),
('2022-04-28',3.20,'HISTORY',3,3,6),
('2022-04-28',12.26,'ACTUAL',4,3,6),
('2022-04-28',3.70,'ACTUAL',5,3,6),
('2022-04-28',2.64,'ACTUAL',6,3,6),
('2022-04-28',5.45,'HISTORY',7,3,6),
('2022-04-28',5.78,'HISTORY',8,3,6),
('2022-04-28',5.83,'HISTORY',9,3,6),
('2022-04-28',58.36,'ACTUAL',10,3,6),
('2022-04-28',37.83,'ACTUAL',11,3,6),
('2022-04-28',64.20,'ACTUAL',12,3,6),
('2022-04-28',65.20,'ACTUAL',13,3,6),
('2022-04-28',18.55,'ACTUAL',14,3,6),
('2022-04-28',21.30,'ACTUAL',15,3,6),
('2022-04-28',6.30,'ACTUAL',16,3,6),
('2022-04-28',7.40,'ACTUAL',17,3,6),
('2022-04-28',3.80,'ACTUAL',18,3,6),
('2022-04-28',2.57,'ACTUAL',19,3,6),
('2022-04-28',1.45,'ACTUAL',20,3,6),
('2022-04-28',1.12,'ACTUAL',21,3,6),
('2022-04-28',12.32,'ACTUAL',22,3,6),
('2022-04-28',4.99,'HISTORY',23,3,6),
('2022-04-28',0.40,'HISTORY',24,3,6),
('2022-04-28',11.99,'ACTUAL',25,3,6),
('2022-04-28',6.10,'ACTUAL',26,3,6),
('2022-04-28',6.99,'ACTUAL',27,3,6),
('2022-05-12',1.20,'ACTUAL',1,3,1),
('2022-05-14',2.70,'ACTUAL',2,3,2),
('2022-05-14',3.35,'ACTUAL',3,3,2),
('2022-05-17',5.60,'ACTUAL',7,3,3),
('2022-05-17',5.99,'ACTUAL',8,3,3),
('2022-05-18',5.89,'ACTUAL',9,3,5),
('2022-05-18',5.49,'ACTUAL',23,3,4),
('2022-05-18',0.45,'ACTUAL',24,3,4),
('2022-04-28',1.22,'ACTUAL',1,2,6),
('2022-04-28',2.70,'ACTUAL',2,2,6),
('2022-04-28',3.10,'ACTUAL',3,2,6),
('2022-04-28',3.70,'HISTORY',5,2,6),
('2022-04-28',2.30,'ACTUAL',6,2,6),
('2022-04-28',5.80,'HISTORY',7,2,6),
('2022-04-28',5.25,'ACTUAL',8,2,6),
('2022-04-28',59.65,'ACTUAL',10,2,6),
('2022-04-28',31.20,'HISTORY',11,2,6),
('2022-04-28',65.32,'ACTUAL',12,2,6),
('2022-04-28',6.90,'ACTUAL',16,2,6),
('2022-04-28',7.35,'HISTORY',17,2,6),
('2022-04-28',3.47,'HISTORY',18,2,6),
('2022-04-28',2.20,'ACTUAL',19,2,6),
('2022-04-28',5.20,'ACTUAL',23,2,6),
('2022-04-28',0.35,'HISTORY',24,2,6),
('2022-04-28',6.83,'ACTUAL',26,2,6),
('2022-05-01',3.20,'HISTORY',5,2,1),
('2022-05-01',5.90,'HISTORY',7,2,1),
('2022-05-16',31.90,'HISTORY',11,2,2),
('2022-05-16',0.44,'HISTORY',24,2,2),
('2022-05-16',3.50,'ACTUAL',5,2,4),
('2022-05-16',5.99,'ACTUAL',7,2,4),
('2022-05-16',31.99,'ACTUAL',11,2,4),
('2022-05-16',7.50,'ACTUAL',17,2,5),
('2022-05-17',3.65,'ACTUAL',18,2,5),
('2022-05-17',0.49,'ACTUAL',24,2,3),
('2022-04-28',1.10,'ACTUAL',1,4,6),
('2022-04-28',2.28,'ACTUAL',2,4,6),
('2022-04-28',10.63,'HISTORY',4,4,6),
('2022-04-28',2.90,'ACTUAL',5,4,6),
('2022-04-28',2.20,'ACTUAL',6,4,6),
('2022-04-28',5.16,'ACTUAL',7,4,6),
('2022-04-28',5.90,'ACTUAL',9,4,6),
('2022-04-28',36.84,'ACTUAL',11,4,6),
('2022-04-28',62.80,'HISTORY',13,4,6),
('2022-04-28',16.50,'HISTORY',14,4,6),
('2022-04-28',20.30,'ACTUAL',15,4,6),
('2022-04-28',7.35,'HISTORY',17,4,6),
('2022-04-28',3.92,'ACTUAL',18,4,6),
('2022-04-28',2.20,'HISTORY',19,4,6),
('2022-04-28',1.04,'ACTUAL',21,4,6),
('2022-04-28',11.90,'ACTUAL',22,4,6),
('2022-04-28',0.34,'HISTORY',24,4,6),
('2022-04-28',6.95,'ACTUAL',26,4,6),
('2022-05-08',10.77,'HISTORY',4,4,1),
('2022-05-08',62.85,'HISTORY',13,4,1),
('2022-05-08',2.30,'ACTUAL',19,4,1),
('2022-05-18',10.84,'ACTUAL',4,4,3),
('2022-05-18',62.95,'ACTUAL',13,4,5),
('2022-05-18',16.58,'ACTUAL',14,4,5),
('2022-05-18',7.45,'ACTUAL',17,4,5),
('2022-05-18',0.45,'ACTUAL',24,4,5),
('2022-04-28',1.30,'ACTUAL',1,5,6),
('2022-04-28',2.30,'ACTUAL',2,5,6),
('2022-04-28',3.45,'ACTUAL',3,5,6),
('2022-04-28',3.22,'ACTUAL',5,5,6),
('2022-04-28',2.25,'ACTUAL',6,5,6),
('2022-04-28',5.40,'ACTUAL',8,5,6),
('2022-04-28',5.90,'HISTORY',9,5,6),
('2022-04-28',59.20,'ACTUAL',10,5,6),
('2022-04-28',33.90,'HISTORY',11,5,6),
('2022-04-28',65.10,'ACTUAL',12,5,6),
('2022-04-28',7.10,'ACTUAL',17,5,6),
('2022-04-28',3.10,'ACTUAL',18,5,6),
('2022-04-28',2.90,'ACTUAL',19,5,6),
('2022-04-28',1.72,'ACTUAL',20,5,6),
('2022-04-28',10.30,'ACTUAL',25,5,6),
('2022-04-28',5.40,'ACTUAL',27,5,6),
('2022-05-14',6.10,'ACTUAL',9,5,2),
('2022-05-14',34.05,'ACTUAL',11,5,2),
('2022-04-28',1.15,'ACTUAL',1,6,6),
('2022-04-28',2.30,'ACTUAL',2,6,6),
('2022-04-28',3.45,'ACTUAL',3,6,6),
('2022-04-28',14.00,'ACTUAL',4,6,6),
('2022-04-28',3.90,'ACTUAL',5,6,6),
('2022-04-28',2.99,'ACTUAL',6,6,6),
('2022-04-28',5.90,'ACTUAL',7,6,6),
('2022-04-28',5.90,'ACTUAL',8,6,6),
('2022-04-28',6.12,'ACTUAL',9,6,6),
('2022-04-28',61.05,'ACTUAL',10,6,6),
('2022-04-28',39.25,'ACTUAL',11,6,6),
('2022-04-28',22.00,'ACTUAL',15,6,6),
('2022-04-28',7.30,'ACTUAL',16,6,6),
('2022-04-28',34.20,'ACTUAL',18,6,6),
('2022-04-28',1.45,'ACTUAL',21,6,6),
('2022-04-28',12.20,'HISTORY',22,6,6),
('2022-04-28',5.45,'ACTUAL',23,6,6),
('2022-04-28',0.50,'ACTUAL',24,6,6),
('2022-04-28',14.10,'ACTUAL',25,6,6),
('2022-05-18',12.45,'ACTUAL',22,6,5),
('2022-04-28',1.15,'ACTUAL',1,7,6),
('2022-04-28',2.30,'ACTUAL',2,7,6),
('2022-04-28',3.45,'ACTUAL',3,7,6),
('2022-04-28',3.90,'ACTUAL',5,7,6),
('2022-04-28',2.99,'ACTUAL',6,7,6),
('2022-04-28',5.90,'ACTUAL',7,7,6),
('2022-04-28',5.90,'ACTUAL',8,7,6),
('2022-04-28',6.12,'ACTUAL',9,7,6),
('2022-04-28',61.05,'ACTUAL',10,7,6),
('2022-04-28',39.25,'ACTUAL',11,7,6),
('2022-04-28',22.00,'ACTUAL',15,7,6),
('2022-04-28',7.30,'ACTUAL',16,7,6),
('2022-04-28',1.45,'ACTUAL',21,7,6),
('2022-04-28',0.50,'ACTUAL',24,7,6);

INSERT INTO `ratings` (`assortment`,`quality_of_service`,`prices`,`date`, `status`,`shops_id`,`users_id`)
VALUES (5,5,5,'2022-05-02', 'ACTUAL',1,1),
(5,4,5,'2022-05-16','ACTUAL',1,3),
(4,4,5,'2022-05-16','ACTUAL',1,4),
(5,5,4,'2022-05-02','ACTUAL',2,1),
(3,4,4,'2022-05-14','ACTUAL',2,2),
(5,2,5,'2022-05-17','ACTUAL',2,5),
(5,3,5,'2022-05-18','ACTUAL',2,6),
(5,4,4,'2022-05-14','ACTUAL',3,2),
(3,5,2,'2022-05-17','ACTUAL',3,1),
(3,4,2,'2022-05-18','ACTUAL',3,3),
(5,4,5,'2022-05-16','ACTUAL',4,3),
(3,4,5,'2022-05-16','ACTUAL',4,4),
(3,2,4,'2022-05-07','ACTUAL',5,1),
(5,4,4,'2022-05-17','ACTUAL',5,3),
(4,5,4,'2022-05-18','ACTUAL',5,4),
(5,3,4,'2022-05-18','ACTUAL',5,5),
(4,5,3,'2022-05-15','ACTUAL',6,3),
(4,5,3,'2022-05-18','ACTUAL',6,1),
(4,4,5,'2022-05-18','ACTUAL',6,4),
(2,5,5,'2022-05-18','ACTUAL',6,5),
(5,4,5,'2022-05-10','ACTUAL',7,1),
(3,4,2,'2022-05-15','ACTUAL',7,2),
(4,4,4,'2022-05-16','ACTUAL',7,3),
(5,5,5,'2022-05-17','ACTUAL',7,4),
(4,3,4,'2022-05-17','ACTUAL',7,5);

INSERT INTO `roles` (`id`,`name`)
VALUES (1,'ROLE_ADMIN'),
(2,'ROLE_USER');

INSERT INTO `users_has_roles` (`users_id`,`roles_id`)
VALUES (1,2),
(2,2),
(3,2),
(4,2),
(5,2),
(6,1);