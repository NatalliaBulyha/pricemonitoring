mysql -uroot -p < database/pricemonitoring.sql
pause
call mvn clean install
pause
call java -jar main/target/main-0.0.1-SNAPSHOT.jar
pause