import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @(#)MultiThreaded_WebServer.java
 *
 * @author Adapted from SSaurel's Blog
 * by Dr. Abdulghani M. Al-Qasimi
 * @version 1.00 2020/8/7
 *
 * This is a simple web server for teaching purposes.
 * It works as a single threaded application, where,
 * while a client request is being served, other clients
 * will have to wait until that request is finished.
 */
public class MultiThreaded_WebServer{

	// verbose mode
	static final boolean verbose = true;

	// Client Connection via Socket Class
	static Socket connect;

	// connection ID
	static int count = 0;

    // default values (take values form args if avalible)
	static final int PORT = 8085; 
    static final int pool_size = 8085; 
    static final int buffer_size = 8085;
    static final int overload_policy = 8085;  

    // Circular Queue (request data structure)
    private static circularQueue requests;

    static void interactive_console(){
        // change defalut values
    }

	public static void main(String[] args) {
		try {

            if (args.length == 0){ // cold be 1
                interactive_console();
            }

			// create a server listening socket
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
            
			// create one instance of the required task
			ServeWebRequest s = new ServeWebRequest();

            // create one instance of circularQueue with size using args
            if (args.length >= 3){ // check args condition pleease
                requests = new circularQueue<request>(Integer.parseInt(args[3]));
            } else {
                requests = new circularQueue<request>();
            }

			// listen until user halts server execution
			while (true) {
				// accept client connection request
				connect = serverConnect.accept();
				count++;
                
				if (verbose) {
					System.out.println("Connecton " + count + " opened. (" + new Date() + ")");
				}
			}

		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}
}