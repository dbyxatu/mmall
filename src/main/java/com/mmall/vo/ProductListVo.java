package com.mmall.vo;

/**
 * vo 视图显示对象，用于传递controller到前端的数据传输对象
 * bo 业务传输对象，用于传递service到controller间的数据传输对象
 * pojo 简单对象，其的对象属性全面，用于dao到service间的数据传输对象。
 * 
 */
import java.math.BigDecimal;
import lombok.*;

/**
 * Created by geely
 */
public class ProductListVo {

	@Setter
	@Getter
	private Integer id;

	@Setter
	@Getter
	private Integer categoryId;

	@Setter
	@Getter
	private String name;

	@Setter
	@Getter
	private String subtitle;

	@Setter
	@Getter
	private String mainImage;

	@Setter
	@Getter
	private BigDecimal price;

	@Setter
	@Getter
	private Integer status;

	@Setter
	@Getter
	private String imageHost;

}
