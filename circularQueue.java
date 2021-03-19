// https://www.javainuse.com/java/circular_java
// https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Queue.html#add(E)

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class circularQueue<E> {

    private int currentSize; //Current Circular Queue Size
    private E[] circularQueueElements;
    private int maxSize; //Circular Queue maximum size // this is a default value

    private int tail;//rear position of Circular queue(new element enqueued at rear).
    private int head; //front position of Circular queue(element will be dequeued from front).
    
    private Semaphore sem = new Semaphore(1);

    public circularQueue(){
        this(16);
    }

    public circularQueue(int maxSize){
        this.maxSize = maxSize;
        circularQueueElements = (E[]) new Object[this.maxSize];
        currentSize = 0;
        tail = -1;
        head = -1;
    }
    //////////////////////////////////////////////////////////////////////////////////////////
   //                                Adding to Queue                                       //
  //////////////////////////////////////////////////////////////////////////////////////////
    public void add(E item) throws QueueFullException, InterruptedException{
        sem.acquire();
        if (isFull()) {
            sem.release();
            // will make a dead thread
           throw new QueueFullException("Queue is Full");
        }
        else {
            tail = (tail + 1) % circularQueueElements.length;
            circularQueueElements[tail] = item;
            currentSize++;
            
            if (head == -1) {
				head = tail;
			}
            sem.release();
        }
    }

    public boolean offer(E item) throws InterruptedException{
        sem.acquire();
        if (isFull()) {
            sem.release();
            // [ missing ] add Queue is full in log file
           return false;
        }
        else {
            tail = (tail + 1) % circularQueueElements.length;
            circularQueueElements[tail] = item;
            currentSize++;
            
            if (head == -1) {
				head = tail;
			}
            sem.release();
            return true;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////
   //                                get from Queue                                        //
  //////////////////////////////////////////////////////////////////////////////////////////
    public E remove()  throws InterruptedException{
        sem.acquire();
        E deQueuedElement;
        if (isEmpty()) {
            // [ missing ]  add to log file that request queue is empty
            sem.release();
            // we could make the thread throws exception but that will lead to dead thread unless it is hamdled
            return null;
        }
        else {
            deQueuedElement = circularQueueElements[head];
            circularQueueElements[head] = null;
            head = (head + 1) % circularQueueElements.length;
            currentSize--;
            sem.release();
            return deQueuedElement;
        }
        
    }
    
    public E poll() throws InterruptedException{
        sem.acquire();
        E deQueuedElement;
        if (isEmpty()) {
            // [ missing ]  add to log file that request queue is empty
            sem.release();
            // we could make the thread throws exception but that will lead to dead thread unless it is hamdled
            return null;
        }
        else {
            deQueuedElement = circularQueueElements[head];
            circularQueueElements[head] = null;
            head = (head + 1) % circularQueueElements.length;
            currentSize--;
            sem.release();
            return deQueuedElement;
        }
    }

    public void element(){

    }

    public E peek(){
      return null;
    }
    //////////////////////////////////////////////////////////////////////////////////////////
   //                                  utils                                               //
  //////////////////////////////////////////////////////////////////////////////////////////
    public boolean isFull() {
        return (currentSize == circularQueueElements.length);
    }
    public boolean isEmpty() {
        return (currentSize == 0);
    }

    public int getSize() {
        return currentSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void cleanup(){
        // destory semphore and deallocate memory !!!
    }

    @Override
    public String toString() {
        return "CircularQueue [" + Arrays.toString(circularQueueElements) + "]";
    }
}


  //////////////////////////////////////////////////////////////////////////////////////////
 //                                  exceptions                                          //
//////////////////////////////////////////////////////////////////////////////////////////

class QueueFullException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QueueFullException() {
        super();
    }

    public QueueFullException(String message) {
        super(message);
    }

}

class QueueEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QueueEmptyException() {
        super();
    }

    public QueueEmptyException(String message) {
        super(message);
    }

}