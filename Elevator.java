import java.util.*;
import java.util.concurrent.PriorityBlockingQueue; 

public class Elevator implements Runnable{
    int weight; // Weight limit is 20 people, trolley weight is 2 people 
    int currentFloor;
    PriorityQueue<Person> queue = new PriorityQueue<>(new Comparator<Person>(){


    @Override
    public int compare(Person p1, Person p2) {
        return Integer.compare(p1.arrivalFloor, p2.arrivalFloor);
    } 
    });  // Sorts the queue by the lowest arrival floor

    public Elevator(){
         // Constructor to set elevator variables
		 this.weight = 0;
		 this.currentFloor = 0;
		 this.queue = queue;

    
    
    }
    
    public void addRequest(Person p1){
       this.queue.add(p1);
       updateWeight(p1); 
       updateFloor(p1); 

    }

    public void updateWeight(Person p1){
       // If user has trolley, add trolley weight 2 and then person weight 1 else just person weight 1
       if (p1.hasTrolley == true){
           this.weight+=3;
        }
        else{
            this.weight+=1;
        }

    } 

    public void updateFloor(Person p1){
         this.currentFloor = p1.arrivalFloor; 

    }
    public boolean checkWeightLimit(){
       // Checks if weight is greater then 20 people 
       if (this.weight>20){
           return false;
        }
        return true; 

    } 
    public void run(){
       try{
        System.out.printf("The current weight is: %s people, current floor: %s  Queue is [ ",weight,currentFloor);
        while(!queue.isEmpty()){
           Person p = queue.poll();
           System.out.printf("id: %s arrivalTime: %s arrivalFloor: %s destinationFloor: %s , ",p.id,p.arrivalTime,p.arrivalFloor,p.destinationFloor); 

        }
        System.out.print(" ] \n"); 
       }
       catch(Exception e){



       }
   }

    






}
