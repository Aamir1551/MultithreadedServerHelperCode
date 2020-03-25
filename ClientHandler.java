import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//ClientHandler is a thread, by ClientHandler im talking about the class (actually not even the class, just the run method inside this class) and not the file.
//Its a thread since its only a small piece of code that can be scheduled by the CPU and all the instructons inside this thread can be done in isolation with 
//the other code there may be running (this also includes other threads, since this thread does not depend upon other threads)

class ClientHandler implements Runnable {

    private Socket socketConnectionWithClient = null; //make this socket connection private, since each thread will have its own socket, since
    //each socket will be talking to its own unique client.


    //In general, static variables are used to denote variables that are shared by all instances of a class. Since each thread (in our case, ClientHandler), 
    //is an instance the static variables they have will allow some form of data sharing between them. Hence to have shared memory between our threads (or clients)
    //we can make use of static variables

    public static List<String> sharedData = new ArrayList<String>(); //the keyword static, is important here, to allow for the data in this variable to be shared among all threads
    // and hence, this data can then be shared among all clients currently connected.


    public ClientHandler(Socket socketConnectionWithClient) { //contructor
        this.socketConnectionWithClient = socketConnectionWithClient;
    }


    @Override
    public void run() {
        //The following code occurs when the thread starts

        //How the thread will work

        // 1) Recieve either a show or add command from client
        // 2) If show command is recieved, print out the contents of the sharedData
        // 3) If add command is recieved, add some data to the sharedData
        


        try {
            
            DataOutputStream writeToClient = new DataOutputStream(new BufferedOutputStream(socketConnectionWithClient.getOutputStream()));
            DataInputStream readFromCLient = new DataInputStream(new BufferedInputStream(socketConnectionWithClient.getInputStream()));

            String showOrAddCommand = readFromCLient.readUTF();

            if(showOrAddCommand.equals("show")) {
                //if its a show command, we need to output all the contents of the sharedData
                // 1) Client is expecting us to first write an integer, describing the length of the array
                // 2) Then we must write the contents of the array one by one

                synchronized(this) {
                    writeToClient.writeInt(sharedData.size()); //sending the size of the list to the client
                    writeToClient.flush();

                    for(int i=0; i<sharedData.size(); i++) {
                        writeToClient.writeUTF(sharedData.get(i)); //sending each item to client
                        writeToClient.flush();
                    }
                }

                //synchronized is used to only allow one thread at a time to do this particular block of code.
                //This was done becuse imagine the scenario:
                // 1) Client 1, has connected to a server, and is handled by a newly created thread, lets call it thread 1
                // 2) Client 2, now connects and is handled by thread 2, again newly created by the server, to handle client 2
                // 3) Client 1, now does the show command. So Client 1 wants to see the contents of the sharedData
                // 4) Client 2, now wants to add data
                // 5) Since both threads run asynchronously(not at the same time), once thread can complete before another since they are not run
                // one after another, since they happen simultanously within the CPU. Hence, by chance, thread 2 could instead add the data that client 2 wanted to add
                // even before, client 1 recieved any data.
                // This means that when client 1, now recieves the content of the array, Client 1 will recieve the sharedData array and also the newly added content by 
                // client 2, even though client 1 actually connected before client 2, and hence should not recieve the data added by client 2

                //In this case this isin't too much of a problem. However, allot of times this can lead to allot of unexpected errors, in ways we don't see.
                //Hence since many threads will be accessing sharedData, we must synchronize any access to it, which basicallly blocks any 2 threads, 
                //doing the code inside it at any time. This ensures what we call thread safety. Hance access to any shared data in general, should be
                //synchronized to stop any two threads from accessing the same data at the same time.

            } else if(showOrAddCommand.equals("add")) {
                // If its an add command, we need to take in the input that the client is sending that they would like to add to the array
                // 1) Client is sending data, so we must recieve it
                // 2) We then add this data to our sharedData array

                String dataToAddToSharedDataList = readFromCLient.readUTF();
                
                synchronized(this) {
                    sharedData.add(dataToAddToSharedDataList);
                }

            } else {
                //Something must have gone wrong, since it must either be a show or add command
                System.out.println("Somehting went wrong...");
            }

            writeToClient.close();
            readFromCLient.close();
            socketConnectionWithClient.close();

        } catch (Exception e) {
            //TODO: handle exception
        }

    }

}