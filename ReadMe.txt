Spring Boot version : 2.4.0

curl -v http://localhost:8080/web-datetime?timezones=est,utc

-- Maven Run Tests & Generate HTML Report
mvn clean surefire-report:report

-- Maven package
mvn clean package -DskipTests=true

-- Docker
docker images
docker image rm spring-boot-docker -f

docker container ls
docker stop spring-boot-docker

docker build -t spring-boot-docker .
docker run -p 8080:8080 spring-boot-docker
