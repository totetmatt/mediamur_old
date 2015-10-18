package mediamur.configuration;

import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "mediamur")
public class MediamurConfiguration {
	private boolean pauseOnHover = false;
	private boolean saveImage = false;
	private int imageScoreLimit = 1;
	
	private String saveDirectory = "./save/" + new Date().getTime();
	
	
	public String getSaveDirectory() {
		return saveDirectory;
	}
	public void setSaveDirectory(String saveDirectory) {
		this.saveDirectory = saveDirectory;
	}
	public boolean isPauseOnHover() {
		return pauseOnHover;
	}
	public void setPauseOnHover(boolean pauseOnHover) {
		this.pauseOnHover = pauseOnHover;
	}
	public boolean isSaveImage() {
		return saveImage;
	}
	public void setSaveImage(boolean saveImage) {
		this.saveImage = saveImage;
	}
	public int getImageScoreLimit() {
		return imageScoreLimit;
	}
	public void setImageScoreLimit(int imageScoreLimit) {
		this.imageScoreLimit = imageScoreLimit;
	}

}
