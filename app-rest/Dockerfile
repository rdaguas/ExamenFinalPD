# Para loguearame: docker login -u rdaguas
# docker build -t rdaguas/app-rest .
# docker push rdaguas/app-rest

FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21

ENV LANGUAGE='en_US:en'

RUN mkdir /app
WORKDIR /app

COPY build/quarkus-app/lib ./lib
COPY build/quarkus-app/quarkus ./quarkus
COPY build/quarkus-app/app ./app
COPY build/quarkus-app/quarkus-run.jar app.jar

CMD ["java", "-jar", "app.jar"]

#docker run --rm -it --entrypoint /bin/bash app-rest
#docker run app-rest
