curl -v http://localhost:8080/web/file/nio?uri=https://www.w3.org/TR/PNG/iso_8859-1.txt
curl -v http://localhost:8080/web/file/nio?uri=http://www.sci.utah.edu/~macleod/docs/txt2html/sample.txt
curl -v http://localhost:8080/web/file/nio?uri=http://ipv4.download.thinkbroadband.com/5MB.zip > output.zip
curl -v http://localhost:8080/web/file/nio?uri=http://ipv4.download.thinkbroadband.com/512MB.zip > output.zip

curl -v --connect-timeout 1 -m 100 http://localhost:8080/web/file/nio?size=512

    private static final String TEXT_FILE_URL = "http://www.sci.utah.edu/~macleod/docs/txt2html/sample.txt";
    private static final String ZIP_FILE_URL = "http://ipv4.download.thinkbroadband.com/5MB.zip";
    private static final String LOCATION = TEXT_FILE_URL;
