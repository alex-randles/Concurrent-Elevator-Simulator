import java.util.*;

public class Person implements Runnable{
    int id;
    int arrivalTime; 
    int arrivalFloor;
    int destinationFloor; 
    boolean hasTrolley;

    
    public Person(int id, int arrivalTime, int arrivalFloor, int destinationFloor, boolean hasTrolley){
		 this.id = id;
		 this.arrivalTime = arrivalTime;
		 this.arrivalFloor = arrivalFloor;
		 this.destinationFloor = destinationFloor; 
         this.hasTrolley = hasTrolley; 
    
    }
    
    public int getCurrentFloor(Person p1){
         return p1.arrivalFloor; 

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

