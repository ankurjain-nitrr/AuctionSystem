FROM adoptopenjdk/openjdk8

RUN apt-get update
RUN apt-get install -y unzip wget

ADD glowroot-conf ./glowroot-conf
RUN wget https://github.com/glowroot/glowroot/releases/download/v0.13.6/glowroot-0.13.6-dist.zip
RUN unzip glowroot-0.13.6-dist.zip
RUN rm glowroot-0.13.6-dist.zip
RUN cp glowroot/glowroot.jar .
RUN cp -r glowroot/lib .

ADD target/universal/auction-web-1.0-SNAPSHOT.zip auction-web-1.0-SNAPSHOT.zip
RUN unzip auction-web-1.0-SNAPSHOT.zip
RUN rm auction-web-1.0-SNAPSHOT.zip
RUN mv auction-web-1.0-SNAPSHOT /opt/

EXPOSE 9000
EXPOSE 4000

ENTRYPOINT [ "bash" ,"/opt/auction-web-1.0-SNAPSHOT/bin/auction-web" ]
