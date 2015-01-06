package mediamur;

import javax.annotation.PostConstruct;

import mediamur.configuration.StreamQueryConfiguration;
import mediamur.configuration.TwitterConfiguration;
import mediamur.endpoint.MediaWsEndpoint;
import mediamur.endpoint.UserWsEndpoint;

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
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * @author Totetmatt Main class for Wallpic application
 */
@ComponentScan
@Controller
@EnableAutoConfiguration
public class Server extends SpringBootServletInitializer {

	private Log log = LogFactory.getLog(Server.class);
	
	@Autowired
	private UserWsEndpoint userWsEndpoint;

	@Autowired
	private MediaWsEndpoint mediaWsEndpoint;

	@Autowired
	private TwitterConfiguration twitterConfiguration;

	@Autowired
	private StreamQueryConfiguration streamConfiguration;

	@PostConstruct
	void postConstruct() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(twitterConfiguration.getOAuthConsumerKey());
		cb.setOAuthConsumerSecret(twitterConfiguration.getOAuthConsumerSecret());
		cb.setJSONStoreEnabled(true);
		cb.setDebugEnabled(false);
		cb.setPrettyDebugEnabled(false);
		cb.setOAuthAccessToken(twitterConfiguration.getOAuthAccessToken());
		cb.setOAuthAccessTokenSecret(twitterConfiguration
				.getOAuthAccessTokenSecret());
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();
		twitterStream.addListener(mediaWsEndpoint);
		twitterStream.addListener(userWsEndpoint);
		FilterQuery fq = new FilterQuery();
		if (streamConfiguration.isEmpty()) {
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
		SpringApplication.run(new Object[] { Server.class }, args);
	}

	@Override
	protected SpringApplicationBuilder configure(
			final SpringApplicationBuilder application) {
		return application.sources(Server.class);
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

}