# Mediamur
Server & Simple Html Client to display media from a Twitter Stream.

#TL;DR
##Run the Server
Grab the Mediamur zip here or build it.
Launch it throught the command java -jar mediamur.jar
It should have open a websocket entrypoint accessible at http://<yourHostName>:8080/mediastream
## Run the Client
The client is just an html page.  You just need to serve it via a simple html web server or if you're running locally, just open it in you web browser.
Just make sure that in this line :

    socket = new WebSocket("<url>");

the <url> target the server that you launched. ( http://<yourHostName>:8080/mediastream e.g )
#Streamquery.yml
By default, Streamquery is empty and the server will use the sample stream api. You can filter the stream by :


    words:
        track:
            - something
            - to
            - lookfor
    user:
        follow:
            - totetmatt
            - twitterdev
    location:
        place:
            -Name;SouthWestLatitude,SouthWestLongitude;NorthEastLatitude,NorthEastLongitude
  


