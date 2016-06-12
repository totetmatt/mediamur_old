FROM java:8
MAINTAINER @totetmatt <matthieu.totet@gmail.com>

RUN git clone https://github.com/totetmatt/mediamur.git
WORKSPACE /mediamur