import java.util.*; 
import java.lang.*; 
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*; 
import java.util.Random;
public class Main{
	
	public  static void main(String[] args) throws InterruptedException{
		
	  ElevatorController elevatorController = new ElevatorController(); 
	  Elevator elevator = new Elevator(elevatorController);
	  
	  // start the elevator and wait for request
	  elevator.start(); 

	  
	  
	  // create thread pool 
	  ExecutorService executor = Executors.newFixedThreadPool(4);
	  Person p1 = new Person(elevatorController);
	  Person p2 = new Person(elevatorController);
	  Person p3 = new Person(elevatorController); 
	  Person p4 = new Person(elevatorController); 

	 // p1.start(); 
	 // p2.start(); 
	 // p3.start();
	  Person[] people = new Person[]{p1,p2,p3,p4}; 
	  
	  for (Person p: people){
		  System.out.println(p); 
		  executor.submit(p); 
	  }
	
	

	  executor.shutdown();
	  executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		

	}
	
}








