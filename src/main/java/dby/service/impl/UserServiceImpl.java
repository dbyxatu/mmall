package dby.service.impl;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.util.MD5Util;

import dby.common.ServerResponse;
import dby.service.IUserService;

/**
 * 
 * Title: UserServiceImpl Description:
 * 
 * @author 董博源
 * @date 2019/11/25 10:42:21
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 用户登录
	 */
	@Override
	public ServerResponse<User> login(String username, String Password) {

		int resultCode = userMapper.checkUsername(username);
		if (resultCode == 0) {
			return ServerResponse.createByErrorMessage("用户名不存在");
		}

		String md5Passeord = MD5Util.MD5EncodeUtf8(Password);
		User user = userMapper.selectLogin(username, md5Passeord);
		if (user == null) {
			return ServerResponse.createByErrorMessage("密码错误");
		}

		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySuccessStatusMessageData("登录成功", user);

	}

	/**
	 * 用户注册
	 */
	public ServerResponse<String> register(User user) {
		ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}

		validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}

		user.setRole(Const.Role.ROLE_CUSTOMER);
		// MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

		int resultCount = userMapper.insert(user);
		if (resultCount == 0) {
			return ServerResponse.createByErrorMessage("注册失败");
		}

		return ServerResponse.createBySuccessStatusMessage("注册成功");
	}

	/**
	 * 信息校验
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public ServerResponse<String> checkValid(String str, String type) {
		if (StringUtils.isNotBlank(type)) {
			// 开始校验
			if (Const.USERNAME.equals(type)) {
				int resultCode = userMapper.checkUsername(str);
				if (resultCode > 0) {
					return ServerResponse.createByErrorMessage("用户名已存在");
				}
			}
			if (Const.EMAIL.equals(type)) {
				int resultCode = userMapper.checkEmail(str);
				if (resultCode > 0) {
					return ServerResponse.createByErrorMessage("邮箱已存在");
				}
			}

		} else {
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccessStatusMessage("校验成功");
	}

	/**
	 * 忘记密码获取提示问题
	 */
	public ServerResponse<String> selectQuestion(String username) {
		ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
		if (validResponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("用户不存在");
		}

		String question = userMapper.selectQuestionByUsername(username);
		if (StringUtils.isNotBlank(question)) {
			return ServerResponse.createBySuccessStatusData(question);
		}
		return ServerResponse.createByErrorMessage("找回密码的问题是空");

	}

	/**
	 * 使用本地缓存-校验问题答案
	 * 
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	public ServerResponse<String> checkAnswer(String username, String question, String answer) {
		int resultCount = userMapper.checkAnswer(username, question, answer);
		if (resultCount > 0) {
			String forgetToken = UUID.randomUUID().toString();
			// forgetToken放到cache
			TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
			return ServerResponse.createBySuccessStatusData(forgetToken);
		}

		return ServerResponse.createByErrorMessage("问题的答案错误");

	}

	/**
	 * 忘记密码时的重置密码
	 * 
	 * @param username
	 * @param newPassword
	 * @param forgetToken
	 * @return
	 */
	public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
		if (StringUtils.isBlank(forgetToken)) {
			return ServerResponse.createByErrorMessage("参数错误，token需要传递");
		}

		ServerResponse validResoponse = this.checkValid(username, Const.USERNAME);
		if (validResoponse.isSuccess()) {
			return ServerResponse.createByErrorMessage("用户不存在");
		}

		/**
		 * 获取cache中的token
		 */
		String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
		if (StringUtils.isBlank(token)) {
			return ServerResponse.createByErrorMessage("token过期或者无效");
		}

		/**
		 * 传递的token和cache中的token一致时，根据用户名重置密码
		 */
		if (StringUtils.equals(forgetToken, token)) {
			String md5Password = MD5Util.MD5EncodeUtf8(newPassword);

			int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

			if (rowCount > 0) {
				return ServerResponse.createBySuccessStatusMessage("修改密码成功");
			}
		} else {
			return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
		}

		return ServerResponse.createByErrorMessage("修改密码失败");

	}

	/**
	 * 登录状态下的重置密码
	 * 
	 * @param session
	 * @param newPassword
	 * @return
	 */
	public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {
		/**
		 * 防止横向越权，需要校验一下这个用户的旧密码指定的是这个用户，因为我们查询一个count(1)，如果不指定Id,那么结果就是true
		 */
		int resultCount = userMapper.checkPassword(oldPassword, user.getId());
		if (resultCount == 0) {
			return ServerResponse.createByErrorMessage("旧密码错误");
		}

		user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
		int updateCount = userMapper.updateByPrimaryKeySelective(user);
		if (updateCount > 0) {
			return ServerResponse.createBySuccessStatusMessage("密码更新成功");
		}

		return ServerResponse.createByErrorMessage("密码更新失败");

	}

	/**
	 * 登录状态下更新用户个人信息
	 * 
	 * @param session
	 * @param user
	 * @return
	 */
	public ServerResponse<User> updataInformation(User user) {
		/**
		 * username不能被更新 同时，校验email:1.新的email十分已存在；且存在时.如果相同，不能是当前的用户
		 * 
		 */

		int resultCode = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		/**
		 * 此时说明，email不是当前用户且已经存在了，此时无法完成更新
		 */
		if (resultCode > 0) {
			return ServerResponse.createByErrorMessage("emali已存在，请更换邮箱后，再尝试更新");
		}

		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());

		int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
		if (updateCount > 0) {
			return ServerResponse.createBySuccessStatusMessageData("更新个人信息成功", updateUser);
		}

		return ServerResponse.createByErrorMessage("更新个人信息失败");
	}

	/**
	 * 获取用户详细信息-密码是不显示的
	 * 
	 * @param id
	 * @return
	 */
	public ServerResponse<User> getInformation(int userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			return ServerResponse.createByErrorMessage("找不到当前用户");
		}
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySuccessStatusData(user);
	}

	// backend

	/**
	 * 校验是否是管理员
	 * 
	 * @param user
	 * @return
	 */
	public ServerResponse checkAdminRole(User user) {
		if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}

}