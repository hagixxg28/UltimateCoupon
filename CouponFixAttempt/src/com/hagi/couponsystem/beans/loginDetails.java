package com.hagi.couponsystem.beans;

import javax.xml.bind.annotation.XmlRootElement;

import com.hagi.couponsystem.Enums.ClientType;
@XmlRootElement
public class loginDetails {

	private long id;
	private String password;
	private ClientType type;

	public long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public ClientType getType() {
		return type;
	}

	public loginDetails(long id, String password, ClientType type) {
		super();
		this.id = id;
		this.password = password;
		this.type = type;
	}

}
