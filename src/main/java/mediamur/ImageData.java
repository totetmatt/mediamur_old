package mediamur;
/**
 * 
 * @author Totetmatt
 * Base class to send image information throught websocket endpoint.
 */
public class ImageData {
	String url;
	String id;
	String hash;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
}
