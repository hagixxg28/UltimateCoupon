package com.hagi.couponsystem.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class loginDetails {

	private long id;
	private String password;
	private String type;

	public long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	public loginDetails(long id, String password, String type) {
		super();
		this.id = id;
		this.password = password;
		this.type = type;
	}

}
