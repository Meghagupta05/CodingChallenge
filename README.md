# CodingChallenge
Download the code from master branch.
How to Run Project : we can run this project in following ways:
  1. java -jar telecomSampleModule-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
      This jar is runnable so it will start a server on port 8080. After this we stat using services. 
  2. import project in eclipse or any otherid which support java. Then go to class VertxServer this class contain main method. So we can        run project from it as java application.
 
## API
This project runs on port 8080 so basic url will be http://localhost:8080:
 1. Url http://localhost:8080/allcustomers will give you all customers we have
 2. Url http://localhost:8080/allcontacts will give list of all phenom numbers
 3. Url http://localhost:8080//contacts/<name of customer> get all phone numbers of a single customer
 4. Url http://localhost:8080/activate/contact/<phone number> activate  phone number
 
## Tech used in this project : Java8 , vert.x , Maven 
