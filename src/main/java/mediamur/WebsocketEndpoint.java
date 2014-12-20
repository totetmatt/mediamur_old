package mediamur;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
/**
 * 
 * @author Totetmatt
 *	Wallpic main wwensocket Endpoint
 */
@ServerEndpoint(value = "/mediastream")
public class WebsocketEndpoint {
	
	//http://stackoverflow.com/questions/18481597/how-to-get-an-existing-websocket-instance
	private static final Set<Session> sessions = 
	          Collections.synchronizedSet(new HashSet<Session>());
	
	public static Set<Session> getSessions() {
		return sessions;
	}

	/**
	 * on web socket open. Adding the session to the pool
	 * @param session Web socket Session
	 * @throws IOException
	 */
	@OnOpen
    public void onOpen(Session session) throws IOException {
        sessions.add(session);
  
    }

	/**
	 * on web socket close. Removing the session from the pool
	 * @param closing session
	 * @throws IOException
	 */
	@OnClose
	 public void onClose(Session session) throws IOException {
        sessions.remove(session);
 
    }
 
}