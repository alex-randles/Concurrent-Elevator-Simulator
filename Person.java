import java.util.*;
import java.lang.Math;

public class Person implements Runnable{
    int id = 0;
    int arrivalTime; 
    int arrivalFloor;
    int destinationFloor; 
    int baggageWeight;
    int personWeight;

    public Person(){
		 this.id = id;
		 this.arrivalTime = arrivalTime();
		 this.arrivalFloor = floor();
		 this.destinationFloor = floor();
         this.baggageWeight = luggage();
         this.personWeight = passengerWeight();
         id++;
    }

    public Time arrivalTime(){
         randomMinute = (Math.random()*((15-0)+1))+0;
         randomSecond = (Math.random()*((60-0)+1))+0;
         Time time = new Time(16, randomMinute, randomSecond);
         return Time;

    }

    public int floor(){
        return (Math.random()*((10-0)+1))+0;
    }

    public int luggage(){
        return (Math.random()*((30-0)+1))+0;
    }
    
    public int getCurrentFloor(Person p1){
         return p1.arrivalFloor; 

    }

    public int passengerWeight(){
        return (Math.random()*((100-40)+1))+40;
    }
    
    public void run(){
        try{
            System.out.printf("id: %s arrivalTime: %s arrivalFloor: %s destinationFloor: %s\n",id,arrivalTime,arrivalFloor,destinationFloor);  
            Thread.sleep(100);
            System.out.printf("id: %s arrivalTime: %s arrivalFloor: %s destinationFloor: %s\n",id,arrivalTime,arrivalFloor,destinationFloor);  

        }
        catch(Exception e){
        
        
        }
    
    }

}

