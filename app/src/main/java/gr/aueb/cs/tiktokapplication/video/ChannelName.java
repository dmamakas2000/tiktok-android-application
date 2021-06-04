package gr.aueb.cs.tiktokapplication.video;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ChannelName implements Serializable {
	/* -------------- This class represents a Channel -------------- */
	
	// -------------- Attributes -------------- 
	
	private static final long serialVersionUID = 1L;    // Serial version ID
	private String channelName;
	private ArrayList<String> hashtagsPublished;
	private  HashMap<String, ArrayList<Value>> userVideoFilesMap = new HashMap<String, ArrayList<Value>>();
	
	// -------------- Constructors --------------
	public ChannelName(String name, ArrayList<String> hashtags) {
		this.channelName = name;
		this.hashtagsPublished = hashtags;
	}
	
	// Default constructor 
	public ChannelName() {
		this.channelName = null;
		this.hashtagsPublished = null;
		this.userVideoFilesMap = null;
	}
	
	
	// -------------- Getters --------------
	
	public String getChannelName() {
		return channelName;
	}
	
	public ArrayList<String> getHashtagsPublished() {
		return hashtagsPublished;
	}
	
	public HashMap<String, ArrayList<Value>> getUserVideoFilesMap() {
		return userVideoFilesMap;
	}
	
	// -------------- Setters --------------
	
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	
	public void setHashtagsPublished(ArrayList<String> hashtagsPublished) {
		this.hashtagsPublished = hashtagsPublished;
	}
	
	public void setUserVideoFilesMap(HashMap<String, ArrayList<Value>> userVideoFilesMap) {
		this.userVideoFilesMap = userVideoFilesMap;
	}
	
	

}
