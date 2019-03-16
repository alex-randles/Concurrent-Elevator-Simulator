import java.util.*;
// one access to thread at a time 
//  not thread that is synchronized, its the method acting on it
// monitor controls object of a thread, even though its not one 
// deadlock - 2 or more threads become interlocked 
import java.util.*; 
public class Person extends Thread{
    int id = 0;
    int arrivalTime;
    int arrivalFloor;
    int destinationFloor;
    int baggageWeight;
    int personWeight;
    ElevatorController elevatorController;
    Random random;

    public Person(ElevatorController elevatorController){
        this.id = id;
        this.arrivalTime = this.elevatorController.getTime();
        this.arrivalFloor = floor();
        this.destinationFloor = floor();
        this.baggageWeight = luggage();
        this.personWeight = passengerWeight();
        this.random = new Random();
        this.elevatorController = elevatorController;
        id++;
    }

    public int getCurrentFloor(){
         return arrivalFloor; 

    }

    public int passengerWeight(){
        return random.nextInt((100-40)+1)+40;
    }


    public int floor(){
        return random.nextInt((10-0)+1)+0;
    }

    public int luggage(){
        return random.nextInt((30-0)+1)+0;
    }

    public String toString(){
		String personalDetails = String.format("id: %s, arrivalFloor: %s, destinationFloor: %s and arrivalTime: %s",id, arrivalFloor, destinationFloor, arrivalTime);
		return personalDetails; 
		
		
	}

    public void run(){
        try{
            //System.out.printf("Person with id: %s and arrivalTime:  wants to go from floor %s to floor %s\n",id,arrivalFloor,destinationFloor);
            elevatorController.makeRequest(this);
            //notifyAll();
        }
        catch(Exception e){


        }

    }

}


