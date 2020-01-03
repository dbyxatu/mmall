package dby.service;

import com.mmall.pojo.User;

import dby.common.ServerResponse;

/**
 * 
 * Title: IUserService Description:
 * 
 * @author 董博源
 * @date 2019/11/25 10:40:47
 */
public interface IUserService {

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param Password
	 * @return
	 */
	ServerResponse<User> login(String username, String Password);

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	ServerResponse<String> register(User user);

	/**
	 * 信息校验
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	ServerResponse<String> checkValid(String str, String type);

	/**
	 * 忘记密码获取提示问题
	 * 
	 * @param username
	 * @return
	 */
	ServerResponse<String> selectQuestion(String username);

	/**
	 *
	 * 使用本地缓存-校验问题答案
	 * 
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	ServerResponse<String> checkAnswer(String username, String question, String answer);

	/**
	 * 忘记密码时的重置密码
	 * 
	 * @param username
	 * @param newPassword
	 * @param forgetToken
	 * @return
	 */
	ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

	/**
	 * 登录状态下的重置密码
	 * 
	 * @param session
	 * @param newPassword
	 * @return
	 */
	ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);

	/**
	 * 登录状态下更新用户个人信息
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	ServerResponse<User> updataInformation(User user);

	/**
	 * 获取用户详细信息
	 * 
	 * @param id
	 * @return
	 */
	ServerResponse<User> getInformation(int userId);

	// backend

	/**
		 * 校验是否是管理员
		 * 
		 * @param user
		 * @return
		 */
	ServerResponse checkAdminRole(User user);
}