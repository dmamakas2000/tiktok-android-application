package gr.aueb.cs.tiktokapplication.appnode;

import gr.aueb.cs.tiktokapplication.video.ChannelName;

public class ThreadInformation {
	
	// -------------------------- Attributes -------------------------- 
	
	private int option;
	private int id;
	private int port;
	private String ip;
	private ChannelName channel;
	private String keyToUse;
	
	// -------------------------- Constructors --------------------------
	
	public ThreadInformation(int newOption, int newId, int newPort, String newIp, ChannelName newChannel, String key) {		
		this.setId(newId);
		this.setOption(newOption);
		this.setPort(newPort);
		this.setIp(ip);
		this.setChannel(newChannel);	
		this.keyToUse = key;
	}
	
	public ThreadInformation() {
		
	}
	
	// -------------------------- Getters --------------------------
	
	public int getOption() {
		return option;
	}
	
	public int getId() {
		return id;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getIp() {
		return ip;
	}
	
	public ChannelName getChannel() {
		return channel;
	}
	
	public String getKeyToUse() {
		return this.keyToUse;
	}
	
	// -------------------------- Setters --------------------------
	
	public void setOption(int option) {
		this.option = option;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setChannel(ChannelName channel) {
		this.channel = channel;
	}
	
	public void setKeyToUse(String newKey) {
		this.keyToUse = newKey;
	}
	
}
