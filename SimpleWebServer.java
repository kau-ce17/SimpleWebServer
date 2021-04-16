import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @(#)SimpleWebServer.java
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
public class SimpleWebServer {

	// verbose mode
	static final boolean verbose = true;

	// port to listen for connection
	static final int PORT = 8085;

	// Client Connection via Socket Class
	static Socket connect;

	// connection ID
	static int count = 0;

	public static void main(String[] args) {
		try {
			// create a server listening socket
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

			// create one instance of the required task
			ServeWebRequest s = new ServeWebRequest();

			// listen until user halts server execution
			while (true) {
				// accept client connection request
				connect = serverConnect.accept();
				count++;

				if (verbose) {
					System.out.println("Connecton " + count + " opened. (" + new Date() + ")");
				}

				// manage the client connection
				s.serve(connect, count);
			}

		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}
}