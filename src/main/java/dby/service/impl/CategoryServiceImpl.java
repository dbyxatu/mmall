package dby.service.impl;

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

	/**
	 * 根据当前品类id获取其平级子节点的信息，并且不递归
	 */
	public ServerResponse<List<Category>> getChildParalleCategory(int categoryId) {

		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if (CollectionUtils.isEmpty(categoryList)) {
			logger.info("未找到当前分类的子类");
		}

		return ServerResponse.createBySuccess(categoryList);

	}

	/**
	 * 查询当前节点的id和递归子节点的Id
	 */
	public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {

		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if (CollectionUtils.isEmpty(categoryList)) {
			logger.info("未找到当前分类的子类");
		}

		// 集合初始化
		Set<Category> categorySet = Sets.newHashSet();
		findChildCategory(categorySet, categoryId);

		// 获得品类id集合
		List<Integer> categoryIdList = Lists.newArrayList();
		if (categoryId != null) {
			for (Category categoryItem : categorySet) {
				categoryIdList.add(categoryItem.getId());
			}
		}

		return ServerResponse.createBySuccess(categoryIdList);

	}

	// 递归算法，算出子节点
	private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			categorySet.add(category);
		}

		/**
		 * mybatis select没查到数据时，不会返回Null，因此不用担心categoryList空指针异常
		 */
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		for (Category categoryItem : categoryList) {
			findChildCategory(categorySet, categoryItem.getId());
		}

		return categorySet;

	}

}