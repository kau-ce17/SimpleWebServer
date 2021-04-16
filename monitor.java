public class monitor extends Thread{
    private ThreadSafeCircularQueue<request>    buffer;
    private ServeWebRequest                     s;
    private pool                                pool_of_workers;

    // (1) Maintain a constant number of live worker threads in the thread-pool,            (( DONE )) 
    // (2) Report any abnormal conditions as they occur in the server,
    //              look at java.util.logger/logging                                        ((not clear))
    // (3) Handle a termination signal (Ctrl-c) when it comes from the keyboard, and        ((partially DONE))
    // (4) Clean-up before normal termination.                                              ((partially DONE))
    public void run(){
        // this not the correct solution just for testing ressons
        // this hook will start when terimantion occur 
        Thread ref_moinitor_Thread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() { 
                System.out.println("I will cleanup then terimninate");
                pool_of_workers.clean_up();
                buffer.cleanup();
                clean_up(ref_moinitor_Thread);
                // main thread not cleaned take the reference for it then clean up(call a method in main thread)
                // clean up buffer and destroy semphores (lab 10)
            }
        });

        while (!Thread.interrupted()) {
            pool_of_workers.mintain_threads();
        }

    }

    public monitor(int npools, ServeWebRequest s, ThreadSafeCircularQueue<request> buffer){
        this.buffer     = buffer;
        this.s          = s;
        pool_of_workers = new pool(npools , this.s, this.buffer);
    }

    public void clean_up(Thread ref_moinitor_Thread){
        this.s               = null;
        this.buffer = null;
        this.pool_of_workers = null;
        ref_moinitor_Thread.interrupt();
    }

}