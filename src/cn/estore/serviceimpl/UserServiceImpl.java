package cn.estore.serviceimpl;

import java.sql.SQLException;

import cn.estore.Exception.ActiveException;
import cn.estore.Exception.LoginException;
import cn.estore.Exception.RegistException;
import cn.estore.dao.UserDao;
import cn.estore.domain.User;
import cn.estore.service.UserService;
import cn.estore.utils.MailUtils;
import javassist.compiler.ast.NewExpr;

public class UserServiceImpl implements UserService {

	@Override
	public void regist(User user) throws RegistException {
		UserDao userDao = new UserDao();
		try {

			userDao.adduser(user);

			// 发送邮件
			String emailMsg = "注册成功，请在12小时内<a href='http://localhost:8080/bookstore/user?method=activeuser&activecode="
					+ user.getActivecode() + "'>激活</a>，激活码是" + user.getActivecode();
			MailUtils.sendMail(user.getEmail(), emailMsg);

		} catch (SQLException e) {

			throw new RegistException("註冊失敗");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RegistException("邮件发送失败");
		}

	}

	@Override
	public User login(String name, String password) throws LoginException {
		User user;
		try {
			UserDao userDao = new UserDao();
			user = userDao.findUserByNameAndPassword(name, password);
			if (user == null) {
				throw new LoginException("用戶名或密碼不准确");
			}
			if (user.getState() == 0) {
				throw new LoginException("用户状态未激活");

			}
		} catch (SQLException e) {

			throw new LoginException("登陸失敗");

		}
		return user;
	}

	@Override
	public void activeUser(String activecode) throws ActiveException, RegistException {
		// 用户激活
		User user = null;
		UserDao userDao = null;
		try {
			userDao = new UserDao();
			user = userDao.findUserByActiveCode(activecode);

		} catch (SQLException e) {
			throw new RegistException("根据激活码查找用户失败");

		}
		long time = System.currentTimeMillis() - user.getUpdatetime().getTime();
		if (time > 12 * 60 * 60) {
			throw new ActiveException("激活已过期");

		}

		try {
			userDao.activeUserByActivecode(activecode);
		} catch (SQLException e) {
			throw new ActiveException("激活失败");

		}

	}

}
