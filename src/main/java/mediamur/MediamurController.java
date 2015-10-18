package mediamur;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mediamur.configuration.MediamurConfiguration;

@Controller
public class MediamurController {

	@Autowired
	MediamurConfiguration mediamurConfiguration;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Map<String, Object> model) {
		model.put("pauseOnHover", mediamurConfiguration.isPauseOnHover());
		model.put("imageScoreLimit", mediamurConfiguration.getImageScoreLimit());
		return "index";
	}
}
