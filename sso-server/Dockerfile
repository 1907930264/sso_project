FROM java:8
COPY target/sso-server-0.0.1-SNAPSHOT.jar app.jar
CMD ["--server.port=88"]
EXPOSE 88
ENTRYPOINT ["java","-jar","/app.jar"]