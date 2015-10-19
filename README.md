# Mediamur
Server & Simple Html Client to display media from a Twitter Stream.
![Mediamur Example ](./doc/MediaMur.gif)
# How to ?
## Configuration
### application.properties
| Property                       | Type    | Comment        |
|------------------------------ | ------- | --------------- |
| twitter.OAuthConsumerKey      | String  |Your Consumer Key|
| twitter.OAuthConsumerSecret   | String  |Your Consumer Secret |
| twitter.OAuthAccessToken      | String  |Your Access Token |
| twitter.OAuthAccessTokenSecret| String  | Your Access Token Secret |
| mediamur.pauseOnHover         | Boolean | When cursor is on picture, stop the scrolling of incoming images (can be changed on UI after) |
| mediamur.imageScoreLimit      | Integer | Minimum image score before displayed (can be changed on UI after)|
| mediamur.saveImage            | Boolean | Save the image in the filesystem |
| mediamur.saveDirectory        | String  | Path to store the images if saveImage is enabled |

### streamquery.yml
```
words:
    - words1
    - words2
users:
    - {"screenName":aUser}
    - {"screenName":anotherUser}
locations:
    - {"southWestLat":40.756761 ,"southWestLong":-73.989996 ,"northEastLat":40.762039,"northEastLong":-73.971516} 
sampleStream: {useSampleStream: false}
```

## Working in local
### Run the Server
Grab the latest Mediamur zip at [https://github.com/totetmatt/mediamur/releases](https://github.com/totetmatt/mediamur/releases "https://github.com/totetmatt/mediamur/releases").

Open `application.properties` and put your Twitter oAuth crendential. 

Unzip and launch via `start_mediamur.sh` (Unix/Mac) or `start_mediamur.bat`

The in-the-box configuration doesn't have stream filter and will use the sample stream api from twitter. Modify the **streamquery.yml** to have a filtered stream with custom search.

### Run the Client
Open your web browser and go to `http://localhost:8080/`.

#Dockerized version
Soon