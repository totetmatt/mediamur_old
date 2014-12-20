package mediamur.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(locations = { "file:./streamquery.yml" }, prefix = "users")
public class UsersConfiguration {

	List<String> follow = new ArrayList<String>();

	public List<String> getFollow() {
		return follow;
	}

	public void setFollow(List<String> follow) {
		this.follow = follow;
	}

}
