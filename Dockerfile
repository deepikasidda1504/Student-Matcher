FROM tomcat:9.0-jdk8

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY . /usr/local/tomcat/webapps/ROOT

RUN javac -cp "/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*" \
    -d "/usr/local/tomcat/webapps/ROOT/WEB-INF/classes" \
    /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/*.java

EXPOSE 8080

CMD ["catalina.sh", "run"]