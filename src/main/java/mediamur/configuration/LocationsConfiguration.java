package mediamur.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(locations = { "file:./streamquery.yml" }, prefix = "location")
public class LocationsConfiguration {

	private List<String> place = new ArrayList<String>();

	public List<String> getPlace() {
		return place;
	}

	public void setPlace(List<String> place) {
		this.place = place;
	}

}
