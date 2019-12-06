package org.kettle.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	 * @return /main
	 */
	@RequestMapping("/main.shtml")
	public String mainWeb() {
		return "main";
	}

	//=============================================资源库=================================================//
	/**
	 * 资源库列表页面
	 *
	 * @return /repository/list
	 */
	@RequestMapping("/repository/list.shtml")
	public String repositoryListWeb() {
		return "repository/list";
	}

	/**
	 * 资源库添加页面
	 *
	 * @return /repository/add
	 */
	@RequestMapping("/repository/add.shtml")
	public String repositoryAddWeb() {
		return "repository/add";
	}

	/**
	 * 资源库编辑页面
	 *
	 * @return /repository/edit
	 */
	@RequestMapping("/repository/edit.shtml")
	public String repositoryEditWeb(Integer repositoryId, Model model) {
		model.addAttribute("repositoryId", repositoryId);
		return "repository/edit";
	}

	//=============================================定时策略=================================================//
	/**
	 * 定时策略列表页面
	 *
	 * @return /quartz/list
	 */
	@RequestMapping("/quartz/list.shtml")
	public String quartzListWeb() {
		return "quartz/list";
	}

	/**
	 * 定时策略添加页面
	 *
	 * @return /quartz/add
	 */
	@RequestMapping("/quartz/add.shtml")
	public String quartzAddWeb() {
		return "quartz/add";
	}

	/**
	 * 定时策略编辑页面
	 *
	 * @return /quartz/edit
	 */
	@RequestMapping("/quartz/edit.shtml")
	public String quartzEditWeb(Integer quartzId, Model model) {
		model.addAttribute("quartzId", quartzId);
		return "quartz/edit";
	}

	//=============================================用户管理=================================================//
	/**
	 * 用户管理列表页面
	 *
	 * @return /user/list
	 */
	@RequestMapping("/user/list.shtml")
	public String userListWeb() {
		return "user/list";
	}

	/**
	 * 用户管理添加页面
	 *
	 * @return /user/add
	 */
	@RequestMapping("/user/add.shtml")
	public String userAddWeb() {
		return "user/add";
	}

	/**
	 * 用户管理编辑页面
	 *
	 * @return /user/edit
	 */
	@RequestMapping("/user/edit.shtml")
	public String userEditWeb(Integer userId, Model model) {
		model.addAttribute("userId", userId);
		return "user/edit";
	}

	//=============================================任务分类=================================================//
	/**
	 * 任务分类列表页面
	 *
	 * @return /category/list
	 */
	@RequestMapping("/category/list.shtml")
	public String categoryListWeb() {
		return "category/list";
	}

	/**
	 * 任务分类添加页面
	 *
	 * @return /category/add
	 */
	@RequestMapping("/category/add.shtml")
	public String categoryAddWeb() {
		return "category/add";
	}

	/**
	 * 任务分类编辑页面
	 *
	 * @return /category/edit
	 */
	@RequestMapping("/category/edit.shtml")
	public String categoryEditWeb(Integer categoryId, Model model) {
		model.addAttribute("categoryId", categoryId);
		return "category/edit";
	}

	//=============================================转换管理=================================================//
	/**
	 * 转换管理列表页面
	 *
	 * @return /trans/list
	 */
	@RequestMapping("/trans/list.shtml")
	public String transListWeb() {
		return "trans/list";
	}

	/**
	 * 转换管理添加页面
	 *
	 * @return /trans/add
	 */
	@RequestMapping("/trans/add.shtml")
	public String transAddWeb() {
		return "trans/add";
	}

	/**
	 * 转换管理编辑页面
	 *
	 * @return /trans/edit
	 */
	@RequestMapping("/trans/edit.shtml")
	public String transEditWeb(Integer transId, Model model) {
		model.addAttribute("transId", transId);
		return "trans/edit";
	}
}
