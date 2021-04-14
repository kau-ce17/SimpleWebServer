import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
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
	static  int    PORT            = 8085; 
    static  int    pool_size       = 5; 
    static  int    buffer_size     = 16;
    static  String overload_policy = "BLCK";  

    // Circular Queue (request data structure)
    private static ThreadSafeCircularQueue<request> buffer;

	// Monitior Thread
	private static monitor Mointor;

	// this will give the user a way of changing some paramters 
	// it will be executed if there are not given paramter
    public static void interactive_console(){
		Scanner input = new Scanner(System.in);
		boolean Start = false;
		while (!Start){
			System.out.printf("Port Number: %d\nPool Size: %d\nBuffer Size: %d\nOverlod Policy: BLCK%s\n", 
											PORT,        pool_size,      buffer_size,        overload_policy);
			System.out.println( "1- Change Port Number\n"    +
								"2- Change Pool Size\n"      +
								"3- Change Buffer Size\n"    +
								"4- Change Overlod Policy\n" +
								"5- Start Server\n"          +
								"press a number: "
			);

			switch(input.nextInt()) {
				case 1:
					System.out.println("Port Number is: ");
					try {
						PORT = input.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Port Number Must be integer");						
					}	
				break;

				case 2:
					System.out.println("Pool Size is: ");
					try {
						pool_size = input.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Pool Size Must be integer");						
					}	
				break;

				case 3:
					System.out.println("Buffer Size is: ");
					try {
						buffer_size = input.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Buffer Size Must be integer");						
					}				
				break;

				case 4:
					System.out.println("1-Block (BLCK)\n"      +
									   "2-Drop_tail (DRPT)\n"  +
									   "3-Drop_head (DRPH)"
									  );
					switch(input.nextInt()) {
						case 1:
							overload_policy = "BLCK";
						break;
						case 2:
							overload_policy = "DRPT";
						break;
						case 3:
							overload_policy = "DRPH";
						break;
						default:
							System.out.println("Wrong Number Nothing Changed!!!");
					}
				break;

				case 5:
					System.out.println("Starting Server");
					Start = true;
				break;

				default:
					System.out.println("Wrong Number Nothing Changed!!!");
			  }
		}
		input.close();
    }

	public static void Paramter_extrator(String[] args){
		if (args.length > 4){
			System.out.println("paramter is more than for 4");
			System.exit(0);
		}
		else if (args.length ==4){
			PORT            = Integer.parseInt(args[0]);
			pool_size       = Integer.parseInt(args[1]); 
			buffer_size     = Integer.parseInt(args[2]);
			overload_policy = args[3];  
		}
		else if(args.length ==3){
			PORT            = Integer.parseInt(args[0]);
			pool_size       = Integer.parseInt(args[1]); 
			buffer_size     = Integer.parseInt(args[2]);

		}
		else if(args.length ==2){
			PORT            = Integer.parseInt(args[0]);
			pool_size       = Integer.parseInt(args[1]); 

		}
		else if(args.length ==1){
			PORT            = Integer.parseInt(args[0]);
		}
		else{
			interactive_console();
		}
	}

	public static void main(String[] args) {
		try {
			Paramter_extrator(args);

			PrintStream log_file = new PrintStream(new File("web-server-log.txt")); // PrintStrea is sync
            System.setOut(log_file); // this might generate race conditiin 
			
			// create a server listening socket
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
            
			// create one instance of the required task
			ServeWebRequest s = new ServeWebRequest();
			
			
            // create one instance of circularQueue with size using args
			buffer  = new ThreadSafeCircularQueue<request>(buffer_size, overload_policy, s);
			Mointor = new monitor(pool_size, s, buffer); 
			Mointor.start();

			// listen until user halts server execution
			while (true) {
				// accept client connection request
				connect = serverConnect.accept();
				count++;
				
				// make rquest object and add it to buffer
				try{
					if(!(buffer.enqueue(new request(connect, count)))){
						//TODO: report in the log file
						s.refuse(connect,count); //for Drop_tail (DRPT) policy, the two other polcies implmented inside the queue
					}
				}
				catch (InterruptedException e){
					System.out.println("Main thread throws exception in buffer.enqueue");
				}
				
                
				if (verbose) {
					System.out.println("Connecton " + count + " opened. (" + new Date() + ")");
				}
			}

		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}
}