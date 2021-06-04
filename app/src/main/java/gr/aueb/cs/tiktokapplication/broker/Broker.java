package gr.aueb.cs.tiktokapplication.broker;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;

import gr.aueb.cs.tiktokapplication.appnode.Consumer;
import gr.aueb.cs.tiktokapplication.appnode.Message;
import gr.aueb.cs.tiktokapplication.appnode.Node;
import gr.aueb.cs.tiktokapplication.appnode.Publisher;
import gr.aueb.cs.tiktokapplication.video.ChannelName;
import gr.aueb.cs.tiktokapplication.video.Value;

public class Broker implements Node, BrokerInterface {

	// ---------------------------------------- Attributes ----------------------------------------
	
	private ArrayList<Message> brokers;		// Information about all brokers (IP + Port)
	private ArrayList<Message> registeredUsers = new ArrayList<Message>();           // Broker's registered users
	private ArrayList<Publisher> registeredPublishers = new ArrayList<Publisher>();	 // Broker's registered publishers
	
	private ArrayList<String> responsibleKeys = new ArrayList<String>();			 // Broker's responsible keys
	
	private int brokerId;             		// Broker's id
	
	private PrintWriter outBroker;    		// Broker's output stream
    private BufferedReader inBroker;  		// Broker's input stream
    private ServerSocket socketBroker;		// Broker's connection server-socket
    private Socket socketBrokerConnect;		// Broker's connection socket
    private String ip;						// Broker's address
    private int port;             			// Broker's port
    
    private String keys;                    // Broker's keys 
	
    private Queue<Value> brokerQueue = new LinkedList<>();   // Broker's queue used to forward chunks into consumers
    
    // ---------------------------------------- Constructor ----------------------------------------
    
    public Broker(int nodeNumber) {
    	this.setBrokerId(nodeNumber);
    	this.setInBroker(null);
    	this.setOutBroker(null);
    	
    	this.calculateKeys();
    }
    
    // ---------------------------------------- Default constructor ----------------------------------------
    
    public Broker() {
    	this.setBrokerId(-100);
    	this.setInBroker(null);
    	this.setOutBroker(null);
    }
    
    // ---------------------------------------- Getters ----------------------------------------
    
    public int getBrokerId() {
		return brokerId;
	}

    
    public PrintWriter getOutBroker() {
		return outBroker;
	}
    
    public BufferedReader getInBroker() {
		return inBroker;
	}
    
    public ServerSocket getSocketBroker() {
    	return this.socketBroker;
    }
    
    public Socket getSocketBrokerConnection() {
    	return this.socketBrokerConnect;
    }
    
    public String getKeys() {
		return keys;
	}
    
    public String getIpBroker() {
		return ip;
	}
    
    public int getPortBroker() {
		return port;
	}
    
    public List<Message> getRegisteredUsers() {
		return registeredUsers;
	}
    
    public List<Publisher> getRegisteredPublishers() {
		return registeredPublishers;
	}
    
    public Queue<Value> getBrokerQueue() {
		return brokerQueue;
	}
    
    public List<String> getResponsibleKeys() {
		return responsibleKeys;
	}
    
    public int getChunksSize(String key) {
    	int chunks = 0;
    	
    	for (Value chunk: this.getBrokerQueue()) {
			if (chunk.getVideoFile().getAssociatedHashtags().contains(key)) {
				chunks++;
			}
		}
    	
    	return chunks;
    }
    
    // ---------------------------------------- Setters ----------------------------------------
    
