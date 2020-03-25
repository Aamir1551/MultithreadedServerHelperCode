import java.net.ServerSocket;
import java.net.Socket;

class Server {

    public static void main(String[] args) {

        //A multithreaded server handles the case for when a server is expected to handle multiple clients
        
        //WHAT IS A THREAD?
        //Before, we have been programming using only one thread (Usually called the main thread), hence all programs written before come under what is called
        //single threaded programs, since they only use 1 thread.

        //Now since, we will be handling multiple clients, we need to change the way our programs run, so that we can talk to multiple clients simultanously

        //A thread is basically some small amount of code, that the cpu can handle by itself. Thats it
        //Usually, this amount of code (the thread), needs to be ran so many times.
        //So what we do is create loads of threads (by creating loads of threads we in essence have created so many instances of this small piece of code)
        //These small amount of code can then be executed by the computer simulatously, using its multiprocessing power

        //How this plays out with out multiple clients, is that in essence if we think about how we handle each client, each client is handled the same
        //We treat each client the same, hence the code executed to handle each client is the same. This means that to handle every new client
        //we can just perform the same code that we did for all the cleints before. Hence, since the code is the same, we can just create a new
        //thread for every new client that wants to connect with our server, and that is exactly what we do, to handle multiple clients.

        //To Be more clear

        //A single threaded server
        // 1) Server turns on and listens and waits for a client to connect
        // 2) Client turns on and connects with the server
        // 3) Server accepts the connection
        // 4) Server then interacts with client and gives whatever it needs
        // 5) If a 2nd user decided to connect, the 2nd user would be ignored by this server.

        //A multithreaded server allows us to handle multiple clients simultanously
        // 1) Server turns on and listens and waits for a client to connect (using the main thread)
        // 2) Client turns on and connects with the server
        // 3) Server accepts the connection and creates a new thread. This thread alone is used to handle this client. Again a thread is a small piece of code that
        //the CPU can do. The CPU then decideds to run this code, along with the main thread
        // 4) This thread then interacts with client and gives whatever it needs
        // 5) If a 2nd user decided to connect with the server, a new thread is created to handle this client. (Giving us a total of 3 threads, since
        //we have one main thread, and 2 threads that are used to handle the 2 clients we now have)
        // 6) Once client have been handled by the thread, the thread is then destroyed.


        //IMPORTANT: A thread is more than just a small piece of code. Its basically the code plus by cpu resources that are required to run this code.
        //This includes the memory, ALU's and registers required to run this code. Hance many threads created would mean more and more resources of the
        //cpu are being used. This can be good in cases, where allot of the CPU is basically idle, but bad in places, where some threads require allot of
        //resources, leaving very little resources for other threads, hence increaseing the total time taken by all threads to execute their code

        //IMPORTANT 2: Using more threads DOES NOT speed up your program (99% of the time)


        ClientHandler.sharedData.add("Some Initial Data"); //Adding some initial data to the sharedData
        //I can do the above following line of code, since sharedData is "public" static, hence this data is visibile outside the class

        try {
            
            ServerSocket serverSocket = new ServerSocket(1234); //this is the port our server is listening on. Again no need to provide any sort of IPaddress
            //since IPaddress of this server is automatically the ip address of the computer this server is ran

            while(true) {

                Socket connectionWithClientSocket = serverSocket.accept(); //wait until we recieve a connection to a client

                //we've now recieved a connection to a client
                //Lets now create a thread to handle this client

                ClientHandler clientHandlerObject = new ClientHandler(connectionWithClientSocket); //create an instance of CLientHandler
                Thread  clientHandlerThread = new Thread(clientHandlerObject); //now create the thread by providing it a runnable -- 
                //We are able to pass in our object into the Thread constructor  (new thread(...)), since clientHandlerThread implements runnable
                //Implementing runnable just means, having a run method.

                clientHandlerThread.start(); // This function forces the run method to start on the clientHandlerThread.
                //It knowns it can do this, since clientHandlerObject itself has the run method.
                //Hence, when clientHandlerThread.start() happens, it runs the run function of the clientHandlerObject.

                //Thread has now been created and is running

                //the main thread however, returns to the beginning of the while loop, and starts listening for more clients to connect

            }

            //serverSocket.close() // no need to have this line of code, since while(true) -- will happend foreever, so no need to close the ServerSocket

        } catch (Exception e) {
            //TODO: handle exception
        }


    }

}