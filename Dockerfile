FROM dockerfile/java
MAINTAINER @totetmatt <matthieu.totet@gmail.com>

VOLUME /var/www/html

EXPOSE 8080

RUN wget -O /data/mediamur.jar https://github.com/totetmatt/mediamur/releases/download/v0.1.1/mediamur-0.1.1.jar

COPY init.sh /data/init.sh
COPY application.properties.sample /data/application.properties
COPY streamquery.yml /data/streamquery.yml
COPY html/index.html /var/www/html/index.html
COPY html/minimal.css /var/www/html/minimal.css
RUN chmod +x /data/init.sh

ENTRYPOINT /data/init.sh
