package gr.aueb.cs.tiktokapplication.dao;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class BrokerDAO {

    private static String ipAddress;
    private static final ArrayList<Integer> ports = new ArrayList<>();

    public String getIpAddress() {
        return ipAddress;
    }

    public ArrayList<Integer> getPorts() {
        return ports;
    }

    public void readFiles() {

        try {

            // Getting current working directory
            String userDirectory = System.getProperty("user.dir");

            File directory= new File(userDirectory);

            for (File file : directory.listFiles()) {

                if (file.getName().endsWith(".txt")) {

                    Scanner myReader = new Scanner(file);

                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();

                        String[] splited = data.split("\\s+", 2);

                        String ipadrress = splited[0];
                        int port = Integer.parseInt(splited[1]);

                        ports.add(port);
                        ipAddress = ipadrress;
                    }

                    myReader.close();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void acceptConnection() {

        try {

            // Server is listening on specific port
            ServerSocket serverSocket = new ServerSocket(5555);
            serverSocket.setReuseAddress(true);
            // Running infinite loop for getting client requests

            while (true) {

                // Socket object to receive incoming client requests
                Socket client = serverSocket.accept();
                System.out.println("Accepting connection from: " + client);

                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject(this.getIpAddress());
                out.writeObject(this.getPorts());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BrokerDAO brokerDAO = new BrokerDAO();

        brokerDAO.readFiles();

        while (true) {
            brokerDAO.acceptConnection();
        }

    }
}