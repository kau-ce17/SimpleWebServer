public class worker extends Thread{
    // worker thread shall remove and serve exactly one request from the list.
    public void run(){
        System.out.println(Thread.currentThread().getName());
        test qqq = new test();
        System.out.println(qqq.n);
    }

    public worker(){
    }

    public void get_request(){

    }

    public void send_resnpones(){
    }

    public void remove_request(){

    }
}