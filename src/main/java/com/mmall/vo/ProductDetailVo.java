package com.mmall.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by geely
 */
public class ProductDetailVo {

	@Setter
	@Getter
    private Integer  id;
	
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
    private String subImages;
	
	@Setter
	@Getter
    private String detail;
	
	@Setter
	@Getter
    private BigDecimal price;
	
	@Setter
	@Getter
    private Integer stock;
	
	@Setter
	@Getter
    private Integer status;
	
	@Setter
	@Getter
    private String createTime;
	
	@Setter
	@Getter
    private String updateTime;

	@Setter
	@Getter
    private String imageHost;
	
	@Setter
	@Getter
    private Integer parentCategoryId;
}
