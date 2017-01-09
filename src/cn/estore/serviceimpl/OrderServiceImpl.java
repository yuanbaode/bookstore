package cn.estore.serviceimpl;

import java.sql.SQLException;
import java.util.List;

import cn.estore.Exception.OrderException;
import cn.estore.Exception.PrivilegeException;
import cn.estore.dao.OrderDao;
import cn.estore.dao.OrderItemDao;
import cn.estore.dao.ProductDao;
import cn.estore.domain.Order;
import cn.estore.domain.User;
import cn.estore.service.OrderService;
import cn.estore.utils.DataSourceUtils;
import cn.estore.domain.OrderItem;

public class OrderServiceImpl implements OrderService {

	@Override
	public void add(User user, Order order) throws PrivilegeException {
		OrderDao orderDao = new OrderDao();
		OrderItemDao oiDao = new OrderItemDao();
		ProductDao pDao = new ProductDao();
		try {
			DataSourceUtils.startTransaction();
			orderDao.createOrder(order);
			oiDao.addOrderItem(order);
			pDao.updateProductCount(order);

		} catch (SQLException e) {

			e.printStackTrace();
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				DataSourceUtils.commitAndReleased();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public List<Order> find(User user) throws PrivilegeException, SQLException {
		OrderDao orderDao = new OrderDao();
		List<Order> orders = orderDao.findOrder(user);
		OrderItemDao oiDao = new OrderItemDao();
		for (Order order : orders) {
			order.setOrderItems(oiDao.findOrderItemByOrderId(order));

		}

		return orders;
	}

	@Override
	public void delete(String id) throws OrderException {
		OrderDao orderDao = new OrderDao();
		OrderItemDao oiDao = new OrderItemDao();
		ProductDao pDao = new ProductDao();
		try {
			// 1.1 得到商品的数量
			List<OrderItem> items = oiDao.findOrderItemByOrderId(id);
			// 1.2修改商品的数量
			pDao.updateProductCount(items);
			// 2.删除订单项
			oiDao.delOrderItem(id);
			// 3.删除订单
			orderDao.delOrder(id);

		} catch (SQLException e) {
			e.printStackTrace();

			try {
				DataSourceUtils.rollback(); //事务回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			throw new OrderException("删除订单失败");

		} finally {
			try {
				DataSourceUtils.commitAndReleased(); //事务提交
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


	}

	// 根据订单号修改订单状态
		public void updateState(String id) throws SQLException {
			OrderDao dao = new OrderDao();

			dao.updateState(id);
		}

}
