public class worker extends Thread{
    private ThreadSafeCircularQueue<request>   buffer;
    private ServeWebRequest s;
    private request         r;

    public void run(){
        try{
            while (true) { // might block at get_request() and not teriminte mayby try and catch is better
                get_request();
                send_resnpones();
			}
		}
        catch(InterruptedException e){
            System.out.println("Worker thorws an exception"+Thread.currentThread().getName());
        }
        clean_up();
        System.out.println("Thread "+ Thread.currentThread().getName() + " has been stoped");
    }

    public worker(ThreadSafeCircularQueue<request> buffer, ServeWebRequest s){
        this.buffer = buffer;
        this.s      = s;
    }

    public void get_request() throws InterruptedException {
        this.r =  buffer.dequeue();
        
    }

    public void send_resnpones(){
        this.s.serve(this.r.get_Socket(), this.r.get_request_number());
    }

    public void clean_up(){
        this.buffer = null;
        this.s      = null;
        this.r      = null;
    }
}