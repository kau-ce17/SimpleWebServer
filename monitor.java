public class monitor extends Thread{
    // private ServeWebRequest s;
    private String s = "wdadad";
    private int npools;

    public void run(){
        pool obj = new pool(5, );
        obj.mintain_threads();
    }

    public monitor(String s){
        System.out.println(Integer.toHexString(this.s.hashCode()));
    }

    // public monitor(ServeWebRequest s, int npools){
    //     this.s = s;
    //     this.npools = npools;
    // }

    public void set_npools(){

    }

}