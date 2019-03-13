import java.util.*;
// one access to thread at a time 
//  not thread that is synchronized, its the method acting on it
// monitor controls object of a thread, even though its not one 
// deadlock - 2 or more threads become interlocked 
import java.util.*; 
public class Person extends Thread{
    int id;
    int arrivalTime; 
    int arrivalFloor;
    int destinationFloor; 
    boolean hasTrolley;
   ElevatorController elevatorController; 
    
     
    Random random = new Random();
    public Person(int id, int arrivalTime ){
		 this.id = id;
		 this.arrivalTime = arrivalTime;
		 this.arrivalFloor = random.nextInt(10);
		 this.destinationFloor = random.nextInt(10); 
		// this.elevatorController = new ElevatorController(); 

    }

    public int getCurrentFloor(){
         return arrivalFloor; 

    }

    
    public String toString(){
		String personalDetails = String.format("id: %s, arrivalFloor: %s, destinationFloor: %s and arrivalTime: %s",id, arrivalFloor, destinationFloor, arrivalTime); 
		return personalDetails; 
		
		
	}
    


    public void run(){
        try{
            System.out.printf("Person with id: %s and arrivalTime:  wants to go from floor %s to floor %s\n",id,arrivalFloor,destinationFloor);  
           
        }
        catch(Exception e){


        }

    }

}


