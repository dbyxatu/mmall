package dby.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

import dby.service.ICategoryService;
import dby.service.IUserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 
 * Title: CategoryManageController Description:
 * 
 * @author 董博源
 * @date 2019/12/04 14:10:59
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private ICategoryService iCategoryService;

	/**
	 * 添加商品品类
	 * 
	 * @param session
	 * @param categoryName
	 * @param parentId
	 * @return
	 * @date 2019/12/04 14:10:59
	 */
	@RequestMapping("dby_add_category.do")
	@ResponseBody
	public ServerResponse addCategory(HttpSession session, String categoryName,
			@RequestParam(value = "parentId", defaultValue = "0") int parentId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
		}
		// 校验一下是否是管理员
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 是管理员
			// 增加我们处理分类的逻辑
			return iCategoryService.addCategory(categoryName, parentId);

		} else {
			return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
		}
	}

	/**
	 * 更新品类名称
	 * 
	 * @param session
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	@RequestMapping("dby_set_category_name.do")
	@ResponseBody
	public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
		/**
		 * 判断用户是否登录
		 */
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，需要登录");
		}
		/**
		 * 判断参数是否符合要求
		 */
		if (categoryId == null || StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMessage("添加品类参数错误");
		}
		/**
		 * 校验一下当前用户角色是否是管理员
		 */
		if (iUserService.checkAdminRole(user).isSuccess()) {

			// 更新指定id的品类的名称
			return iCategoryService.setCategoryName(categoryId, categoryName);
		}

		return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
	}

}
