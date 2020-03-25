import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class OptimisedServer {


    //REVIEW: A thread is a small piece of instruction plus by some amount of cpu resources it will need to accomplish this small piece of code

    //The Server we saw before had one problem. It can handle as many clients that connects. This could result in a problem, since
    //for every client that connects, we create a thread to handle this client. Now since each thread requires some CPU resources, we
    //may end up consuming allot of CPU resources at any one time, which couldd result in our server crashing, or even worse overheating
    //or damaging equipment. Hence, we would like to have a limit on the number of threads we have

    //Another issue is, that every time a thread is made, it takes along time to make one, and then once the thread does its task, its then destroyed.
    //This is a waste of time, in situation where we'll be making allot of threads that need to do the same thing over and over again. Hence
    //why not reuse the same old thread instead of destroying it, since the instructions this thread is going to do is the same, and hence the resources
    //this thread will require will also be the same.


    //This can all be achieved by using a fixed pool of threads, and every time a thread is required to accompish the task, the thread can be taken from the pool
    //and conce the thread has accomplished its task, it can then be returned back to the pool, removing the need of destroying the thread.

    


    public static void main(String args[]) {
        
        ExecutorService executer = Executors.newFixedThreadPool(20); //create a fixed pool thread of size 20
        try {
            ServerSocket serverSocket = new ServerSocket(1234); //server is again listening on port 1234
            while(true) {
                Socket clientSocketConnection = serverSocket.accept();
            
                //once client has connected, create thread to handle this client
                //thread is passed the newly created socket inorder to interact with this specific client

                executer.execute(new ClientHandler(clientSocketConnection)); //this basically tells one of the threads in the pool to execute the code (in the run method) of this class
            }
        } catch (Exception e) {
            System.out.println("Server Socket Error");
            return;
        }
    }


}