package cn.estore.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import cn.estore.domain.Product;
import cn.estore.utils.DataSourceUtils;
import cn.estore.domain.Order;
import cn.estore.domain.OrderItem;
import javassist.compiler.ast.NewExpr;

public class ProductDao {
	public void addProduct(Product p) throws SQLException{
		
			QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
			String sql = "insert into products values(?,?,?,?,?,?,?)";
			runner.update(sql, p.getId(),p.getName(),p.getPrice()
					,p.getCategory(), p.getPnum(), p.getImgurl(), p.getDescription());
		
		
	}	
	public List<Product> findAll() throws SQLException{
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from products";
		return runner.query( sql, new BeanListHandler<Product>(Product.class));

	}
	public Product findById(String id) throws SQLException{
		QueryRunner runner  = new QueryRunner(DataSourceUtils.getDataSource());
		String sql ="select * from products where id =?";
		return runner.query(sql, new BeanHandler<Product>(Product.class),id);
		
		
	}
	// 修改商品的数量
		public void updateProductCount(Order order) throws SQLException {

			// 要修改的数量在哪?
			List<OrderItem> items = order.getOrderItems();

			Object[][] params = new Object[items.size()][2];

			for (int i = 0; i < items.size(); i++) {

				OrderItem item = items.get(i);
				params[i][0] = item.getBuynum();
				params[i][1] = item.getProduct_id();

			}

			String sql = "update products set pnum=pnum-? where id=?";

			QueryRunner runner = new QueryRunner();

			runner.batch(DataSourceUtils.getConnection(), sql, params);

			// for(OrderItem item:items){
			//
			// runner.update(sql,item.getBuynum(),item.getProduct_id());
			//
			// }
		}

		// 当删除订单时，修改商品数量
		public void updateProductCount(List<OrderItem> items) throws SQLException {

			Object[][] params = new Object[items.size()][2];

			for (int i = 0; i < items.size(); i++) {

				OrderItem item = items.get(i);
				params[i][0] = item.getBuynum();
				params[i][1] = item.getProduct_id();

			}

			String sql = "update products set pnum=pnum+? where id=?";

			QueryRunner runner = new QueryRunner();

			runner.batch(DataSourceUtils.getConnection(), sql, params);
		}

	
	// 得到销售榜单信息
	public List<Product> findSell() throws SQLException{
		QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "";
		return runner.query(sql, new BeanListHandler<Product>(Product.class));
		
		
		
	}
}
