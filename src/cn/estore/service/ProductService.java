package cn.estore.service;

import java.util.List;

import cn.estore.domain.Product;
import cn.estore.domain.User;

public interface ProductService {
	// 添加商品

	public void add(User user, Product product) throws Exception;

	// 查找商品
	public List<Product> findAll() throws Exception;

	// 根据id查找商品
	public Product findById(String id) throws Exception;

	// 查询销售榜单

	public List<Product> findSell(User user) throws Exception;

}
