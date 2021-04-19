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
 * @author Team BG01 {1651491: Ahmed Alzbidi, 1741869: Mohammed Alsaggaf, 1740489: Khalid Saqi}
 * 
 * @version 1.00 2021/4/16
 *
 * This is a multi threaded web server that handle multiple requests. It is build in java using semaphore as synchronization tool. 
 * The design components consist of a main thread, worker threads to serve the requests, 
 * thread pool to maintain the workers and a monitor thread to handle the abnormal condetions and manitain the number of live threads.
 *
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
    static  int    pool_size       = Runtime.getRuntime().availableProcessors(); 
    static  int    buffer_size     = 10;
    static  String overload_policy = "BLCK";  

    // Circular Queue (request data structure)
    private static ThreadSafeCircularQueue<request> buffer;

	// Monitior Thread
	private static monitor Mointor;

	/** 
	* This will give the user a way of changing some paramters.
	* It will be executed if there are not given paramter.
	*/
    public static void interactive_console(){
		Scanner input = new Scanner(System.in);
		boolean Start = false;
		while (!Start){
			System.out.printf("\nPort Number: %d\nPool Size: %d\nBuffer Size: %d\nOverlod Policy: %s\n\n", 
											PORT,        pool_size,      buffer_size,        overload_policy);
			System.out.println( "1- Change Port Number\n"    +
								"2- Change Pool Size\n"      +
								"3- Change Buffer Size\n"    +
								"4- Change Overlod Policy\n" +
								"5- Start Server\n\n"          +
								"\npress a number: "
			);

			switch(input.next()) {
				case "1":
					System.out.println("Port Number is: ");
					try {
						PORT = input.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Port Number Must be integer");						
					}	
				break;

				case "2":
					System.out.println("Pool Size is: ");
					try {
						pool_size = input.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Pool Size Must be integer");						
					}	
				break;

				case "3":
					System.out.println("Buffer Size is: ");
					try {
						buffer_size = input.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Buffer Size Must be integer");						
					}				
				break;

				case "4":
					System.out.println("Choose a Policy\n"+
									   "1-Block (BLCK)\n"      +
									   "2-Drop_tail (DRPT)\n"  +
									   "3-Drop_head (DRPH)\n\n" +
									   "Pick an option from 1 to 3:"
									  );
					switch(input.next()) {
						case "1":
							overload_policy = "BLCK";
						break;
						case "2":
							overload_policy = "DRPT";
						break;
						case "3":
							overload_policy = "DRPH";
						break;
						default:
							System.out.println("Enter the Right Number, Nothing Changed!!!");
					}
				break;

				case "5":
					System.out.printf("\nPort Number: %d || Pool Size: %d || Buffer Size: %d || Overlod Policy: %s\n\n", 
												PORT,        pool_size,      buffer_size,        overload_policy);
					System.out.println("Server Started......");
					Start = true;
				break;

				default:
					System.out.println("Enter the Right Number, Nothing Changed!!!");
			  }
		}
		input.close();
    }

	public static void Paramter_extrator(String[] args){
		if (args.length > 4){
			System.out.println("Paramter is more than 4");
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

			// //Record all the server activity output on a Log File 
			PrintStream log_file = new PrintStream(new File("web-server-log.txt"));
            System.setOut(log_file);  
			
			// Create a server listening socket
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
            
			// Create one instance of the required task
			ServeWebRequest s = new ServeWebRequest();
			
			
            // Create one instance of circularQueue with size using args
			buffer  = new ThreadSafeCircularQueue<request>(buffer_size, overload_policy, s);
			Mointor = new monitor(pool_size, s, buffer); 
			Mointor.start();

			// Listen until user halts server execution
			while (true) {

				// Accept client connection request
				connect = serverConnect.accept();
				count++;
				
				// Make rquest object and add it to buffer
				try{
					request req = buffer.enqueue(new request(connect, count));
					if( req != null){
						System.out.printf("[Server] The following request number has been refused: %d\n",req.get_request_number());
						s.refuse(req.get_Socket(),req.get_request_number()); //For Drop_tail (DRPT) policy, the two other polcies implmented inside the queue
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
		}catch (Exception e) {
			System.out.print("System Terminated Before Starting ");
		}
	}
}