package dby.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

import dby.service.IProductService;

/**
 * 
 * Title: ProductManageController Description:
 * 
 * @author 董博源
 * @date 2019/12/19 14:53:29
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

	@Autowired
	private IUserService iUserService;
	@Autowired
	private IProductService iProductService;

	/**
	 * 保存产品：更新或新增商品
	 * 
	 * @param session
	 * @param product
	 * @return
	 */
	@RequestMapping("dby_save.do")
	@ResponseBody
	public ServerResponse productSave(HttpSession session, Product product) {

		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 增加产品的业务逻辑
			return iProductService.saveOrUpdateProduct(product);

		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}

	}

	/**
	 * 产品上下架:即修改产品的上下架状态
	 * 
	 * @param session
	 * @param product
	 * @return
	 */
	@RequestMapping("dby_set_sale_status.do")
	@ResponseBody
	public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {

		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 产品上下架的业务逻辑
			if (productId == null || status == null) {
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
						ResponseCode.ILLEGAL_ARGUMENT.getDesc());
			}
			return iProductService.setSaleStatus(productId, status);

		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}

	}

	/**
	 * 获取商品详情
	 * 
	 * @param session
	 * @param productId
	 * @return
	 */
	@RequestMapping("dby_detail.do")
	@ResponseBody
	public ServerResponse getDetail(HttpSession session, Integer productId) {

		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 获取产品详情的业务逻辑
			if (productId == null) {
				return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
						ResponseCode.ILLEGAL_ARGUMENT.getDesc());
			}
			return iProductService.manageProductDetail(productId);

		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}

	}

	/**
	 * 后台-获取商品列表
	 * 
	 * @param session
	 * @param productId
	 * @return
	 */
	@RequestMapping("dby_list.do")
	@ResponseBody
	public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// TODO
			return iProductService.getProductList(pageNum, pageSize);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}

	}
}
