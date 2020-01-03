package com.aiops.uim.mcs.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class UIMInstance {

	//TODO: Need to get it from somewhere rather then static.
	private String protocol = "http";
	private String hostname = "kp642490-ump-e4";
	private int port = 80;
	private String userName = "administrator";
	private String password = "N0tallowed";

	public UIMInstance(String protocol, String hostname, int port, String userName, String password) {
		super();
		this.protocol = protocol;
		this.hostname = hostname;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}
	
	public String getProtocol() {
		return protocol;
	}
	public String getHostname() {
		return hostname;
	}
	public int getPort() {
		return port;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	
	public void addBasicAuthetication(Client client)	{
		client.addFilter(new HTTPBasicAuthFilter(getUserName(), getPassword()));
	}
}
