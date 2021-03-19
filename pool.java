import java.util.Arrays;

public class pool {
    private worker[] workers;
    private ServeWebRequest s;
    
    public pool(){
        this(2);
    }

    public pool(int npools){
        workers = new worker[npools]; // initlze worker array with size npool

        // making worker threads 
        // set it name 
        // then start it
        for (int i = 0; i < npools; i++) {
            workers[i] = new worker();
            workers[i].setName(Integer.toString(i+1));
            workers[i].start();
        }
    }

    public void mintain_threads(){
        for (int i = 0; i < workers.length; i++) {
            if (workers[i].getState() == Thread.State.TERMINATED) { // we might need to cheack for other states
                workers[i] = new worker();
                workers[i].setName(Integer.toString(i+1));
                workers[i].start();
            }
        }   
    }

    public void cleanup(){
        // free memory and change state for all thread to be interrupted
        for (int i = 0; i < workers.length; i++) {
            workers[i].interrupt();
        }
    }

    public void set(ServeWebRequest s){
        this.s = s;
    }


    public void threads_states(){
        for (int i = 0; i < workers.length; i++) {
            System.out.println(workers[i].getName());
        }
    }

    @Override
    public String toString() {
        return "CircularQueue [" + Arrays.toString(workers) + "]";
    }

}
