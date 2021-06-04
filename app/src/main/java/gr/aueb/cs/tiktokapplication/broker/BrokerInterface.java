package gr.aueb.cs.tiktokapplication.broker;// This is a broker interface - Represents a general Broker

import gr.aueb.cs.tiktokapplication.appnode.Consumer;
import gr.aueb.cs.tiktokapplication.appnode.Node;
import gr.aueb.cs.tiktokapplication.appnode.Publisher;

public interface BrokerInterface extends Node {

	public void calculateKeys();
	
	public Publisher acceptConnection(Publisher pub);
	
	public Consumer acceptConnection(Consumer sub);
	
	public void notifyPublisher(String key);
	
	public void notifyBrokersOnChanges(String key);
	
	public void filterConsumers(String key);
	
}
