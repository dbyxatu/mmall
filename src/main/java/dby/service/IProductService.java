package dby.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {

	/**
	 * 保存或更新产品
	 * 
	 */
	ServerResponse saveOrUpdateProduct(Product product);

	/**
	 * 设置商品上下架属性
	 */
	ServerResponse<String> setSaleStatus(Integer productId, Integer status);

	/**
	 * 获取商品详情
	 */
	ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

	/**
	 * 获取商品列表并进行分页处理
	 */
	ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

	/**
	 * 后台-商品搜索
	 */
	ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}