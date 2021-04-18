/**
 * @(#)worker.java 
 * 
 * @authors Team BG01 {1651491: Ahmed Alzbidi, 1741869: Mohammed Alsaggaf, 1740489: Khalid Saqi}
 * 
 * @version 1.00 15/4/2021
 * 
 * This is a worker class that allows the Thread Pool to ceate workers. 
 * The workers are responsible to serve the user's requests.
 *  
 */
public class worker extends Thread{
    private ThreadSafeCircularQueue<request>   buffer;
    private ServeWebRequest                    s;
    private request                            r;

    public void run(){
        try{
            while (!(Thread.currentThread().isInterrupted())) { // might block at get_request() and not teriminte mayby try and catch is better
                get_request();
                send_resnpones();
			}
            
		}
        catch(InterruptedException e){
            
            System.out.println("Worker thorws an exception"+Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        }
        finally{
            System.out.println("Thread "+ Thread.currentThread().getName() + " has been stoped");
            clean_up();
        }
    }
    /**
     * 
     * Creat workers
     * 
     * @param buffer :ThreadSafeCircularQueue<request>
     * @param s :ServeWebRequest
     */
    public worker(ThreadSafeCircularQueue<request> buffer, ServeWebRequest s){
        this.buffer = buffer;
        this.s      = s;
    }
    
    /**
     * 
     * Take the request from queue
     * @throws InterruptedException
     */
    public void get_request() throws InterruptedException {
        this.r =  buffer.dequeue();
        
    }
    
    /**
     * Serve the requset
     * 
     */
    public void send_resnpones(){
        this.s.serve(this.r.get_Socket(), this.r.get_request_number());
    }
    
    /**
     * clean up
     */
    public void clean_up(){
        this.buffer = null;
        this.s      = null;
        this.r      = null;
    }
}