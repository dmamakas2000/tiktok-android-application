package gr.aueb.cs.tiktokapplication.appnode;

import java.io.Serializable;

public class Message implements Serializable {
	
	
	private static final long serialVersionUID = 1L;  // Serial Version ID - Not used
	
	private String ipAddres;
	private int port;
	private String key;   // Used only by the consumer
	
	// Constructor
	public Message(String ip, int p) {
		this.ipAddres = ip;
		this.port = p;
		this.key = "";
	}
	
	// Default Constructor 
	public Message() {
		
	}
		
	// Getters
	public String getIpAddres() {
		return ipAddres;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getKey() {
		return this.key;
	}
	
	// Setters
	public void setIpAddres(String ipAddres) {
		this.ipAddres = ipAddres;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setKey(String newKey) {
		this.key = newKey;
	}


}
