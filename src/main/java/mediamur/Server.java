package mediamur;


import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.websocket.Session;

import mediamur.configuration.StreamQueryConfiguration;
import mediamur.configuration.TwitterConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import twitter4j.FilterQuery;
import twitter4j.MediaEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Totetmatt
 * Main class for Wallpic application
 */
@ComponentScan
@Controller
@EnableAutoConfiguration
public class Server extends SpringBootServletInitializer implements StatusListener  {
	
	private Log log = LogFactory.getLog(Server.class);
	
	@Autowired
	private TwitterConfiguration twitterConfiguration;

	@Autowired
	private StreamQueryConfiguration streamConfiguration;
	@PostConstruct
	void postConstruct(){
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(twitterConfiguration.getOAuthConsumerKey());
		cb.setOAuthConsumerSecret(twitterConfiguration.getOAuthConsumerSecret());
		cb.setJSONStoreEnabled(true);
		cb.setDebugEnabled(false);
		cb.setPrettyDebugEnabled(false);
		cb.setOAuthAccessToken(twitterConfiguration.getOAuthAccessToken());
		cb.setOAuthAccessTokenSecret(twitterConfiguration.getOAuthAccessTokenSecret());
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();
		twitterStream.addListener(this);
		FilterQuery fq = new FilterQuery();
		if(streamConfiguration.isEmpty()) {
			log.info("Empty query, use sample");
			twitterStream.sample();
		} else {
			log.info("Non Empty Search");
			fq = new FilterQuery();
			fq.track(streamConfiguration.generateWordsTrack());
			fq.follow(streamConfiguration.generateFollowTrack());
			
			fq.locations(streamConfiguration.generateLocation());
			
			twitterStream.filter(fq);
		}
	}
	
   
    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{Server.class}, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Server.class);
    }
   
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

	public void onException(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private String imhash(String url) throws IOException, NoSuchAlgorithmException {
		return org.apache.commons.codec.digest.DigestUtils.md5Hex(new URL(url).openStream());
	}
	
	HashMap <String,String> urlHash = new HashMap<String, String>();
	public void onStatus(Status arg0) {
		for (MediaEntity me : arg0.getMediaEntities()) {
			if (! urlHash.containsKey(me.getMediaURL())) {
				try {
					urlHash.put(me.getMediaURL(), imhash(me.getMediaURL()));
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for (Session s : WebsocketEndpoint.getSessions()) {
				ImageData d = new ImageData();
				d.setUrl(me.getMediaURL());
				d.setHash(urlHash.get(me.getMediaURL()));
				d.setId(me.getId()+"");
				 ObjectMapper mapper = new ObjectMapper();
		            String value;
					try {
						value = mapper.writeValueAsString(d);
						s.getAsyncRemote().sendText(value);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		}
		
	}

	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}
}