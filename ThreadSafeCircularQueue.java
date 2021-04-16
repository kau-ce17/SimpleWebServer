 // https://www.javainuse.com/java/circular_java
// https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Queue.html#add(E)

import java.util.concurrent.Semaphore;
 
/**
 * @(#)ThreadSafeCircularQueue.java 
 * 
 * @author Team BG01 {1651491: Ahmed Alzbidi, 1741869: Mohammed Alsaggaf, 1740489: Khalid Saqi}
 * 
 * @version 1.00 15/4/2021
 * 
 * This is a Circular Queue that provides a good environment
 * for store the User's requests.
 */

public class ThreadSafeCircularQueue<E> {

    private E[] circularQueueElements;
    private int maxSize; //Circular Queue maximum size. This is a default value
    private int currnetSize;

    private int tail;//rear position of Circular queue(new element enqueued at rear).
    private int head; //front position of Circular queue(element will be dequeued from front).
    
    private String policy;

    private Semaphore mutex = new Semaphore(1); //  binary Semaphore
    private Semaphore empty; // counting semaphore inslized with avalibale elements to write in
    private Semaphore full = new Semaphore(0); // counting semaphore inslized with avalibale elements to read from
    

    //Start the queue
    public ThreadSafeCircularQueue(int maxSize, String policy){
        this.maxSize = maxSize;
        circularQueueElements = (E[]) new Object[this.maxSize];
        empty = new Semaphore(this.maxSize); // counting semaphore inslized with avalibale elements to read from
        tail = -1;
        head = -1;
        this.policy = policy;
    }

   // Add the request into queue
    public E enqueue(E item) throws InterruptedException{


        //Hnadling overloding policies by cheking the queue state either full or empty
        if(!(empty.tryAcquire())){

            //Block policy (Default) //===================================
            if(policy.equals("BLCK")){
                System.out.println("[ Block Policy Activated! ]");
                empty.acquire();
            }
            //Drop tail policy
            else if(policy.equals("DRPT")){
                System.out.println("[ Drop Tail Policy Activated! ]");
                return item;
            }
            //Drop head policy
            else if(policy.equals("DRPH")){
                System.out.println("[ Drop Head Policy Activated! ]");
                E drped_item = this.dropHead(item);
                if(drped_item != null){
                    return drped_item;
                }
            }

        }
        
        mutex.acquire(); //Acquire Mutex
        this.currnetSize +=1; // For debuging
        // System.out.printf("[ Writer ] %d\n", this.currnetSize); // For debuging

        //Write to the queue
        tail = (tail + 1) % circularQueueElements.length;
        circularQueueElements[tail] = item;
        if (head == -1) {
            head = tail;
        }

        mutex.release(); //Release the Semaphore varible
        full.release();
        printAll();

        return null;
        
    }

    // Remove the request from the queue
    public E dequeue()  throws InterruptedException{

        full.acquire(); //Acquier one block
        mutex.acquire();
        this.currnetSize -=1; // For debuging
        // System.out.printf("[ Reader ] %d\n",this.currnetSize); // for debuging

        //Read and remove
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
       
        // Checking the queue if it STILL full or not //===================================
       if(!(full.tryAcquire())){
           return null;
       }
       E droped_req;
        mutex.acquire();
    
        // System.out.printf("[ Drop Head ] %d\n",this.currnetSize); // for debuging
       System.out.println("\n ↓ The queue BEFORE removing the oldest request ↓");
        printAll();

        //Remove oldest request from the queue
        droped_req = circularQueueElements[head];
        circularQueueElements[head] = null;
        head = (head + 1) % circularQueueElements.length;

        System.out.println("↓ The queue AFTER removing the oldest request ↓");
        printAll();
        
        //Add the request
        tail = (tail + 1) % circularQueueElements.length;
        circularQueueElements[tail] = item;
        if (head == -1) {
            head = tail;
        }
        System.out.println("↓ The queue after adding the NEWEST request ↓");
        printAll();
        
        mutex.release();
        full.release();

        return droped_req;
    }

    // Return the maximum size of the queue
    public int getMaxSize() {
        return maxSize;
    }
    // Destory semaphore mutex and deallocate memory
    public void cleanup(){
    circularQueueElements = null;
    mutex = null;
    empty = null;
    full = null;
    }
    // Visualize the content of the queue (requests)
    public void printAll(){
        System.out.println("\n[ The beginning of the queue ]\n");
        for(int i=0; i<maxSize;i++){
            System.out.println(circularQueueElements[i]);
        }

        System.out.println("\n[ The end of the queue ]\n");
    }

}



