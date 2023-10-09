FROM openjdk:8
EXPOSE 8080
ADD target/auth-course-longvtd-0.0.1-SNAPSHOT.war auth-course-longvtd-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java", "-war","auth-course-longvtd-0.0.1-SNAPSHOT.war"]