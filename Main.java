import java.util.*; 
import java.lang.*; 
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.Random;
public class Main{
	
	public  static void main(String[] args) throws InterruptedException{
		ElevatorController elevatorController = new ElevatorController(); 
		Elevator elevator = new Elevator(elevatorController);
		ElevatorController elevatorController1 = new ElevatorController(); 
		Elevator elevator1 = new Elevator(elevatorController1);		
		
		//Person  p1 = new Person(1,3,1,2,elevatorController); // (id, arrivalFloor, destinationFloor) 9
		//Person  p2 = new Person(2,3,8,9);
		//Person  p3 = new Person(3,4,5,99);
	    //p1.start(); 
	    elevator.start(); 
	    Random random = new Random();
	    int i =0; 
	    /*while(elevatorController.getTime()< 100){
             //elevatorController.currentTime += random.nextInt(10);
            //System.out.printf("Arrival time: %s\n", randomArrival); 
            int randomArrival = random.nextInt(10); 
			Person person = new Person(i,randomArrival);
			//person.start();
			elevatorController.makeRequest(person); 
			TimeUnit.SECONDS.sleep(randomArrival);
			//Thread.sleep(randomArrival); 
			i = i + 3; 
		} */
		// A person with a smaller arrival time may be served first 
		/*int time = 0 ;
		int id = 0; 
		while(time < 100){
			Person person = new Person(id,time);
			int randInt = random.nextInt(10);
			time += randInt;
			elevatorController.makeRequest(person); 
			Thread.sleep(randInt); 
			id ++; 
		}*/
		
		/*Person[] people = new Person[5];
		int id = 1 ;
		for (int x = 0; x <5 ; x++){
			int rand = random.nextInt(10);
			people[x] = new Person(id,rand, elevatorController);
			id++;
		}
		
		for ( Person p : people){
			//p.start(); 
			p.start();  */
			
	//	
	  
	  
	  Person p1 = new Person(1,3,6,1,elevatorController);
	  Person p2 = new Person(2,1,7,4,elevatorController);
	  Person p3 = new Person(3,2,1,2,elevatorController); 
	  p1.start(); 
	  p2.start(); 
	  p3.start(); 
  
	
	
	}
	
	
	  	
		
		//Person  p4 = new Person(3,3,2);
		//p1.start();
		//p2.start(); 
		
	    //randomArrival = (Math.random()*((5-0)+1))+0;
		//elevatorController.makeRequest(p1); 
		//elevatorController.makeRequest(p2); 
		//elevatorController.makeRequest(p3); 
		// elevatorController.makeRequest(p4); 

	}
	









