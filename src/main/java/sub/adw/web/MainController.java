package sub.adw.web;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class MainController {

	@RequestMapping(method = RequestMethod.GET, value = "/test2")
	@ResponseBody
	public ResponseEntity<?> getTest() {
		return ResponseEntity.ok().body("some test 2");
	}

	@RequestMapping(value = "/")
	public String index(Model model) throws Exception {


		return "index";
	}

}
