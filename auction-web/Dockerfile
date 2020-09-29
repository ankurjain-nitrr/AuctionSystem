FROM adoptopenjdk/openjdk8
RUN apt-get update
RUN apt-get install -y unzip
ADD target/universal/auction-web-1.0-SNAPSHOT.zip auction-web-1.0-SNAPSHOT.zip
RUN unzip auction-web-1.0-SNAPSHOT.zip
RUN rm auction-web-1.0-SNAPSHOT.zip
RUN mv auction-web-1.0-SNAPSHOT /opt/
EXPOSE 9000
ENTRYPOINT [ "bash" ,"/opt/auctio-web-1.0-SNAPSHOT/bin/auction-web" ]
