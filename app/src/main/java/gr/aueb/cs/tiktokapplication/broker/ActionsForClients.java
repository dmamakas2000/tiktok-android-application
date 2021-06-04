package gr.aueb.cs.tiktokapplication.broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import gr.aueb.cs.tiktokapplication.appnode.Message;
import gr.aueb.cs.tiktokapplication.appnode.Publisher;
import gr.aueb.cs.tiktokapplication.video.ChannelName;
import gr.aueb.cs.tiktokapplication.video.Value;

public class ActionsForClients extends Thread {
	
	ObjectInputStream in;
	ObjectOutputStream out;
	Broker b;
	
	public ActionsForClients(Socket connection, Broker broker) {
		
		this.b = broker;
		
		try {
			
			// Out: Writing to client
			out = new ObjectOutputStream(connection.getOutputStream());
			
			// In: Reading from client
			in = new ObjectInputStream(connection.getInputStream());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pull(String key) {
		
		try {
			
			
			if (key.startsWith("#")) {
				
				// Means that user typed a hash-tag
				
				for (Value chunk: this.b.getBrokerQueue()) {
					if (chunk.getVideoFile().getAssociatedHashtags().contains(key)) {
						
						// We need to send these specific chunks! 			        
					    
						// Using a for loop, because it is important that we send the chunks at the same time! 
						
							for (Message userToSend: this.b.getRegisteredUsers()) {
								if (userToSend.getKey().equals(key)) {
									// Send to this user!
									out.writeObject(chunk);
								}
							}
					}
				}
				
			} else {
				
				// Means that user typed a channel-name 
				
				for (Value chunk: this.b.getBrokerQueue()) {
					if (chunk.getVideoFile().getChannelName().equals(key)) {
						
						// We need to send these specific chunks! 
					    
						// Using a for loop, because it is important that we send the chunks at the same time! 
						
							for (Message userToSend: this.b.getRegisteredUsers()) {
								if (userToSend.getKey().equals(key)) {
									// Send to this user!
									out.writeObject(chunk);
								}
							}
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Clear the queue because we don't need it anymore
		this.b.getBrokerQueue().clear();
	}
	
	public void run() {
		
        String message = "Unread";
        
        try {

        	message = (String) in.readObject();
        	
        	if (message.equals("Register")) {
        		
        		// Means we need to perform a registration! 
        		Message consumerToWrite = (Message) in.readObject();
        		this.b.getRegisteredUsers().add(consumerToWrite);
        		
        	} else if (message.equals("Disconnect")) {
        		
        		// Means we need to disconnect a user from the list!
        		Message consumerToDelete = (Message) in.readObject();
        		
        		// Find the user and delete him!
        		for (int i=0; i<this.b.getRegisteredUsers().size(); i++) {
        			if (consumerToDelete.getPort()==this.b.getRegisteredUsers().get(i).getPort()) {
        				this.b.getRegisteredUsers().remove(i);
        			}
        		}
        		
        	} else if (message.equals("Consume")) {
        		
        		// Means the user needs to consume a video!
        		String keyToConsume = (String) in.readObject();
        		int numberOfChunks = this.b.getChunksSize(keyToConsume);
        		out.writeObject("SV " + numberOfChunks);
        		this.pull(keyToConsume);
        		
        	} else if (message.equals("Update")) {
        		
        		// Means we have to update the existing brokers
        		String hashTagToDelete = (String) in.readObject();
        		
        		// Search within broker's hash-tags and delete it!       		
        		int index = -1;
        		for (int i=0; i<this.b.getResponsibleKeys().size(); i++) {
        			if (this.b.getResponsibleKeys().get(i).equals(hashTagToDelete)) {
        				index = i;
        			}
        		}
        		
        		if (index >= 0) {
        			// Remove this specific hash-tag
            		this.b.getResponsibleKeys().remove(index);
        		} 
        		        		
        	} else if(message.equals("add")) {
        		
        		this.out.writeObject("Message Accepted!(add)");
        		
        	} else if(message.equals("remove"))  {
        		
        		this.out.writeObject("Message Accepted!(remove)");
        		
        	} else if (message.equals("Failure")) {
        		
        		this.out.writeObject("Failure message accepted! ");
        		
        	} else {
        		
        		ChannelName channel = (ChannelName) in.readObject();
        		Publisher pub = new Publisher();
        		pub.setChannelname(channel);
        		this.b.getRegisteredPublishers().add(pub);
        	
        		// Means a user needs to push a video       	
        		String[] splited = message.split("\\s+");

            	int size = Integer.parseInt(splited[1]);
            	
            	String key = splited[2];
            	
            	this.b.getResponsibleKeys().add(key);
            	
            	if (splited[0].equals("S1")) {
            		
            		for (int i=0; i<size; i++) {
                		Value v = (Value) in.readObject();
                		b.getBrokerQueue().add(v);
                	}
            		
            	} 
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
