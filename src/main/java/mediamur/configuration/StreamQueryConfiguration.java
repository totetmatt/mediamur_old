package mediamur.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mediamur.utils.UserIdResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@ComponentScan
public class StreamQueryConfiguration {

	private List<String> words = new ArrayList<String>();
	private Log log = LogFactory.getLog(StreamQueryConfiguration.class);
	private Map<String, Long> users = new HashMap<String, Long>();
	private Map<String, List<Double[]>> locations = new HashMap<String, List<Double[]>>();

	public boolean isEmpty() {
		return words.isEmpty() && users.isEmpty() && locations.isEmpty();
	}
	
	public Map<String, List<Double[]>> getLocations() {
		return locations;
	}

	public void setLocations(Map<String, List<Double[]>> locations) {
		this.locations = locations;
	}

	public StreamQueryConfiguration(Map<String, List<Double[]>> locations) {
		super();
		this.locations = locations;
	}

	public List<String> getWords() {
		return words;
	}

	public Map<String, Long> getUsers() {
		return users;
	}

	@Autowired
	public StreamQueryConfiguration(WordsConfiguration wordsConfig,
			UsersConfiguration userConfig,
			LocationsConfiguration locationsConfig) {
		words.addAll(wordsConfig.getTrack());

		for (String user : userConfig.getFollow()) {
			log.info(user);
			String[] sUser = user.split(",");
			if (sUser.length == 2) {
				users.put(sUser[0], Long.parseLong(sUser[1]));
			} else {
				log.info(user + " don't have id, trying to grab it");
				try {
					long id = UserIdResolver.resolve(user);
					users.put(user, id);
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		for (String place : locationsConfig.getPlace()) {
			String[] sPlace = place.split(";");
			if (sPlace.length != 3) {
				log.warn("Incorrect place definiton");

			} else {
				String[] sswPoint = sPlace[1].split(",");
				Double[] swPoint = { Double.parseDouble(sswPoint[1]),
						Double.parseDouble(sswPoint[0]) };

				String[] snePoint = sPlace[2].split(",");
				Double[] nePoint = { Double.parseDouble(snePoint[1]),
						Double.parseDouble(snePoint[0]) };

				List<Double[]> l = new ArrayList<Double[]>();
				l.add(swPoint);
				l.add(nePoint);

				locations.put(sPlace[0], l);
			}
		}
	}

	public String[] generateWordsTrack() {
		String[] track = new String[words.size()];
		words.toArray(track);
		return track;
	}

	public double[][] generateLocation() {
		double[][] loc = new double[locations.keySet().size() * 2][2];
		int i = 0;
		for (Entry<String, List<Double[]>> e : locations.entrySet()) {
			for (Double[] f : e.getValue()) {
				loc[i] = new double[] {f[0],f[1]};
				i++;
				log.info(e.getKey()+" > "+f);
			}

		}
		return loc;
	}

	public long[] generateFollowTrack() {
		List<Long> trackableUser = new ArrayList<Long>();

		for (Entry<String, Long> entrySet : users.entrySet()) {
			if (entrySet.getValue() != 0) {
				trackableUser.add(entrySet.getValue());
			}
		}
		long[] follow = new long[trackableUser.size()];
		for (int i = 0; i < trackableUser.size(); i++) {
			follow[i] = trackableUser.get(i);
		}
		return (follow);

	}

}
