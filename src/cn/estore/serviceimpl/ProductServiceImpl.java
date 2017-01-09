package cn.estore.serviceimpl;

import java.sql.SQLException;
import java.util.List;

import cn.estore.dao.ProductDao;
import cn.estore.domain.Product;
import cn.estore.domain.User;
import cn.estore.service.ProductService;

public class ProductServiceImpl implements ProductService {

	@Override
	public void add(User user, Product product) throws SQLException {
		ProductDao pd =new ProductDao();
		pd.addProduct(product);

	}

	@Override
	public List<Product> findAll() throws SQLException {
		ProductDao pd =new ProductDao();
		 List<Product> pList =pd.findAll();
		return pList;
	}

	@Override
	public Product findById(String id) throws SQLException {
		
		ProductDao dao = new ProductDao();
		return dao.findById(id);
	}

	@Override
	public List<Product> findSell(User user) throws Exception {
		
		ProductDao dao = new ProductDao();
		return dao.findSell();
	}

}
