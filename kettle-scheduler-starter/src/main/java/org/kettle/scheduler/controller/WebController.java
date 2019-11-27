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

	/**
	 * 请求登录页面
	 *
	 * @return /login
	 */
    @RequestMapping("/login.shtml")
	public String loginWeb() {
		return "login";
	}

	/**
	 * 登录成功后跳转的页面
	 *
	 * @return /index
	 */
	@RequestMapping("/index.shtml")
	public String indexWeb() {
		return "index";
	}

	/**
	 * 首页统计页面
	 *
	 * @return /index
	 */
	@RequestMapping("/main.shtml")
	public String mainWeb() {
		return "main";
	}

	//=============================================资源库=================================================//
	/**
	 * 资源库列表页面
	 *
	 * @return /index
	 */
	@RequestMapping("/repository/list.shtml")
	public String repositoryListWeb() {
		return "repository/list";
	}

	/**
	 * 资源库添加页面
	 *
	 * @return /index
	 */
	@RequestMapping("/repository/add.shtml")
	public String repositoryAddWeb() {
		return "repository/add";
	}

	/**
	 * 资源库编辑页面
	 *
	 * @return /index
	 */
	@RequestMapping("/repository/edit.shtml")
	public String repositoryEditWeb() {
		return "repository/edit";
	}
}
