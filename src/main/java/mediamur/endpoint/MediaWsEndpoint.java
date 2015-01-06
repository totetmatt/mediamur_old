package mediamur.endpoint;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import mediamur.ImageData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import twitter4j.MediaEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Totetmatt Wallpic main wwensocket Endpoint
 */
@ServerEndpoint(value = "/media")
public class MediaWsEndpoint implements StatusListener {

	private Log log = LogFactory.getLog(MediaWsEndpoint.class);

	private HashMap<String, String> urlHash = new HashMap<String, String>();

	// http://stackoverflow.com/questions/18481597/how-to-get-an-existing-websocket-instance
	private static final Set<Session> sessions = Collections
			.synchronizedSet(new HashSet<Session>());

	public static Set<Session> getSessions() {
		return sessions;
	}

	/**
	 * on web socket open. Adding the session to the pool
	 * 
	 * @param session
	 *            Web socket Session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		sessions.add(session);

	}

	/**
	 * on web socket close. Removing the session from the pool
	 * 
	 * @param closing
	 *            session
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		sessions.remove(session);

	}

	private String imhash(String url) throws IOException,
			NoSuchAlgorithmException {
		return org.apache.commons.codec.digest.DigestUtils.md5Hex(new URL(url)
				.openStream());
	}

	public void onStatus(Status arg0) {
		for (MediaEntity me : arg0.getMediaEntities()) {
			if (!urlHash.containsKey(me.getMediaURL())) {
				try {
					urlHash.put(me.getMediaURL(), imhash(me.getMediaURL()));
				} catch (NoSuchAlgorithmException e) {
					log.error(e);
				} catch (IOException e) {
					log.error(e);
				}
			}
			for (Session s : MediaWsEndpoint.getSessions()) {
				ImageData d = new ImageData();
				d.setUrl(me.getMediaURL());
				d.setHash(urlHash.get(me.getMediaURL()));
				d.setId(me.getId() + "");
				ObjectMapper mapper = new ObjectMapper();
				String value;
				try {
					value = mapper.writeValueAsString(d);
					s.getAsyncRemote().sendText(value);
				} catch (JsonProcessingException e) {
					log.error(e);
				}

			}
		}

	}

	public void onTrackLimitationNotice(int arg0) {}

	public void onException(Exception arg0) {}

	public void onDeletionNotice(StatusDeletionNotice arg0) {}

	public void onScrubGeo(long arg0, long arg1) {}

	public void onStallWarning(StallWarning arg0) {}

}