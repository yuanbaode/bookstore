package cn.estore.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.estore.Exception.ActiveException;
import cn.estore.Exception.LoginException;
import cn.estore.Exception.RegistException;
import cn.estore.domain.User;
import cn.estore.service.UserService;
import cn.estore.serviceimpl.UserServiceImpl;

public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String method = request.getParameter("method");
		// 得到请求参数method,判断当前是什么操作
		if ("login".equals(method)) { // 登录操作台
			login(request, response);
		} else if ("regist".equals(method)) { // 注册操作
			regist(request, response);
		} else if ("logout".equals(method)) {
			// 注销操作
			logout(request, response);
		} else if ("activeuser".equals(method)) {
			activecode(request, response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}

	// 激活用户操作
	public void activecode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String activecode = request.getParameter("activecode");
		UserService userService = new UserServiceImpl();

		try {
			userService.activeUser(activecode);
		} catch (RegistException e) {
			String msgString = e.getMessage() + ",重新<a href='" + request.getContextPath() + "/regist.jsp'>注册</a>";
			response.getWriter().write(msgString);
			return;
		} catch (ActiveException e) {
			String msgString = e.getMessage() + ",重新<a href='" + request.getContextPath() + "/regist.jsp'>注册</a>";
			response.getWriter().write(msgString);

			e.printStackTrace();
			return;

		}
		response.getWriter().write("用户激活成功,请<a href='" + request.getContextPath() + "/index.jsp'>回首页</a>");

	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 注销
		request.getSession().invalidate(); // 销毁session

		Cookie cookie = new Cookie("autologin", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");

		response.addCookie(cookie);

		response.sendRedirect(request.getContextPath() + "/index.jsp");


	}

	private void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 注册
		//验证码
		String checkcode =request.getParameter("checkcode");
		String _checkcode =(String)request.getSession().getAttribute("checkcode_session");
		request.getSession().removeAttribute("checkcode_session");
		if (_checkcode == null || (!_checkcode.equals(checkcode))) {
			request.setAttribute("regist.message", "验证码不正确");
			request.getRequestDispatcher("/regist.jsp").forward(request,
					response);
			return;
		}
		// 1.得到请求参数，封装到javaBean中.
				User user = new User();
				try {
					BeanUtils.populate(user, request.getParameterMap());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				// 进行服务器端数据校验
				Map<String, String> map = user.validateRegist();

				if (map.size() > 0) {
					// 说明有错误信息
					request.setAttribute("map", map);
					request.getRequestDispatcher("/regist.jsp").forward(request,
							response);
					return;
				}

				// 手动封装一个激活码
				user.setActivecode(UUID.randomUUID().toString());

				// 2.调用service操作

				UserServiceImpl service = new UserServiceImpl();

				try {
					service.regist(user);

					response.getWriter().write(
							"注册成功，激活后请<a href='" + request.getContextPath()
									+ "/index.jsp'>登录</a>");

				} catch (RegistException e) {
					request.setAttribute("regist.message", e.getMessage());
					request.getRequestDispatcher("/regist.jsp").forward(request,
							response);
					return;
				}

	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 登录
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserService userService = new UserServiceImpl();
		User user = null;
		try {
			user = userService.login(username, password);
			if (user != null) {
				String remember = request.getParameter("remember");
				if ("on".equals(remember)) {
					Cookie cookie = new Cookie("saveusername", URLEncoder.encode(username, "utf-8"));
					cookie.setMaxAge(7 * 24 * 60 * 60);
					cookie.setPath("/");
					response.addCookie(cookie);
				} else {
					Cookie cookie = new Cookie("saveusername", URLEncoder.encode(username, "utf-8")); // 存储utf-8码
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
				String autologin= request.getParameter("autologin");
				if("on".equals(autologin)){
					Cookie cookie =new Cookie("autologin", URLEncoder.encode(username, "utf-8")+"|"+password);
					cookie.setMaxAge(7 * 24 * 60 * 60);
					cookie.setPath("/");
					response.addCookie(cookie);
				}else {
					Cookie cookie = new Cookie("autologin", URLEncoder.encode(
							username, "utf-8") + "|" + password);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
				// 登录成功后，将用户存储到session中.
				request.getSession().invalidate();
				request.getSession().setAttribute("user", user);
				response.sendRedirect(request.getContextPath() + "/index.jsp");
				return;
			} else {
				request.setAttribute("login.message", "用户名或密码错误");
				request.getRequestDispatcher("/page.jsp").forward(request,
						response);
				return;
			}
			

		} catch (LoginException e) {
			e.printStackTrace();
			request.setAttribute("login.message", e.getMessage());
			request.getRequestDispatcher("/page.jsp")
					.forward(request, response);
			return;
		}
	}
}
