import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

class Client {


    //The client will be interacting with the server in the following way:
    // 1) Given the "add" or "show"
    // 2) If client chooses "show", client can then view all data currently stored on server
    // 3) If client chooses to "add", the client can then add data to the server, which the server will then store

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);


        System.out.println("Input(show|add): ");
        String input = in.nextLine(); //take in input


        try {
            
            //lets first try connecting to our server
            //Below code was covered in single threaded server
            Socket clientSocket = new Socket("localhost", 1234); //server is listening on port 1234, hence we will connet to the server on port 1234.
            //Again port chosen by this client to send data, is randomly chosen by the OS.(Not really randomly chosen, the OS, just chooses the next available port
            //it has, that it can use)

            DataOutputStream writeToServer = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            DataInputStream readFromServer = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

            if(input.equals("show")) {
                writeToServer.writeUTF("show");
                writeToServer.flush();

                int sizeOfSharedData = readFromServer.readInt(); //get the number of items in the sharedData

                for(int i=0; i<sizeOfSharedData; i++) {
                    String listItem = readFromServer.readUTF(); //getan item
                    System.out.println(listItem); //print that item
                }

            } else if(input.equals("add")) {
                writeToServer.writeUTF("add");
                writeToServer.flush();

                System.out.print("Text to store on the server: ");
                String dataToStore = in.nextLine(); //take input from console
                

                writeToServer.writeUTF(dataToStore); //send this data to server
                writeToServer.flush(); //finally flush
            } else {
                System.out.println("Sorry Input was not recognised. Input can either be show or add");
            }

            //finally close all the streams and sockets
            writeToServer.close();
            readFromServer.close();
            in.close();
            clientSocket.close();
            
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Sorry socket could not be made");
        }


    }

}