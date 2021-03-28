import java.util.Arrays;

public class pool {
    private worker[]        workers;
    private ServeWebRequest s;
    private ThreadSafeCircularQueue<request>   buffer;
    
    public pool(int nworkers, ServeWebRequest s, ThreadSafeCircularQueue<request>  buffer){
        this.buffer = buffer;
        this.s      = s;
        workers     = new worker[nworkers]; // initlze worker array with size npool

        // making worker threads 
        // set it name 
        // then start it
        for (int i = 0; i < nworkers; i++) {
            workers[i] = new worker(this.buffer, this.s);
            workers[i].setName(Integer.toString(i+1));
            workers[i].start();
        }
    }

    public void mintain_threads(){
        for (int i = 0; i < workers.length; i++) {
            if (workers[i].getState() == Thread.State.TERMINATED) { // we might need to cheack for other states
                workers[i] = new worker(this.buffer, this.s);
                workers[i].setName(Integer.toString(i+1));
                workers[i].start();
            }
        }   
    }

    // not used
    public void threads_states(){
        for (int i = 0; i < workers.length; i++) {
            System.out.println(workers[i].getState());
        }
    }

    public void clean_up(){
        // free memory and change state for all thread to be interrupted
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
