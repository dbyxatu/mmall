package dby.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mmall.common.Const;
import com.mmall.pojo.User;

import dby.common.ServerResponse;
import dby.service.IUserService;

/**
 * 用户管理员 Title: UserManageController Description:
 * 
 * @author 董博源
 * @date 2019/11/26 09:37:15
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

	@Autowired
	private IUserService iUserService;

	/**
	 * 后台管理员登录
	 * j
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "dby_admin_login.do", method = RequestMethod.POST)
	public ServerResponse<User> login(String username, String password, HttpSession session) {
		ServerResponse response = iUserService.login(username, password);

		if (response.isSuccess()) {
			User user = (User) response.getData();
			if (user.getRole() == Const.Role.ROLE_ADMIN) {
				// 说明当前登录者为管理员
				session.setAttribute(Const.CURRENT_USER, user);
				return response;
			} else {
				return ServerResponse.createByErrorMessage("不是管理员，无法登录");
			}
		}

		return response;
	}

}
