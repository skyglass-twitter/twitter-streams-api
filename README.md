# Running the application
- Please enter the correct credentials in twitter4j.properties file.
- Then run mvn install -DskipTests command
- Check the docker-compose folder and services.yml file that includes both config server and twitter to kafka service
- Then go to docker-compose folder and run docker-compose up command to run kafka cluster and twitter-to-kafka-service 