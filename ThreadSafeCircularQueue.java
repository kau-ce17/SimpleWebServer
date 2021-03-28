 // https://www.javainuse.com/java/circular_java
// https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Queue.html#add(E)

import java.util.Arrays;
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

    /*
     * make write and reader write and read and the same time 
     * excepet when tail equal head 
     * 
     * how !!!!
     * by using the mutex only when tail and head eaual each other 
     * otherways use write mutex and another reader mutex
     */
    public void enqueue(E item) throws InterruptedException{

        empty.acquire(); //acquier one block
        mutex.acquire(); //acquire WMutex
        this.currnetSize +=1;
        System.out.printf("[ Writer ] %d\n", this.currnetSize);

        //write to the queue
        tail = (tail + 1) % circularQueueElements.length;
        circularQueueElements[tail] = item;
        if (head == -1) {
            head = tail;
        }

        mutex.release(); //release the Semaphore varible
        full.release();
        

        
    }

    /*
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
        this.currnetSize -=1;
        System.out.printf("[ Reader ] %d\n",this.currnetSize);

        //read and remove
        E deQueuedElement;
        deQueuedElement = circularQueueElements[head];
        circularQueueElements[head] = null;
        head = (head + 1) % circularQueueElements.length;

        mutex.release();
        empty.release();
        
        return deQueuedElement;  
    }


    public int getMaxSize() {
        return maxSize;
    }

    public void cleanup(){
        // destory mutexphore and deallocate memory !!!
    }

    public void printAll(){

        for(int i=0; i<maxSize;i++){
            System.out.println(circularQueueElements[i]);
        }

    }

}


