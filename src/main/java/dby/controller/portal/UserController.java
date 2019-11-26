package dby.controller.portal;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.pojo.User;
import com.mmall.util.MD5Util;

import ch.qos.logback.core.joran.conditional.IfAction;
import dby.common.ServerResponse;
import dby.service.IUserService;

/**
 * 
 * Title: UserController Description:
 * 
 * @author 董博源
 * @date 2019/11/25 10:27:26
 */
@Controller
@RequestMapping("/dby_user/")
public class UserController {

	@Autowired
	IUserService iUserService;

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param Password
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "dby_login.do", method = RequestMethod.POST)
	// 实现自动序列化为JSON
	@ResponseBody()
	public ServerResponse<User> login(String username, String Password, HttpSession session) {
		ServerResponse<User> response = iUserService.login(username, Password);
		if (response.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}

	/**
	 * 用户注销登录-无service
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "dby_logout.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<String> logout(HttpSession session) {
		session.removeAttribute(Const.CURRENT_USER);

		return ServerResponse.createBySuccess();
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "dby_register.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<String> register(User user) {
		return iUserService.register(user);
	}

	/**
	 * 信息校验
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "dby_check_valid.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<String> checkValid(String str, String type) {
		return iUserService.checkValid(str, type);
	}

	/**
	 * 从session中获取用户登录信息-无service
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "dby_get_user_info.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<User> getUserInfo(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user != null) {
			return ServerResponse.createBySuccessStatusData(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
	}

	/**
	 * 用户忘记密码并获得提示问题
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "dby_forget_get_question.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<String> forgetGetQuestion(String username) {
		return iUserService.selectQuestion(username);
	}

	/**
	 * 使用本地缓存-校验问题答案
	 * 
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	@RequestMapping(value = "dby_forget_check_answer.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
		return iUserService.checkAnswer(username, question, answer);
	}

	/**
	 * 忘记密码时的重置密码
	 * 
	 * @param username
	 * @param newPassword
	 * @param forgetToken
	 * @return
	 */
	@RequestMapping(value = "dby_forget_reset_password.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
		return iUserService.forgetResetPassword(username, newPassword, forgetToken);
	}

	/**
	 * 登录状态下的重置密码
	 * 
	 * @param session
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "dby_reset_password.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<String> resetPassword(HttpSession session, String oldPassword, String newPassword) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);

		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登录");
		}

		return iUserService.resetPassword(oldPassword, newPassword, user);

	}

	/**
	 * 登录状态下更新用户个人信息
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "dby_update_information.do", method = RequestMethod.POST)
	@ResponseBody()
	public ServerResponse<User> updataInformation(HttpSession session, User user) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);

		if (currentUser == null) {
			return ServerResponse.createByErrorMessage("用户未登录");
		}
		/**
		 * 这两个属性不允许更新
		 */
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());

		ServerResponse<User> response = iUserService.updataInformation(user);
		if (response.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}

		return response;
	}

	/**
	 * 获取用户详细信息-未登录时需强制用户登录，随后根据id从数据库中获取用户的详细信息
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "dby_get_information.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> get_information(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
		}
		return iUserService.getInformation(currentUser.getId());
	}

}
