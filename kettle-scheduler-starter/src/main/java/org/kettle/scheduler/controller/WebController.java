package org.kettle.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面访问层
 *
 * @author lyf
 */
@Controller
@RequestMapping("/web")
public class WebController {

    @RequestMapping("/login.shtml")
	public String loginWeb() {
		return "login";
	}

	@RequestMapping("/index.shtml")
	public String indexWeb() {
		return "index";
	}
}
