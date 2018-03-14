package hr.fer.zemris.webapps.blog.servlets.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.webapps.blog.dao.DAOProvider;
import hr.fer.zemris.webapps.blog.model.BlogUser;
import hr.fer.zemris.webapps.blog.servlets.util.PasswordUtil;

/**
 * {@link Form} for user login.
 *
 * @author Dan
 */
public class LoginForm extends Form{

	/** Nick of the user. */
	private String nick;
	
	/** Plain password of the user. */
	private String password;
	
	/**
	 * Creates a new {@code LoginForm}.
	 */
	public LoginForm() {
		super();
		nick = "";
		password = "";
	}
	
	@Override
	public void fillFromHttpReq(HttpServletRequest req) {
		nick = prepare(req.getParameter("nick"));
		password = prepare(req.getParameter("password"));
	}
	
	@Override
	public void validate() {
		errors.clear();
		
		if (nick.isEmpty()) {
			errors.put("nick", "Please provide nick!");
			return;
		}
		
		if (password.isEmpty()) {
			errors.put("password", "Please provide password!");
			return;
		}
		
		BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
		if(user == null){
			errors.put("nick", "Invalid nick!");
			return;
		}
		
		String passwordHash = PasswordUtil.calculateHash(password);
		if(!passwordHash.equals(user.getPasswordHash())){
			errors.put("password", "Invalid password!");
			return;
		}
	}
	
	/**
	 * Getter for nick.
	 * 
	 * @return nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Setter for nick.
	 * 
	 * @param nick
	 *            nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Getter for password.
	 * 
	 * @return plain password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for password.
	 * 
	 * @param password
	 *            password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}
