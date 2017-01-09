package cn.estore.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.estore.domain.Product;
import cn.estore.service.ProductService;
import cn.estore.serviceimpl.ProductServiceImpl;

public class ProductServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");

		if ("findById".equals(method)) {
			findById(request, response);

		} else if ("findAll".equals(method) || method == null) {
			findAll(request, response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

	// 产找所有商品
	public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ProductService productService = new ProductServiceImpl();
			List<Product> pList = productService.findAll();
			request.setAttribute("pList", pList);

		} catch (Exception e) {
			e.printStackTrace();

		}
		request.getRequestDispatcher("/page.jsp").forward(request, response);;

	}

	// 按id查找产品
	public void findById(HttpServletRequest request, HttpServletResponse response) {
		try {
			String id = request.getParameter("id");
			ProductService productService = new ProductServiceImpl();
			Product p = productService.findById(id);
			request.setAttribute("product", p);
			request.getRequestDispatcher("/productInfo.jsp").forward(request,
					response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.getRequestDispatcher("/page.jsp");

	}
}
