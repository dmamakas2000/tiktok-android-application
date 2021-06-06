package gr.aueb.cs.tiktokapplication.appnode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import gr.aueb.cs.tiktokapplication.broker.Broker;
import gr.aueb.cs.tiktokapplication.video.ChannelName;
import gr.aueb.cs.tiktokapplication.video.Value;
import gr.aueb.cs.tiktokapplication.video.VideoFile;


public class Publisher extends Thread implements Node, PublisherInterface {
	
	// --------------------------- Attributes ---------------------------
	
	private ChannelName channelname;
	
	private int nodeId;
	
	private Socket socket;        // Publisher's socket
	private String ip;		      // Publisher's IP address
	private int port;             // Publisher's port
	private PrintWriter out;      // Publisher's output stream
	private BufferedReader in;    // Publisher's input stream
	
	private ThreadInformation option;
	
	private ArrayList<String> hashtagsToBeDeleted = new ArrayList<String>(); // Hash-tags to be deleted are stored there temporarily
	
	private ArrayList<Message> brokers = new ArrayList<Message>();
	
	// Constructor
	public Publisher(ThreadInformation newOption) {
		this.option = newOption;
	}
	
	// Default constructor
	public Publisher() {
		
	}
	
	// --------------------------- Getters ---------------------------
	
