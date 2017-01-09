package cn.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import cn.estore.domain.User;

public class PrivilegeTag extends SimpleTagSupport {

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void doTag() throws JspException, IOException {

		System.out.println(user);

		if (user == null || ("user".equals(user.getRole()))) {

			this.getJspContext().getOut().write("<h1>权限不足</h1>");

			throw new SkipPageException();
		}
	}
}
