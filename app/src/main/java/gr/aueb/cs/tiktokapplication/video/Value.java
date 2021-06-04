package gr.aueb.cs.tiktokapplication.video;

import java.io.Serializable;

public class Value implements Serializable{
	
	/* -------------- This class represents a general Value-------------- */
	
	// -------------------------- Attributes --------------------------
	
	private static final long serialVersionUID = 1L;  // Serial Version ID
	
	private VideoFile videoFile;

	
	// Constructor
	public Value (VideoFile videofile) {
		this.videoFile = videofile;
	}
	
	// Default constructor
	public Value () {
		this.videoFile = null;
	}
	
	// -------------------------- Getter --------------------------
	
	public VideoFile getVideoFile() {
		return videoFile;
	}

	// -------------------------- Setter --------------------------
	
	public void setVideoFile(VideoFile videoFile) {
		this.videoFile = videoFile;
	}

}
