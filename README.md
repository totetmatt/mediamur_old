# Mediamur
Server & Simple Html Client to display media from a Twitter Stream.

#How to ?
## Working in local
###Run the Server
Grab the Mediamur jar at [http://matthieu-totet.fr/release/mediamur/mediamur-0.0.1-RELEASE.jar](http://matthieu-totet.fr/release/mediamur/mediamur-0.0.1-RELEASE.jar "http://matthieu-totet.fr/release/mediamur/mediamur-0.0.1-RELEASE.jar") or build it.

Launch it throught the command `java -jar mediamur-0.0.1-RELEASE.jar`

The in-the-box configuration doesn't have stream filter and will use the sample stream api from twitter. Check the **streamquery.yml** part bellow to see how to have a filtered stream.

It should have open a websocket entrypoint accessible at `http://localhost:8080/mediastream`


### Run the Client
The client is just an html page. You can just open it in you web browser by double clicking.


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
            -Name;SouthWestLatitude,SouthWestLongitude;NorthEastLatitude,NorthEastLongitude
  


##Working in remote
If you need that other people access to your stream, you'll need to makes some changes.

*There will be some advise given, but please adapt the solution to your problem.*
###Run the Server
Nothing to change execpt that the entrypoint will be `http://<yourHostName>:8080/mediastream`.

You should make sure also that the `http://<yourHostName>:8080/mediastream` is accessible from where the other users will be (internet, local network etc..)

### Run the Client
You will need to serve the html page via a html web server (nginx, apache , python SimpleHTTPServer module  etc... ).

Just make sure that in this line in the `index.html `:

    socket = new WebSocket("<url>");

the `<url>` target the server that you launched. ( `http://<yourHostName>:8080/mediastream` e.g )

#Dockerized version
See [https://github.com/totetmatt/mediamur-docker](https://github.com/totetmatt/mediamur-docker)