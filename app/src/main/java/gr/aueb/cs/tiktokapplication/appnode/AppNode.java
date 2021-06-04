package gr.aueb.cs.tiktokapplication.appnode;

import java.util.ArrayList;
import java.util.Scanner;
import gr.aueb.cs.tiktokapplication.video.ChannelName;

public class AppNode {
	
	public static void main(String[] args) {
		
		try {
			
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("***** Performing initializations before to start ***** \n\n");
			
			System.out.println("Please, complete the fields required to run this AppNode ");
			
			System.out.println("1) Publisher's ID");
			int pubId = scanner.nextInt();
			
			// Skip next line
			scanner.nextLine();
			
			System.out.println("2) Publisher's IP");
			String pubIp = scanner.nextLine();
			
			System.out.println("3) Publisher's Port");
			int pubPort = scanner.nextInt();
			
			// Skip next line
			scanner.nextLine();
					
			System.out.println("4) Channel-Name");
			String pubChannel = scanner.nextLine();
					
			System.out.println("5) Consumer's ID");
			int subId = scanner.nextInt();
			
			// Skip next line
			scanner.nextLine();
					
			System.out.println("6) Consumer's IP");
			String subIp = scanner.nextLine();
			
			System.out.println("7) Consumer's Port");
			int subPort = scanner.nextInt();
						
			// Create a new Publisher object
			Publisher publisher = new Publisher();
			
			ArrayList<String> hashtags = new ArrayList<String>();
			ChannelName channel = new ChannelName(pubChannel, hashtags);
			
			// Initialize publisher and retrieve information about brokers
			publisher.init(pubId, pubIp, pubPort, channel);
			publisher.retrieveInformation();
			
			// Create a new Consumer object and initialize it
			Consumer consumer = new Consumer();
			consumer.init(subId, subIp, subPort);
			consumer.retrieveInformation();
			
			while (true) {
				
			
				
				System.out.println("**************** AppNode's Menu ****************\n");
				System.out.println("Option 1: Add a hashtag to your channel. (Press 1) ");
				System.out.println("Option 2: Remove a hashtag from your channel. (Press 2) ");
				System.out.println("Option 3: Push a video. (Press 3) ");
				System.out.println("Option 4: Register to a certain key. (Press 4) ");
				System.out.println("Option 5: Disconnect from a certain key. (Press 5) ");
				System.out.println("Option 6: Play latest video from a certain key. (Press 6) ");
				System.out.println("Option 7: Exit. (Press 7) \n");
				System.out.println("************************************************\n");
				
				int option = scanner.nextInt();
				
				scanner.nextLine();
				
				if (option==1) {
					
					System.out.println("Please, give a hashtag to add. ");
					String hashtag = scanner.nextLine();
					
					// Create a publisher's thread
					ThreadInformation p = new ThreadInformation(1, pubId, pubPort, pubIp, channel, hashtag);
					publisher.setOption(p);
					new Thread(publisher).start();
					
				} else if (option == 2) {
					
					System.out.println("Please, give a hashtag to remove. ");
					String hashtag = scanner.nextLine();
					
					// Create a publisher's thread
					ThreadInformation p = new ThreadInformation(2, pubId, pubPort, pubIp, channel, hashtag);
					publisher.setOption(p);
					new Thread(publisher).start();
					
				} else if (option == 3) {
					
					System.out.println("Please, give a hashtag to push. ");
					String hashtag = scanner.nextLine();
					System.out.println("Please, give a video to push ");
					String video = scanner.nextLine();
					
					String key = hashtag + " " + video;
					
					// Create a publisher's thread
					ThreadInformation p = new ThreadInformation(3, pubId, pubPort, pubIp, channel, key);
					publisher.setOption(p);
					new Thread(publisher).start();
					
				} else if (option == 4) {
					
					System.out.println("Please, provide the key you want to register into. ");
					String key = scanner.nextLine();
					
					// Create a consumer's thread
					ThreadInformation c = new ThreadInformation(4, subId, subPort, subIp, null, key);
					consumer.setOpinion(c);
					new Thread(consumer).start();
					
				} else if (option == 5) {
					
					System.out.println("Please, provide the key you want to disconnect from. ");
					String key = scanner.nextLine();
					
					// Create a consumer's thread
					ThreadInformation c = new ThreadInformation(5, subId, subPort, subIp, null, key);
					consumer.setOpinion(c);
					new Thread(consumer).start();
					
				} else if (option == 6) {
					
					System.out.println("Please, provide the key you want to consume the video from. ");
					String key = scanner.nextLine();
					
					// Create a consumer's thread
					ThreadInformation c = new ThreadInformation(6, subId, subPort, subIp, null, key);
					consumer.setOpinion(c);
					new Thread(consumer).start();
					
				} else if (option == 7) {
					
					break;
					
				} else {
					
					System.out.println("This was not an option. Please, try again! \n\n");
					
				}
		
			}
			
			scanner.close();
		
		} catch (Exception e) {
			System.out.println("Input missmatch in some filed. Please, try again. \n\n");
		}
			
	}
	
}
