package cn.estore.service;

import cn.estore.Exception.ActiveException;
import cn.estore.Exception.LoginException;
import cn.estore.Exception.RegistException;
import cn.estore.domain.User;

public interface UserService {
	// 注册操作
	public void regist(User user) throws RegistException;

	// 登录操作
	public User login(String username, String password) throws LoginException;

	// 激活操作
	public void activeUser(String activecode) throws ActiveException, RegistException;

}
