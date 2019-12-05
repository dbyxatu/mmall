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

}
