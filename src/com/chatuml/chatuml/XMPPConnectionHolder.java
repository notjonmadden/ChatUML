package com.chatuml.chatuml;

import org.jivesoftware.smack.XMPPConnection;

public class XMPPConnectionHolder {
	private XMPPConnection conn;
	private static final XMPPConnectionHolder self = new XMPPConnectionHolder();
	private XMPPConnectionHolder() { }
	
	public static XMPPConnectionHolder getInstance() {
		return self;
	}
	public void setConnection(XMPPConnection connection) {
		conn = connection;
	}
	public XMPPConnection getConnection() {
		return conn;
	}
}
