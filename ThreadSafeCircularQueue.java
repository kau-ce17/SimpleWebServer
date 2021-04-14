 // https://www.javainuse.com/java/circular_java
// https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Queue.html#add(E)

import java.util.concurrent.Semaphore;
 
public class ThreadSafeCircularQueue<E> {

    private E[] circularQueueElements;
    private int maxSize; //Circular Queue maximum size // this is a default value
    private int currnetSize;

    private int tail;//rear position of Circular queue(new element enqueued at rear).
    private int head; //front position of Circular queue(element will be dequeued from front).
    
    private String policy;

    private Semaphore mutex = new Semaphore(1); //  binary Semaphore
    private Semaphore empty; // counting semaphore inslized with avalibale elements to write in
    private Semaphore full = new Semaphore(0); // counting semaphore inslized with avalibale elements to read from
    

    public ThreadSafeCircularQueue(int maxSize, String policy){
        this.maxSize = maxSize;
        circularQueueElements = (E[]) new Object[this.maxSize];
        empty = new Semaphore(this.maxSize); // counting semaphore inslized with avalibale elements to read from
        tail = -1;
        head = -1;
        this.policy = policy;
    }

    /** make write and reader write and read and the same time 
     * excepet when tail equal head 
     * 
     * how !!!!
     * by using the mutex only when tail and head eaual each other 
     * otherways use write mutex and another reader mutex
     * 
     */
    public E enqueue(E item) throws InterruptedException{

        printAll();
        if(!(empty.tryAcquire())){

            if(policy.equals("BLCK")){
                System.out.println("[ block policy ]");
                empty.acquire();
            }
            else if(policy.equals("DRPT")){
                System.out.println("[ Drop tail policy ]");
                return item;
            }
            else if(policy.equals("DRPH")){
                System.out.println("[ Drop head policy ]");
                E drped_item = this.dropHead(item);
                if(drped_item != null){
                    return drped_item;
                }
                System.out.println("catchy");
            }

        }
        
        // printAll();
        // empty.acquire(); //acquier one block

        mutex.acquire(); //acquire WMutex
        this.currnetSize +=1; // for debuging
        System.out.printf("[ Writer ] %d\n", this.currnetSize); // for debuging

        //write to the queue
        tail = (tail + 1) % circularQueueElements.length;
        circularQueueElements[tail] = item;
        if (head == -1) {
            head = tail;
        }

        mutex.release(); //release the Semaphore varible
        full.release();
        
        return null;
        
    }
    /** 
     * make write and reader write and read and the same time 
     * excepet when tail equal head 
     * 
     * how !!!!
     * by using the mutex only when tail and head eaual each other 
     * otherways use write mutex and another reader mutex
     */
    public E dequeue()  throws InterruptedException{

        full.acquire(); //acquier one block
        mutex.acquire();
        this.currnetSize -=1; // for debuging
        System.out.printf("[ Reader ] %d\n",this.currnetSize); // for debuging

        //read and remove
        E deQueuedElement;
        deQueuedElement = circularQueueElements[head];
        circularQueueElements[head] = null;
        head = (head + 1) % circularQueueElements.length;

        mutex.release();
        empty.release();
        
        return deQueuedElement;  
    }

    /** 
     * The main thread should drop the oldest request in the queue 
     * that is not currently being processed by a thread (this is the request in the front of the queue), 
     * and add the new request to the end of the queue.
     */
    public E dropHead(E item) throws InterruptedException{
        
       if(!(full.tryAcquire())){
           return null;
       }
       E droped_req;
        mutex.acquire();
    
        System.out.printf("[ Drop Head ] %d\n",this.currnetSize); // for debuging

        
        //remove
        droped_req = circularQueueElements[head];
        circularQueueElements[head] = null;
        head = (head + 1) % circularQueueElements.length;

        printAll();


        //add
        tail = (tail + 1) % circularQueueElements.length;
        circularQueueElements[tail] = item;
        if (head == -1) {
            head = tail;
        }
        printAll();
        
        mutex.release();
        full.release();

        return droped_req;
    }


    public int getMaxSize() {
        return maxSize;
    }

    public void cleanup(){
        // destory mutexphore and deallocate memory !!!
        // same as LAB 10
    }

    public void printAll(){
        System.out.println("[ this our queue ]");
        for(int i=0; i<maxSize;i++){
            System.out.println(circularQueueElements[i]);
        }

        System.out.println("[ end of queue ]");
    }

}



