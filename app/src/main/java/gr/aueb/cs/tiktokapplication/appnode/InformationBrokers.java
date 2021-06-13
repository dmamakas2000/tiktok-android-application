package gr.aueb.cs.tiktokapplication.appnode;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class InformationBrokers {

    private String ipAddress;
    private final ArrayList<Integer> ports = new ArrayList<>();

    public String getIpAddress() {
        return ipAddress;
    }

    public ArrayList<Integer> getPorts() {
        return this.ports;
    }

    public void readFiles() {

        try {

            // Getting current working directory
            String userDirectory = System.getProperty("user.dir");
            System.out.println("Directory is: " + userDirectory);

            File directory= new File(userDirectory + "/app/");

            for (File file : directory.listFiles()) {

                if (file.getName().endsWith(".txt")) {

                    Scanner myReader = new Scanner(file);

                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();

                        String[] splited = data.split("\\s+", 2);


                        String ipadrress = splited[0];
                        int port = Integer.parseInt(splited[1]);

                        this.ports.add(port);
                        this.ipAddress = ipadrress;
                    }

                    myReader.close();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        InformationBrokers informationBrokers = new InformationBrokers();

        informationBrokers.readFiles();
    }
}
