FROM maven:3-alpine

LABEL maintainer="samir.costa@ifce.edu.br"
LABEL version="1.0"

ADD pom.xml .
ADD src .

CMD ["mvn", "jetty:run"]

EXPOSE 8083/tcp