package mediamur;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class MediamurController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(){
		return "index";
	}
}
