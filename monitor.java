/**
 * @(#)monitor.java 
 * 
 * @author Team BG01 {1651491: Ahmed Alzbidi, 1741869: Mohammed Alsaggaf, 1740489: Khalid Saqi}
 * 
 * @version 1.00 15/4/2021
 * 
 * Monitor the life of the threads in the thread pool,
 * replacing any dead threads by creating new ones 
 * if necessary to maintain the specified pool-size.
 * 
 */


public class monitor extends Thread{
    private ThreadSafeCircularQueue<request>    buffer;
    private ServeWebRequest                     s;
    private pool                                pool_of_workers;

    
    // (2) Report any abnormal conditions as they occur in the server,
    //              look at java.util.logger/logging                                        ((DONE))
    // (3) Handle a termination signal (Ctrl-c) when it comes from the keyboard, and        ((DONE))
    // (4) Clean-up before normal termination.                                              ((DONE))
    public void run(){
        
        // This hook will start when terimantion occur 
        Thread ref_moinitor_Thread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() { 
                System.out.println("I will cleanup then terimninate");
                pool_of_workers.clean_up();
                buffer.cleanup();
                clean_up(ref_moinitor_Thread);
            }
        });
        // Mintain the Workers 
        while (!Thread.interrupted()) {//===================================
            pool_of_workers.mintain_threads();
        }

    }
    //Create Monitor thread
    public monitor(int npools, ServeWebRequest s, ThreadSafeCircularQueue<request> buffer){
        this.buffer     = buffer;
        this.s          = s;
        pool_of_workers = new pool(npools , this.s, this.buffer);
    }
    // Clean up the Monitor thread
    public void clean_up(Thread ref_moinitor_Thread){
        this.s               = null;
        this.buffer = null;
        this.pool_of_workers = null;
        ref_moinitor_Thread.interrupt();
    }

}