package dev.yuuki.olympiaserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {

	private static final Logger logger = LoggerFactory.getLogger(User.class);

	public String name;
	public final Role role;
	private String loginCode;

	public User(String name, Role role) {
		this.role = role;
		this.name = name;
		resetLoginCode();
	}

	public boolean validateLoginCode(String requestCode) {
		return loginCode.equals(requestCode);
	}

	public void resetLoginCode() {
		loginCode = Tools.randomLoginCode();
		logger.info("Login to \"%s\" with code: %s".formatted(name, loginCode));
	}

}
