FROM maven:3-jdk-8
MAINTAINER @totetmatt <matthieu.totet@gmail.com>

RUN git clone https://github.com/totetmatt/mediamur.git
WORKDIR /mediamur
# RUN git describe --abbrev=1 --tags | cut -d '-' -f1-2
RUN mvn package
RUN mv /mediamur/target/mediamur-$(git describe --abbrev=1 --tags | cut -d '-' -f1-2).jar /mediamur/target/mediamur.jar
