package mediamur.endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mediamur.ImageData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * 
 * @author Totetmatt Wallpic main wwensocket Endpoint
 */
@ServerEndpoint(value = "/user")
public class UserWsEndpoint implements StatusListener {
	
	private Log log = LogFactory.getLog(UserWsEndpoint.class);
	
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


	public void onStatus(Status status) {
		for (Session s : UserWsEndpoint.getSessions()) {
			ImageData d = new ImageData();
			d.setId(status.getUser().getScreenName());
			d.setHash(status.getUser().getOriginalProfileImageURL());
			d.setUrl(status.getUser().getOriginalProfileImageURL());
			d.setLabel(status.getUser().getScreenName());
			ObjectMapper mapper = new ObjectMapper();
			String value;
			try {
				value = mapper.writeValueAsString(d);
				s.getAsyncRemote().sendText(value);
			} catch (JsonProcessingException e) {
				log.error(e);
			}
			if (status.getRetweetedStatus() != null) {

				d = new ImageData();
				d.setId(status.getRetweetedStatus().getUser().getScreenName());
				d.setHash(status.getRetweetedStatus().getUser()
						.getOriginalProfileImageURL());
				d.setUrl(status.getRetweetedStatus().getUser()
						.getOriginalProfileImageURL());
				d.setLabel(status.getRetweetedStatus().getUser()
						.getScreenName());
				mapper = new ObjectMapper();
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