	public ChannelName getChannelname() {
		return channelname;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public PrintWriter getOut() {
		return out;
	}
	
	public BufferedReader getIn() {
		return in;
	}
	
	public int getNodeId() {
		return this.nodeId;
	}

	public ArrayList<Message> getBrokers() {
		return this.brokers;
	}
	
	public ArrayList<String> getHashtagsToBeDeleted() {
			return this.hashtagsToBeDeleted;
	}
	
	public ThreadInformation getOption() {
		return this.option;
	}
	
	// --------------------------- Setters ---------------------------
	
	public void setChannelname(ChannelName channelname) {
		this.channelname = channelname;
	}
	
	public void setSocket(Socket newSocket) {
		this.socket = newSocket;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}
	
	public void setNodeId(int newId) {
		this.nodeId = newId;
	}
	
	public void setBrokers(ArrayList<Message> b) {
		this.brokers = b;
	}
	
	public void setHashtagsToBeDeleted(ArrayList<String> newHashtags) {
		this.hashtagsToBeDeleted = newHashtags;
	}
	
	public void setOption(ThreadInformation newOption) {
		this.option = newOption;
	}
	// --------------------------- Node's Methods ---------------------------
	
	public void init(int nodeNumber, String ipAdress, int portNumber, ChannelName channel) {
		
		// Do the important initializations
		this.setNodeId(nodeNumber);
		this.setIp(ipAdress);
		this.setPort(portNumber);
		this.setChannelname(channel);
		
	}

	public void connect() {
		
        try {
            
        	// Establish a socket connection by providing host and port number
        	this.socket = new Socket(this.getIp(), this.getPort());
                       
        }
        catch (IOException e) {
            e.printStackTrace();
        }
			
	}
	
	public void connect(String ip, int port) {
		
		try {
            
        	// Establish a socket connection by providing host and port number
        	this.socket = new Socket(ip, port);
                       
        }
        catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	public void disconnect() {
		
		try {
			
			this.socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateNodes() {
		
		try {
			
			// We just need to check, for each hash-tag if this is contained inside it's certain broker or not
			
			for (String hashtag: this.getHashtagsToBeDeleted()) {
				
				Message responsibleBroker = this.hashTopic(hashtag);
				
				// Establish a connection with this specific broker 
				this.connect(responsibleBroker.getIpAddres(), responsibleBroker.getPort());
				
				OutputStream os = this.getSocket().getOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(os);
		        
		        // Sending an Update message to this broker 
		        oos.writeObject("Update");
		        
		        oos.writeObject(hashtag);
				
			}
			
			// Now, just remove the hash-tags and from our local ArrayList and we are fine!
			for (String hashtag: this.getHashtagsToBeDeleted()) {
				this.getHashtagsToBeDeleted().remove(hashtag);
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void retrieveInformation() {
		try {

			String ipB1 = "192.168.1.200";
			String ipB2 = "192.168.1.200";
			String ipB3 = "192.168.1.200";
			int pB1 = 3030;
			int pB2 = 4040;
			int pB3 = 5050;


			Message b1 = new Message(ipB1, pB1);
			Message b2 = new Message(ipB2, pB2);
			Message b3 = new Message(ipB3, pB3);

			this.brokers.add(b1);
			this.brokers.add(b2);
			this.brokers.add(b3);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// --------------------------- Publisher's Methods ---------------------------
	
	public void addHashTag(String hashtag) {
		
		// Adds the new hash-tag if it doesn't exist in the channels hash-tags already
		
		try{
			
            if (!channelname.getHashtagsPublished().contains(hashtag)) {
                channelname.getHashtagsPublished().add(hashtag);
                notifyBrokersForHashTags("add",hashtag);
            }
            
        }
        catch(Exception e){
            System.err.println("Could not add Hashtag");
            e.printStackTrace();
        }
     
	}

	public void removeHashTag(String hashtag) {
		
		// Removes a hash-tag if it doesn't exist in the channels hash-tags already

		try{
			
            if (this.channelname.getHashtagsPublished().contains(hashtag)) {
                this.channelname.getHashtagsPublished().remove(hashtag);
        		this.getHashtagsToBeDeleted().add(hashtag); // Adding the hash-tag to the queue to be deleted from broker later on
                this.notifyBrokersForHashTags("remove" , hashtag);
                this.updateNodes();
            }
            
        }
        catch(Exception e){
            System.err.println("Could not remove Hashtag");
            e.printStackTrace();
        }
		
		
	}

	public ArrayList<Message> getBrokerList() {
		return this.getBrokers();
		
	}

	public Message hashTopic(String hashtag) {
		
		String hashtext = null; // this is going to be the hashed text
		
		try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
  
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(hashtag.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            hashtext = no.toString(16);
  
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
  
            // return the HashText
        }
  
        // For specifying wrong message digest algorithms
        catch (Exception e) {
            System.err.println("Could not hash text!");
        }
				
		// Trying to determine the broker this certain text goes on
		
		// Let's just create 3 Broker objects 
		
		ArrayList<String> bKeys = new ArrayList<String>();


		long[] sums = new long[3];
		int pointer = 0;
		
		for (Message m: this.getBrokers()) {
			String ip = m.getIpAddres();
			int port = m.getPort();
			
			Broker b = new Broker();
			b.setIpBroker(ip);
			b.setPortBroker(port);
			b.calculateKeys();
			bKeys.add(b.getKeys());
			
			long sum = 0, mul = 1;
			for (int i = 0; i < b.getKeys().length(); i++) {
			    mul = (i % 4 == 0) ? 1 : mul * 256;
			    sum += b.getKeys().charAt(i) * mul;
			}
			

			sums[pointer] = sum;
			pointer++;
			
		}
		
		
		String[] arr = new String[4];   // The array we want to sort
		int[] indexes = new int[4];		// The indexes of the brokers
		
		arr[0] = bKeys.get(0);
		arr[1] = bKeys.get(1);
		arr[2] = bKeys.get(2);
		arr[3] = hashtext; 				// Adding hashed text into last position
		
		indexes[0] = 1;
		indexes[1] = 2;
		indexes[2] = 3;
		indexes[3] = 4;					// Hash text has the index of '4'
		
		int size = arr.length;
		
		for(int i = 0; i<size-1; i++) {
			for (int j = i+1; j<arr.length; j++) {
				
				// Compares each elements of the array to all the remaining elements  
				if(arr[i].compareTo(arr[j])>0) {
					
					// Swapping array elements  
					String temp = arr[i];  
					arr[i] = arr[j];  
					arr[j] = temp; 
					
					// Swapping indexes array elements
					int t = indexes[i];
					indexes[i] = indexes[j];
					indexes[j] = t;
					
				}
			}
		}
		
		// Variable, trying to detect where index=4 is (our hashed text)
		int index = -1;
		
		// Searching for it
		for (int i=0; i<indexes.length; i++) {
			if (indexes[i] == 4) {
				index = i;
			}
		}
		
		// OK, so now let's find the suitable broker
		int broker = -5;
		
		if (index == 0) {
			// First space
			broker = indexes[1];
		} else if (index == 1) {
			// Second space
			broker = indexes[2];
		} else if (index == 2) {
			// Third space
			broker = indexes[3];
		} else if (index == 3) {
			// Fourth space 
			broker = indexes[1];
		}
		
		// Finally, return the desired broker
		return this.getBrokers().get(broker-1);
	}

	public void push(String key, Value video) {
		
		// First, we need to hash the video
		
		// Hashing it, we get the desired broker for this specific key
		Message desiredBroker = this.hashTopic(key);
		
		// Extracting information about the broker
		String ip = desiredBroker.getIpAddres();	// Broker's IP address
		int port = desiredBroker.getPort();			// Broker's port
		
		// Establish a connection with this specific broker
		this.connect(ip, port);
		
		// Now, we need to generate the chunks of this video
		ArrayList<Value> chunks = this.generateChunks(video.getVideoFile().getVideoName());
		
		// Adding the chunks into the channel's name information
		this.getChannelname().getUserVideoFilesMap().put(key, chunks);
				
		try {
			
			System.out.println("Sending video to broker: " + ip + " - " + port);
			
			OutputStream os = this.getSocket().getOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(os);
	        
	        oos.writeObject("S1 "+chunks.size() + " " + key);
	        oos.writeObject(this.getChannelname());
	        
			for (Value chunk: chunks) {
				oos.writeObject(chunk);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void notifyFailure(Broker b) {
		
		// Notifies the user about a failed search of the hash tag they used as input and then disconnects
		this.connect(b.getIpBroker(),b.getPortBroker());

        try {
            OutputStream os = this.getSocket().getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject("Failure");
        }
        catch (Exception e) {
            System.out.println("Exception during output stream of notifyFailure!");
        }
        
        disconnect();
        
	}

	public void notifyBrokersForHashTags(String type, String hashtag) {
		
		Message m = hashTopic(hashtag);
        this.connect(m.getIpAddres(),m.getPort());
        try {
            OutputStream os = this.getSocket().getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(type);
        }
        catch (Exception e) {
            System.out.println("Exception found in notifyBrokersForHashTags!");
        }
		
	}

	/*
	public ArrayList<Value> generateChunks(String video) {
	
		ArrayList<Value> chunks = new ArrayList<Value>();
		
		
		// Step 1: Convert video file into a byte array
		
		File file = new File(video);
		int size = (int) file.length();
		byte[] bytes = new byte[size]; // byte array
		
		
		
		try {
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
			buf.close();
			
			
			// Step 2: Create chunks
			
			// Each chunk is going to contain 12 KB of data
			
			// Detecting the file type
		    BodyContentHandler handler = new BodyContentHandler();
		    Metadata metadata = new Metadata();
		    FileInputStream inputstream = new FileInputStream(new File(video));
		    ParseContext pcontext = new ParseContext();
		      
		    // Parser 
		    MP4Parser MP4Parser = new MP4Parser();
		    MP4Parser.parse(inputstream, handler, metadata, pcontext);
	
		    // File size in bytes  
			int file_size = bytes.length;
			
			// Whole chunks required
			int numberOfChunks = file_size/12288;
			int remainingData = (file_size-(numberOfChunks*12288)); // Remaining bytes = 1 extra chunk
			
			if (remainingData == 0) {
				
				// Case 1 - Bytes fit perfectly
				
				for (int i=0; i< numberOfChunks; i++) {
					
					String videoName = i + "_" + video;
					String channelname = this.getChannelname().getChannelName();
					String dateCreated = metadata.get("creation_date");
					String length =  metadata.get("image_length");
					String frameRate = "";
					String frameWidth = metadata.get("image_width");
					String frameHeight = metadata.get("image_height");
					ArrayList<String> associatedHashtags = this.getChannelname().getHashtagsPublished();
					
					byte[] videoFileChunk = new byte[12288];
					
					// initializing videoFileChunk array 
					int index = 0;
					for (int j=i*12288; j<(i+1)*12288; j++) {
						videoFileChunk[index] = bytes[j];
						index++;
					}
					
					VideoFile v = new VideoFile(videoName, channelname, dateCreated, length, frameRate, frameWidth, frameHeight, associatedHashtags, videoFileChunk);
					chunks.add(new Value(v));
				}
				
			} else {
				
				// Case 2 - Bytes don't fit perfectly
				
				for (int i=0; i< numberOfChunks+1; i++) {
					
					String videoName = i + "_" + video;
					String channelname = this.getChannelname().getChannelName();
					String dateCreated = metadata.get("creation_date");
					String length =  metadata.get("image_length");
					String frameRate = "";
					String frameWidth = metadata.get("image_width");
					String frameHeight = "";
					ArrayList<String> associatedHashtags = this.getChannelname().getHashtagsPublished();
					
					byte[] videoFileChunk = new byte[12288];
					
					if (i == numberOfChunks) {
						// Last chunk remaining
						
						// initializing videoFileChunk array
						int index = 0;
						for (int j=i*12288; j<bytes.length; j++) {
							videoFileChunk[index] = bytes[j];
							index++;
						}
						
					} else {
						
						// initializing videoFileChunk array
						int index = 0;
						for (int j=i*12288; j<(i+1)*12288; j++) {
							videoFileChunk[index] = bytes[j];
							index++;
						}
					}
					VideoFile v = new VideoFile(videoName, channelname, dateCreated, length, frameRate, frameWidth, frameHeight, associatedHashtags, videoFileChunk);
					chunks.add(new Value(v));
				}
				
			}
			
		} catch (Exception e) {
			System.err.println("Couldn't convert video into chunks ");
		}
		
		return chunks;
	}
	*/

	public ArrayList<Value> generateChunks(String video) {
		return null;
	}

	public void run() {
				
		if (this.getOption().getOption()==1) {
			
			this.addHashTag(this.getOption().getKeyToUse());
			
		} else if (this.getOption().getOption()==2) {
			
			this.removeHashTag(this.getOption().getKeyToUse());
			
		} else if (this.getOption().getOption()==3) {
			
			String[] splited = this.getOption().getKeyToUse().split("\\s+");
			
			VideoFile videoValueFile = new VideoFile();
			videoValueFile.setVideoName(splited[1]);
	        Value videoValue = new Value(videoValueFile);
	        
	        this.push(splited[0], videoValue);
			
		}
				
	}
	
}
