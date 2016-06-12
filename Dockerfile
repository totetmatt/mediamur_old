FROM java:8
MAINTAINER totetmatt <matthieu.totet@gmail.com>
ENV MEDIAMUR_VERSION 0.4.0
RUN wget https://github.com/totetmatt/mediamur/releases/download/$MEDIAMUR_VERSION/mediamur.jar
CMD mediamur.jar