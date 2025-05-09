FROM gradle:8.12.1-jdk17 as build
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"
WORKDIR /usr/src/app
COPY . .
ENTRYPOINT ["sleep"]
CMD ["3000"]
RUN ./gradlew build --info

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]