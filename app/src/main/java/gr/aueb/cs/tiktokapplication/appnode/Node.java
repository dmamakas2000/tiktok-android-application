package gr.aueb.cs.tiktokapplication.appnode;// --- Node interface - Generally represents a certain node (Publisher, Consumer, Broker) ---

public interface Node {
	
	public void connect();
	
	public void disconnect();
	
	public void updateNodes();
}
