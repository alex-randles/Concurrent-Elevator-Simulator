import java.util.*;
// this person class is purely for testing, not the final implementation 
// it only has the id, arrival floor and destination floor at the moment
// they will be randomised later 


// one access to thread at a time 
//  not thread that is synchronized, its the method acting on it
// monitor controls object of a thread, even though its not one 
// deadlock - 2 or more threads become interlocked 
public class Person extends Thread{
    int id;
   // int arrivalTime; 
    int arrivalFloor;
    int destinationFloor; 
    boolean hasTrolley;
    
    

    
    public Person(int id, int arrivalFloor, int destinationFloor){
		 this.id = id;
		 //this.arrivalTime = arrivalTime;
		 this.arrivalFloor = arrivalFloor;
		 this.destinationFloor = destinationFloor; 
        // this.hasTrolley = hasTrolley; 

    }

    public int getCurrentFloor(){
         return arrivalFloor; 

    }

    
    public String toString(){
		String personalDetails = String.format("Person with id: %s, arrivalFloor: %s, destinationFloor: %s",id, arrivalFloor, destinationFloor); 
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


