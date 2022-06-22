# Price-monitoring



## Introduction 

This is Maven multi-module project that simulates application for monitoring current prices for goods in the shops of the city of Zaslavl. 

The project was completed as part of the final task at the java courses at the Software Engineering Laboratory Senla.


## Main goals

- [ ] User registration
- [ ] Profile editing
- [ ] Directory of goods categories, directory of shops
- [ ] View a list of products by category. Search and filter
- [ ] Add / update / delete good
- [ ] Linking the price to the good in a particular shop at the current moment
- [ ] Price dynamics for a good for a period
- [ ] Comparison of prices in several shops
- [ ] Price dynamics
- [ ] Import csv file with prices and goods

## Technologies used

* Java 11
* Spring: SpringBoot, MVC, Data JPA, JWT Token Security.
* Maven
* Mysql
* Test: JUnit, Mockito


## Quick start

You need to start the file pricemonitoring.bat

It has three actions:  create a database, run clean + install and jar-file
```
mysql -uroot -p < database/pricemonitoring.sql
call mvn clean install
call java -jar main/target/main-0.0.1-SNAPSHOT.jar
```

You can use [the postman collection](https://www.getpostman.com/collections/5854fdc0f4004c160d35) to make sure the program works correctly now