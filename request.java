import java.net.Socket;
public class request{
    
    private Socket socket;
    private int count;

    public request(Socket s, int count){
        this.socket = s;
        this.count  = count;   
    }

    public Socket get_Socket(){
        return this.socket;
    }

    public int get_request_number(){
        return this.count;
    }
}