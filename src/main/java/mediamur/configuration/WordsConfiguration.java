package mediamur.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(locations = { "file:./streamquery.yml" }, prefix = "words")
public class WordsConfiguration {

	private List<String> track = new ArrayList<String>();

	public List<String> getTrack() {
		return track;
	}

	public void setTrack(List<String> track) {
		this.track = track;
	}

}
