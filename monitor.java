public class monitor extends Thread{
    private ServeWebRequest s;
    private circularQueue   buffer;
    private pool            pool_of_workers;

    // (1) Maintain a constant number of live worker threads in the thread-pool,            (( DONE )) 
    // (2) Report any abnormal conditions as they occur in the server, 
    // (3) Handle a termination signal (Ctrl-c) when it comes from the keyboard, and 
    // (4) Clean-up before normal termination.
    public void run(){
        // this not the correct solution just for testing ressons
        // this hook will start when terimantion occur 
        Thread ref_moinitor_Thread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() { 
                pool_of_workers.clean_up();
                clean_up(ref_moinitor_Thread);
            }
        });

        while (!Thread.interrupted()) {
            pool_of_workers.mintain_threads();
        }

    }

    public monitor(ServeWebRequest s, circularQueue buffer){
        this.s          = s;
        this.buffer     = buffer;
        pool_of_workers = new pool(this.s, this.buffer);
    }

    public monitor(int npools, ServeWebRequest s, circularQueue buffer){
        this.s          = s;
        this.buffer     = buffer;
        pool_of_workers = new pool(npools ,this.s, this.buffer);
    }

    public void clean_up(Thread ref_moinitor_Thread){
        this.s               = null;
        this.buffer          = null;
        this.pool_of_workers = null;
        ref_moinitor_Thread.interrupt();
    }

}