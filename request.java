import java.net.Socket;

/**
 * @(#)request.java 
 * 
 * @author Team BG01 {1651491: Ahmed Alzbidi, 1741869: Mohammed Alsaggaf, 1740489: Khalid Saqi}
 * 
 * @version 1.00 15/4/2021
 * 
 * This class will help the workers by providing the request's information to them.
 * 
 */
public class request{
    
    private Socket socket;
    private int    count;

    /**
     * Request constructor
     * 
     */
    public request(Socket s, int count){
        this.socket = s;
        this.count  = count;   
    }
    
    /**
     * Provide the Socket
     * @return Socket
     */
    public Socket get_Socket(){
        return this.socket;
    }
    
    /**
     * Provide the request count number
     * 
     * @return conut number
     */
    public int get_request_number(){
        return this.count;
    }
}