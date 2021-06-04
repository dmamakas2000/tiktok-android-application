package gr.aueb.cs.tiktokapplication.appnode;// This is a consumer interface - Represents a general Consumer

public interface ConsumerInterface extends Node {

	public void register(String key);
	
	public void disconnect(String key);
	
	public void playData(String key);
	
}
