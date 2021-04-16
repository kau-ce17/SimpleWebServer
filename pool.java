import java.util.Arrays;

/**
 * @(#)pool.java 
 * 
 * @author Team BG01 {1651491: Ahmed Alzbidi, 1741869: Mohammed Alsaggaf, 1740489: Khalid Saqi}
 * 
 * @version 1.00 15/4/2021
 * 
 * The Tread Pool class will enhance the webserver performance greatly, 
 * by make N threads to serve more requests.
 * 
 */

public class pool {
    private ThreadSafeCircularQueue<request>   buffer;
    private ServeWebRequest                    s;
    private worker[]                           workers;
    
    //Create Thread Pool
    public pool(int nworkers, ServeWebRequest s, ThreadSafeCircularQueue<request>  buffer){
        this.buffer = buffer;
        this.s      = s;
        workers     = new worker[nworkers]; // initlze worker array with size npool

       // Making worker threads, set its name then start it
        for (int i = 0; i < nworkers; i++) {
            workers[i] = new worker(this.buffer, this.s);
            workers[i].setName(String.format("[ worker %d ]",i+1));
            workers[i].start();
        }
    }

    /**  
     * Mintaining the workers by checking there state.
     * If there is a Terminated worker, then another worker will initialize. 
     *  
     */
    
     //===================================
    public void mintain_threads(){
        for (int i = 0; i < workers.length; i++) {
            if (workers[i].getState() == Thread.State.TERMINATED) { // we might need to cheack for other states
                workers[i] = new worker(this.buffer, this.s);
                workers[i].setName(String.format("[ worker %d ]",i+1));
                workers[i].start();
            }
        }   
    }

    // not used //===================================
    public void threads_states(){
        for (int i = 0; i < workers.length; i++) {
            System.out.println(workers[i].getState());
        }
    }
    
    // Free memory and change state for all thread to be interrupted
    public void clean_up(){
        
        for (int i = 0; i < workers.length; i++) {
            workers[i].interrupt(); // signal to thread to cleanup and stop its task
            workers[i] = null;
        }
        this.workers = null;
        this.s       = null;
        this.buffer  = null;
    }

    @Override
    public String toString() {
        return "workers [" + Arrays.toString(workers) + "]";
    }

}
