FROM maven:3-alpine

LABEL maintainer="samir.costa@ifce.edu.br"
LABEL version="1.0"

WORKDIR /usr/src/app

COPY pom.xml /usr/src/app/
COPY src /usr/src/app/src

CMD ["mvn", "jetty:run"]

EXPOSE 8083/tcp