package mediamur;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mediamur.configuration.MediamurConfiguration;

@Controller
public class MediamurController implements ErrorController {

	@Autowired
	MediamurConfiguration mediamurConfiguration;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Map<String, Object> model) {
		model.put("pauseOnHover", mediamurConfiguration.isPauseOnHover());
		model.put("imageScoreLimit", mediamurConfiguration.getImageScoreLimit());
		return "index";
	}

	@RequestMapping(value = "/error")
	public String handleError(HttpServletRequest req) throws IOException {

		return "redirect:/";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}
}
