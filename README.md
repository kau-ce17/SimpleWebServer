# MultiThreadedWebServer
16 April, 2021
Version 1.0

## About
This is a multi threaded web server that handle multiple requests. It is build in java using semaphore as synchronization tool. The design components consist of a main thread, worker threads to serve the requests, thread pool to maintain the workers and a monitor thread to handle the abnormal condetions and manitain the number of live threads.

## Authors
* Mohammed Alsaggaf
* Ahmad Alzbidi
* Khalid Saqi

## Design Requirments and Specifications
The design parts based on Main thread, Monitor thread, Pool data structure, Worker Data structure and Circler Queue data structure.
### 1. Main thread
Main thread is responsile to init all the instances and creat all threads then it will accept the requsts by making a socket connection between the clint and the user. The socket connection then will be saved in the circular queue. The main thread would be responsible to handle the overloaded requsts by applying on the overload policies. The main thread designed and inplmented successfully.
### 2. Monitor thread
For the Monitor Thread, it will initialize an object from Thread Pool to gives it start sign to create Workers Threads. Also, Monitor Thread will monitor the live number of workers, report any abnormal conditions as they occur in the server, handle a termination signal (Ctrl-c) when it comes from the keyboard and clean-up before normal termination. All the requirments implemnted successfully.
### 3. Thread Pool
This class will enhance our webserver performance greatly. By make N threads to serve requests. Thread pool will be initialized by the monitor thread using thread pool constructors. It has an inernal implemntaion of maniating the live thread number so that the monitor thread will only call that function. All the requirments implemnted successfully.
### 4. Worker Thread
Worker is class that inherit Thread class. There will be N workers instilled by Thread pool constructor. Worker is responsible of serving one request at time by taking one request object from CircualrQueue Serve that request using ServeWebRequest object (one instance shared between all worker instances). finally removing request object taken from CircualrQueue. All the requirments implemnted successfully.
### 5. Thread Safe Circular Queue
The CircualrQueue class is a generic class that provide First-in-First-out (FIFO) utilities. It is a thread safe data structure using the Semaphore utilities so that protection is handled internally. Choosing CircualrQueue as the data structure that wraps all the request is because it follows the FIFO concept and the elements can be inserted and deleted with O(1). Although linked list provides O(1) insertion and deletion, java arrays implementation is much efficient in terms of speed and memory usage. All the requirments implemnted successfully.

## Difficulties
webserver stress tool 8 have a unexcpected bug which make it gives wrong test results
if any excel file is opened at the testing phase

## Installation and Build Instructions
### Windows
1. Run the ```clean.bat```
2. Run ```run.bat```
3. Ineract with the program to spicfy all the needed parmaers or use the default one, then start the server. 

(**The requests must use the same IP and port number as you specfiy to the server**)
### Linux
1. Remove the txt extension from the Makefile
2. Clean with the make command ```$make clean``` in the command line
3. To build only use the make build command ```$make build``` in the command line
4. to build and run use the make run command ```$make run``` in the command line


## Performance
MultiThreadedWebServer is more responsive and can handle 
more requests than Single threaded web server	
even at the small number of requests.
Also MultiThreadedWebServer is more stable and mintain it threads 
which make it more reliable than Singlethreadwebserver
which can be crash of way easier and make errors than MultiThreadedWebServer





