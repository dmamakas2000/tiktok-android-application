package gr.aueb.cs.tiktokapplication.appnode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;
import gr.aueb.cs.tiktokapplication.broker.Broker;
import gr.aueb.cs.tiktokapplication.video.Value;

public class Consumer extends Thread implements Node, Serializable, ConsumerInterface  {
	
	// -------------------------- Attributes --------------------------
	
	private static final long serialVersionUID = 1L;  // Serial Version id
	
	private Socket socket;        // Consumer's socket
	private String ip;		      // Consumer's IP address
	private int port;             // Consumer's port
	private int nodeId;
	private PrintWriter out;      // Consumer's output stream
	private BufferedReader in;    // Consumer's input stream
	
	private ThreadInformation option;
	
	private ArrayList<Message> brokers = new ArrayList<Message>();
	
	public Consumer(ThreadInformation newOption) {
		this.option = newOption;
	}
	
	public Consumer() {
		
	}
	// -------------------------- Getters --------------------------
	
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
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public ArrayList<Message> getBrokers() {
		return this.brokers;
	}
	
	public ThreadInformation getOption() {
		return this.option;
	}
	
	// -------------------------- Setters --------------------------
		
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
		
	public void setSocket(Socket newSocket) {
		this.socket = newSocket;
	}
	
	public void setOpinion(ThreadInformation newOpinion) {
		this.option = newOpinion;
	}
	
	// -------------------------- Node --------------------------
		
	public void init(int nodeNumber, String ipAdress, int portNumber) {
		
		// Do the important initializations
		this.setNodeId(nodeNumber);
		this.setIp(ipAdress);
		this.setPort(portNumber);
		
	}

	public void connect() {
			
	       try {
	            
	        	// Establish a socket connection by providing host and port number
	        	this.socket = new Socket(this.getIp(), this.getPort());
	        	
	            // Writing to server
	            this.setOut(new PrintWriter(socket.getOutputStream(), true));
	  
	            // Reading from server
	            this.setIn(new BufferedReader(new InputStreamReader(socket.getInputStream())));
	                       
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
		
		// We don't want this method to do something here, so we just leave it blank and we don't implement it!
		
	}
	
	// -------------------------- Some Publisher's methods used inside Consumer class --------------------------
	
	public void retrieveInformation() {
		try {
			
			// Getting current working directory
			String userDirectory = System.getProperty("user.dir");
			
			File directory= new File(userDirectory);
			for (File file : directory.listFiles()) {
				
				if (file.getName().endsWith(".txt")) {
					
					Scanner myReader = new Scanner(file);
					
					while (myReader.hasNextLine()) {
					        String data = myReader.nextLine();
					        
					        String[] splited = data.split("\\s+");
					        
					        					        			        
					        String ipadrress = splited[0];
					        int port = Integer.parseInt(splited[1]);
					        			        
					        
					        Message message = new Message(ipadrress, port);
					        
					        this.brokers.add(message);
					   }
					   
					   myReader.close();
					   				   
				}			   
			}
						   
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	// -------------------------- Consumer's Methods --------------------------
	
	public void register(String key) {
		
		
		Message brokerToRegister = this.hashTopic(key);
		
		this.connect(brokerToRegister.getIpAddres(), brokerToRegister.getPort());
		
		try {
			
			OutputStream os = this.getSocket().getOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(os);
	        oos.writeObject("Register");
	        Message m = new Message();
	        m.setIpAddres(this.getIp());
	        m.setPort(this.getPort());
	        m.setKey(key);
	        oos.writeObject(m);
	        
	        this.disconnect();
	        
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void disconnect(String key) {
		
		Message brokerToRegister = this.hashTopic(key);
		
		this.connect(brokerToRegister.getIpAddres(), brokerToRegister.getPort());
		
		try {
			
			OutputStream os = this.getSocket().getOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(os);
	        oos.writeObject("Disconnect");
	        Message m = new Message();
	        m.setIpAddres(this.getIp());
	        m.setPort(this.getPort());
	        m.setKey(key);
	        oos.writeObject(m);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.disconnect();
		
	}

	public void playData(String key) {
				
		ArrayList<Value> chunksToPlay = new ArrayList<Value>();
		
		Message responsibleBroker = this.hashTopic(key);
		
		this.connect(responsibleBroker.getIpAddres(), responsibleBroker.getPort());
		
		try {
			
			OutputStream os = this.getSocket().getOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(os);
	        oos.writeObject("Consume");
	        oos.writeObject(key);
	        
	        // And now, just start to retrieve video information -chunks- from broker	        
	        InputStream is = this.getSocket().getInputStream();
	        ObjectInputStream ois = new ObjectInputStream(is);
	        
	        // Receiving message from broker
	        String recievedMessage = (String) ois.readObject();
	        
	        // Split the message into 2 parts
	        String[] splited = recievedMessage.split("\\s+");
	        
	        // First part is considered to be message security code
	        // Second part is considered to be the number of chunks to expect
	        
	        int numberOfChunks = Integer.parseInt(splited[1]);
	        
	        if (splited[0].equals("SV")) {
	        		        	
	        	// Start reading the chunks and placing them inside the ArrayList
	        	for (int i=0; i<numberOfChunks; i++) {
	        		Value chunk = (Value) ois.readObject();
	        		chunksToPlay.add(chunk);
	        	}
	        	
	        	// Now, we received the chunks and next step is to unify them!
	        	ArrayList<Byte> video = new ArrayList<Byte>();
	        	
	        	for (Value v: chunksToPlay) {
	        		
	        		for (int j=0; j<v.getVideoFile().getVideoFileChunk().length; j++) {
	        			// Adding each specific byte into ArrayList
	        			video.add(v.getVideoFile().getVideoFileChunk()[j]);
	        		}
	        		
	        	}
	        	
	        	// Create the video file
	        	FileOutputStream videoFile = new FileOutputStream("downloaded_video.mp4");
	        	
	        	// Convert Byte ArrayList into byte[] array
	        	byte[] videoArray = new byte[video.size()];
	        	
	        	for (int k=0; k<videoArray.length; k++) {
	        		videoArray[k] = video.get(k);
	        	}
	        	
	        	// Write bytes inside video file
	        	videoFile.write(videoArray);
	        	
	        	// Finally, lets play our video!
	        	// Desktop.getDesktop().open(new File("downloaded_video.mp4"));

	        	// Close the file and exit
	        	videoFile.close();
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void run() {
		
		if (this.getOption().getOption()==4) {
			
			this.register(this.getOption().getKeyToUse());
			
		} else if(this.getOption().getOption()==5) {
			
			this.disconnect(this.getOption().getKeyToUse());
			
		} else if (this.getOption().getOption()==6) {
			
			this.playData(this.getOption().getKeyToUse());
			
		}
		
	}
	
}
