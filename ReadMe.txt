Spring Boot version : 2.4.0

curl -v http://localhost:8080/flux-datetime?timezones=est,utc

curl -v http://localhost:8080/fibonacci

curl -v http://localhost:8080/sse

-- Maven Run Tests & Generate HTML Report
mvn clean surefire-report:report

-- Maven package
mvn clean package -DskipTests=true

-- Docker
docker images
docker image rm time-service -f

docker container ls
docker stop time-service -f

docker build -t time-service .
docker run -p 8080:8080 time-service


