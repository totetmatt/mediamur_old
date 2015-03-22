
# Initialisation of the application.properties
sed -i s/TWITTER_OAUTHCONSUMERKEY/$TWITTER_OAUTHCONSUMERKEY/ application.properties
sed -i s/TWITTER_OAUTHCONSUMERSECRET/$TWITTER_OAUTHCONSUMERSECRET/ application.properties
sed -i s/TWITTER_OAUTHACCESSTOKEN/$TWITTER_OAUTHACCESSTOKEN/ application.properties
sed -i s/TWITTER_OAUTHACCESSTOKENSECRET/$TWITTER_OAUTHACCESSTOKENSECRET/ application.properties

# Initialisation of the streamquery.yml

if [ -n $STREAM_WORDS ]; then
    echo "words:" >> streamquery.yml 
    echo "    track:" >> streamquery.yml

    for i in $(echo $STREAM_WORDS | tr "," "\n")
    do
      echo "        - $i" >> streamquery.yml
    done
fi


if [ -n $STREAM_USERS ]; then
    echo "users:" >> streamquery.yml
    echo "    follow:" >> streamquery.yml

    for i in $(echo $STREAM_USERS | tr "," "\n")
    do
      echo "        - $i" >> streamquery.yml
    done
fi

if [ -n $STREAM_LOCATIONS ]; then
    echo "location:" >> streamquery.yml
    echo "    place:" >> streamquery.yml

    for i in $(echo $STREAM_LOCATIONS | tr "|" "\n")
    do
      echo "        - $i" >> streamquery.yml
    done
fi
java -jar mediamur.jar