package impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;

import dby.service.ICategoryService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

/**
 * Created by geely
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryMapper categoryMapper;

	/**
	 * 添加商品品类
	 */
	public ServerResponse addCategory(String categoryName, Integer parentId) {
		if (parentId == null || StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMessage("添加品类参数错误");
		}

		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);// 这个分类是可用的

		int rowCount = categoryMapper.insert(category);
		if (rowCount > 0) {
			return ServerResponse.createBySuccess("添加品类成功");
		}
		return ServerResponse.createByErrorMessage("添加品类失败");
	}

	/**
	 * 更新品类名称
	 */
	public ServerResponse setCategoryName(Integer categoryId, String categoryName) {

		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);

		int rowCount = categoryMapper.updateCategoryNameByPrimaryKey(category);
		if (rowCount > 0) {
			return ServerResponse.createBySuccessMessage("修改品类名称成功");
		}

		return ServerResponse.createByErrorMessage("修改品类名称失败");
	}

}