    public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}
	
	public void setOutBroker(PrintWriter outBroker) {
		this.outBroker = outBroker;
	}
	
	public void setInBroker(BufferedReader inBroker) {
		this.inBroker = inBroker;
	}

	public void setSocketBroker(ServerSocket s) {
		this.socketBroker = s;
	}
	
	public void setSocketBrokerConnection(Socket s) {
		this.socketBrokerConnect = s;
	}
	
	public void setKeys(String keys) {
		this.keys = keys;
	}
	
	public void setPortBroker(int port) {
		this.port = port;
	}

	public void setIpBroker(String ip) {
		this.ip = ip;
	}
	
	public void setRegisteredUsers(ArrayList<Message> registeredUsers) {
		this.registeredUsers = registeredUsers;
	}
	
	public void setRegisteredPublishers(ArrayList<Publisher> registeredPublishers) {
		this.registeredPublishers = registeredPublishers;
	}
	
	public void setBrokerQueue(Queue<Value> brokerQueue) {
		this.brokerQueue = brokerQueue;
	}
	
	public void setResponsibleKeys(ArrayList<String> responsibleKeys) {
		this.responsibleKeys = responsibleKeys;
	}
	
	// ---------------------------------------- Methods to implement ----------------------------------------
	
	public void init(int id, String ip, int port) {
		// Do the important initializations
		
		this.setBrokerId(id);
		this.setIpBroker(ip);
		this.setPortBroker(port);

		this.calculateKeys();
	}

	public List<Message> getBrokers() {
		return brokers;
	}
	
	public void connect() {
		
        try {
            
        	// Establish a socket connection by providing host and port number
        	this.socketBrokerConnect = new Socket(this.getIpBroker(), this.getPortBroker());
        	
            // Output stream
             this.outBroker = new PrintWriter(socketBrokerConnect.getOutputStream(), true);
  
            // Input stream
             this.inBroker = new BufferedReader(new InputStreamReader(socketBrokerConnect.getInputStream()));
                       
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	
	}

	public void disconnect() {
		
		try {
			
			this.socketBroker.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void updateNodes() {
		
		// We don't want this method to do something here, so we don't implement it! 
		
		// We just leave it blank!
		
	}

	public void calculateKeys() {
		
		String key = this.getIpBroker() + this.getPortBroker();
		String hashkey = "";
		
		// Let's hash this String into a SHA-1 function
		
		try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
  
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(key.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            hashkey = no.toString(16);
  
            // Add preceding 0s to make it 32 bit
            while (hashkey.length() < 32) {
            	hashkey = "0" + hashkey;
            }
  
            // Set broker's keys
            this.setKeys(hashkey);
            
        }
  
        // For specifying wrong message digest algorithms
        catch (Exception e) {
            System.err.println("Could not calculate broker's keys!");
        }
		
	}

	public Publisher acceptConnection(Publisher publisher) {
		
        try {
  
            // Server is listening on specific port 
            this.socketBroker = new ServerSocket(this.getPortBroker());
            this.socketBroker.setReuseAddress(true);
  
            // Running infinite loop for getting client requests
            
            while (true) {
  
                // Socket object to receive incoming client requests
                
                Socket client = this.socketBroker.accept();
                
                System.out.println("Accepting connection from: " + client);
                
                this.outBroker = new PrintWriter(client.getOutputStream(), true);
      
                this.inBroker= new BufferedReader(new InputStreamReader(client.getInputStream()));
                      
                // Create a new thread object
                // This thread will handle the client separately
             
                Thread t = new ActionsForClients(client, this);
                t.start();
             
                break;
 
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.socketBroker != null) {
                try {
                	this.socketBroker.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return null;
	}

	public Consumer acceptConnection(Consumer consumer) {
		
		 try {
			  
	            // Server is listening on specific port 
	            this.socketBroker = new ServerSocket(this.getPortBroker());
	            this.socketBroker.setReuseAddress(true);
	  
	            // Running infinite loop for getting client requests
	            
	            while (true) {
	  
	                // Socket object to receive incoming client requests
	                
	                Socket client = this.socketBroker.accept();
	                
	                System.out.println("Accepting connection from: " + client);
	                
	                this.outBroker = new PrintWriter(client.getOutputStream(), true);
	      
	                this.inBroker= new BufferedReader(new InputStreamReader(client.getInputStream()));
	                      
	                // Create a new thread object
	                // This thread will handle the client separately
	             
	                Thread t = new ActionsForClients(client, this);
	                t.start();
	             
	                break;
	 
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (this.socketBroker != null) {
	                try {
	                	this.socketBroker.close();
	                }
	                catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
			return null;
		
	}

	public void notifyPublisher(String key) {
		
		
		// Search between registered publishers and find those 
		// we need (have published at least one video using this hash-tag
		// or have a specific channel name)
		
		
		if (key.startsWith("#")) {
			// Case 1: Hash-Tags
			
			ArrayList<Publisher> toNotify = new ArrayList<Publisher>();
			
			for (Publisher p: registeredPublishers) {
				for (String hashtag: p.getChannelname().getHashtagsPublished()) {
					if (hashtag == key) {
						toNotify.add(p);
					}
				}
			}
			
			// Now, Let's open the connection with those Publishers!
			
			for (Publisher p: toNotify) {
				p.connect();
			}
			
		} else {
			// Case 2: Channel-Name
			
			ArrayList<Publisher> toNotify = new ArrayList<Publisher>();
			
			for (Publisher p: registeredPublishers) {
				if (p.getChannelname().getChannelName() == key) {
					toNotify.add(p);
				}
			}
			
			// Now, Let's open the connection with those Publishers!
			
			for (Publisher p: toNotify) {
				p.connect();
			}
		}
		
	}
	
	public void notifyBrokersOnChanges(String key) {
		
		ArrayList<String> tempHashtags=new ArrayList <String>();
		
		if (key.startsWith("#")) {
			// Case : Hash-Tags
			
			if (brokerQueue.isEmpty()) {
				
				System.out.println("There is no newer video to display! ");
				
			} else {
				
				System.out.println("There is new video for this broker!");
				for (Value v: brokerQueue) {
					ArrayList<String> hashtags=v.getVideoFile().getAssociatedHashtags();    // Get video's hash-tags
					
					Iterator<String>it=hashtags.iterator();
					while (it.hasNext()) {
						if (!tempHashtags.contains(it.next())) {
							tempHashtags.add(it.next());									// Add distinct hash-tags (no double values)	
						}
					}
				}
					Iterator<String> iter=tempHashtags.iterator();
					while(iter.hasNext()) {
						//Broker b=hashTopic(iter.next());    								// Get responsible broker
						this.responsibleKeys.add(iter.next());								// Add the video's keys to the broker	
					}					
			}
		}
		
	}

	public void filterConsumers(String key) {
		
		for (Publisher p:registeredPublishers) {
			
			// Publisher's channel-name 
			ChannelName name=p.getChannelname();
			
			// Get the hash-map of the channel
			HashMap<String,ArrayList<Value>> userVideoFiles= name.getUserVideoFilesMap();		
			
			// KeySet iterates through the keys (here, hash-tags) only
			Iterator <String>it=userVideoFiles.keySet().iterator();	
			while (it.hasNext()) { 		
				
				String h=it.next();				// Get iterator's key
				if (h==key) {					// Channel's key equals to the given one
					it.remove();				// Delete publisher's videos
					
				}
			}	
		}
	}

	public static void main(String[] args) {
		
		try {
			
			// Creating a new Broker object
			Broker broker = new Broker();
			
			// Asking the basic information about this broker
			
			// Creating scanner object
			Scanner input = new Scanner(System.in);
			
			System.out.println("Please, give us the ID for this broker. ");
			int id = input.nextInt();
			
			System.out.println("Please, give us the port for this broker. ");
			int port = input.nextInt();
			
			// Skipping next line
			input.nextLine();
			
			System.out.println("Please, give us the IP address for this broker. ");
			String ip = input.nextLine();
				
			// Closing scanner object
			input.close();
			
			// Initializing broker
			broker.init(id, ip, port);
				
			// Writing Broker's information into a file
			try {
				
				FileWriter brokerWriter = new FileWriter(broker.getBrokerId() + "broker.txt");
				
				// Writing Broker's IP address
				brokerWriter.write(broker.getIpBroker());
				
				brokerWriter.write(" ");
				
				// Writing Broker's Port
				brokerWriter.write(String.valueOf(broker.getPortBroker()));
				
				brokerWriter.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Publisher pp = new Publisher();
			
			while (true) {
				broker.acceptConnection(pp);
			}
		} catch (Exception e) {
			System.out.println("An exception occured. Please check out your inputs and try again. \n\n");
		}	
		
	}

}
