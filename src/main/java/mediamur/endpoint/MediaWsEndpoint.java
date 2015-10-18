package mediamur.endpoint;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mediamur.ImageData;
import mediamur.configuration.MediamurConfiguration;
import twitter4j.MediaEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * 
 * @author Totetmatt Wallpic main wwensocket Endpoint
 */
@ServerEndpoint(value = "/media")
public class MediaWsEndpoint implements StatusListener {

	private Log log = LogFactory.getLog(MediaWsEndpoint.class);

	private HashMap<String, String> urlHash = new HashMap<String, String>();

	@Autowired
	MediamurConfiguration mediamurConfiguration;

	// http://stackoverflow.com/questions/18481597/how-to-get-an-existing-websocket-instance
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

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

	public void onStatus(Status status) {
		for (MediaEntity me : status.getMediaEntities()) {
			if (!urlHash.containsKey(me.getMediaURL())) {
				try {

					URL imageUrl = new URL(me.getMediaURL() + ":large");

					URLConnection connectionBig = imageUrl.openConnection();
					InputStream imageData = connectionBig.getInputStream();

					// Free MD5 from Twitter
					urlHash.put(me.getMediaURL() + ":large", connectionBig.getHeaderField("Content-MD5"));
					if (mediamurConfiguration.isSaveImage()) {
						String fileDestination = FilenameUtils.concat(mediamurConfiguration.getSaveDirectory(),
								FilenameUtils.getName(me.getMediaURL()));
						OutputStream out = new BufferedOutputStream(new FileOutputStream(fileDestination));

						for (int b; (b = imageData.read()) != -1;) {
							out.write(b);
						}
						out.close();
						imageData.close();
					}

				} catch (IOException e) {
					log.error(e);
				}
			}
			for (Session s : MediaWsEndpoint.getSessions()) {
				ImageData d = new ImageData();
				d.setUrl(me.getMediaURL()+ ":large");
				d.setHash(urlHash.get(me.getMediaURL()+ ":large"));
				d.setId(me.getId() + "");
				d.setLabel(status.getText());
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

	public void onTrackLimitationNotice(int arg0) {
	}

	public void onException(Exception arg0) {
	}

	public void onDeletionNotice(StatusDeletionNotice arg0) {
	}

	public void onScrubGeo(long arg0, long arg1) {
	}

	public void onStallWarning(StallWarning arg0) {
	}

}