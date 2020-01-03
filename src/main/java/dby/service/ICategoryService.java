package dby.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * Created by geely
 */
public interface ICategoryService {

	/**
	 * 添加商品品类
	 */
	ServerResponse addCategory(String categoryName, Integer parentId);

	/**
	 * 更新品类名称
	 */
	ServerResponse setCategoryName(Integer categoryId, String categoryName);

	/**
	 * 根据当前品类id获取其平级子节点的信息，并且不递归
	 */
	ServerResponse<List<Category>> getChildParalleCategory(int categoryId);

	/**
	 * 查询当前节点的id和递归子节点的Id
	 */
	ServerResponse selectCategoryAndChildrenById(Integer categoryId);

}