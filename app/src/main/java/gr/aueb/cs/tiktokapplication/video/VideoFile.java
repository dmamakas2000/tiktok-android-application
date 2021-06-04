package gr.aueb.cs.tiktokapplication.video;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoFile implements Serializable{
	/* -------------- This class represents a Video File -------------- */
	
	// -------------------------- Attributes -------------------------- 

	private static final long serialVersionUID = 1L; // Serial Version ID
	
	private String VideoName;
	private String ChannelName;
	private String DateCreated;
	private String length;
	private String framerate;
	private String frameWidth;
	private String frameHeight;
	private ArrayList<String> associatedHashtags;
	private byte[] videoFileChunk = new byte[12288];
	
	// Constructor
	public VideoFile(String video, String channel, String date, String videoLength, String Framerate, String FrameW, String FrameH, ArrayList<String> hashtags, byte[] chunk) {
		this.VideoName = video;
		this.ChannelName = channel;
		this.DateCreated = date;
		this.length = videoLength;
		this.framerate = Framerate;
		this.frameWidth = FrameW;
		this.frameHeight = FrameH;
		this.associatedHashtags = hashtags;
				
		// Deep copying
		for (int i=0; i<chunk.length; i++) {
			this.videoFileChunk[i] = chunk[i];
		}
	}
	
	// Default Constructor
	public VideoFile() {
		this.VideoName = null;
		this.ChannelName = null;
		this.DateCreated = null;
		this.length = null;
		this.framerate = null;
		this.frameWidth = null;
		this.frameHeight = null;
		this.associatedHashtags = null;
		this.videoFileChunk = null;
	}
	
	// -------------------------- Getters --------------------------
	
	public String getVideoName() {
		return VideoName;
	}
	
	public String getChannelName() {
		return ChannelName;
	}
	
	public String getDateCreated() {
		return DateCreated;
	}
	
	public String getLength() {
		return length;
	}
	
	public String getFramerate() {
		return framerate;
	}
	
	public String getFrameHeight() {
		return frameHeight;
	}
	
	public String getFrameWidth() {
		return frameWidth;
	}
	
	public ArrayList<String> getAssociatedHashtags() {
		return associatedHashtags;
	}
	
	public byte[] getVideoFileChunk() {
		return videoFileChunk;
	}
	
	// -------------------------- Setters --------------------------
	
	public void setVideoName(String videoName) {
		VideoName = videoName;
	}
	
	public void setChannelName(String channelName) {
		ChannelName = channelName;
	}
	
	public void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}
	
	public void setLength(String length) {
		this.length = length;
	}
	
	public void setFramerate(String framerate) {
		this.framerate = framerate;
	}
	
	public void setFrameWidth(String frameWidth) {
		this.frameWidth = frameWidth;
	}
	
	public void setFrameHeight(String frameHeight) {
		this.frameHeight = frameHeight;
	}
	
	public void setAssociatedHashtags(ArrayList<String> associatedHashtags) {
		this.associatedHashtags = associatedHashtags;
	}
	
	public void setVideoFileChunk(byte[] videoFileChunk) {
		this.videoFileChunk = videoFileChunk;
	}
	
}
