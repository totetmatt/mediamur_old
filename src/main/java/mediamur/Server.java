package mediamur;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import mediamur.configuration.MediamurConfiguration;
import mediamur.configuration.TwitterConfiguration;
import mediamur.configuration.querystream.QueryStreamConfiguration;
import mediamur.endpoint.MediaWsEndpoint;
import mediamur.endpoint.UserWsEndpoint;
import mediamur.utils.Place;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * @author Totetmatt Main class for Wallpic application
 */
@SpringBootApplication
public class Server extends SpringBootServletInitializer {

	private Log log = LogFactory.getLog(Server.class);

	@Autowired
	private UserWsEndpoint userWsEndpoint;

	@Autowired
	private MediaWsEndpoint mediaWsEndpoint;

	@Autowired
	private TwitterConfiguration twitterConfiguration;

	@Autowired QueryStreamConfiguration queryStreamConfiguration;
	
	@Autowired
	MediamurConfiguration mediamurConfiguration;

	@PostConstruct
	void postConstruct() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(twitterConfiguration.getOAuthConsumerKey());
		cb.setOAuthConsumerSecret(twitterConfiguration.getOAuthConsumerSecret());
		cb.setJSONStoreEnabled(true);
		cb.setDebugEnabled(false);
		cb.setPrettyDebugEnabled(false);
		cb.setOAuthAccessToken(twitterConfiguration.getOAuthAccessToken());
		cb.setOAuthAccessTokenSecret(twitterConfiguration.getOAuthAccessTokenSecret());
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		
		twitterStream.addListener(mediaWsEndpoint);
		twitterStream.addListener(userWsEndpoint);

		if (queryStreamConfiguration.getFirehose().isUseFirehose()) {
			log.info("Use Firehose");
			twitterStream.firehose(queryStreamConfiguration.getFirehose().getCount());

		} else if (queryStreamConfiguration.getLinkstream().isUseLinkStream()) {
			log.info("Use Link Stream");
			twitterStream.links(queryStreamConfiguration.getLinkstream().getCount());

		} else if (queryStreamConfiguration.getSitestream().isUseSiteStream()) {
			log.info("Use Site Stream");
			twitterStream.site(queryStreamConfiguration.getSitestream().isWithFollowings(),
					queryStreamConfiguration.filterQueryUsers());

		} else if (queryStreamConfiguration.getSampleStream().isUseSampleStream()) {
			log.info("Use Sample Stream");
			twitterStream.sample();

		} else {
			log.info("Use Filter");
			FilterQuery fq = new FilterQuery();
			if (!queryStreamConfiguration.getWords().isEmpty()) {
				fq.track(queryStreamConfiguration.filterQueryWords());
			}
			if (!queryStreamConfiguration.getUsers().isEmpty()) {
				fq.follow(queryStreamConfiguration.filterQueryUsers());
			}
			if (!queryStreamConfiguration.getLocations().isEmpty()){
				for(Place p:queryStreamConfiguration.getLocations()){
					log.info(p.getSouthWestLong()+" "+p.getSouthWestLat());
				}
				fq.locations(queryStreamConfiguration.filterLocations());
			}
			twitterStream.filter(fq);
		}
		if(mediamurConfiguration.isSaveMedia()) {
			createImageSaveDirectory(mediamurConfiguration.getSaveDirectory());
		}
	
	}

	public void createImageSaveDirectory(String path){
		File theDir = new File(path);
		if(!theDir.exists()) {
			theDir.mkdirs();
		}
	}
	public static void main(String[] args) throws Exception {
		SpringApplication.run(new Object[] { Server.class }, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(Server.class);
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

}