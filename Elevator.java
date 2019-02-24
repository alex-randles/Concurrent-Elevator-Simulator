import java.util.*;
import java.util.concurrent.PriorityBlockingQueue; 
import java.util.concurrent.ConcurrentLinkedQueue; 
import java.util.concurrent.ConcurrentHashMap; 

public class Elevator implements Runnable{
    int weight; // Weight limit is 20 people, trolley weight is 2 people 
    int currentFloor;
    //public ConcurrentLinkedQueue<Person> floorQueue = new ConcurrentLinkedQueue<Person>(); 
     ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Person> > floorQueue = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
     ConcurrentLinkedQueue<Person> inElevator = new ConcurrentLinkedQueue<Person>(); 
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
         this.floorQueue = floorQueue; 

    
    
    }
    
    public void addRequest(Person p1){
       addFloorRequest(p1); 
       this.queue.add(p1);
       updateWeight(p1); 
       updateFloor(p1); 

    }

    public void addFloorRequest(Person p1){
       // This adds a person to the line on their arrival floor 
       // Hashmap entry for each floor with a queue 
       int floorNum = p1.arrivalFloor; 
       ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
       // Check if there is a queue for their floor otherwise create one 
       if (floorQueue.containsKey(floorNum)){
           tmp = floorQueue.get(floorNum);
           tmp.add(p1); 
           floorQueue.put(floorNum, tmp);
       }
       else{
           tmp.add(p1); 
           floorQueue.put(floorNum, tmp); 
        } 

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

    // try clause needed for making thread sleep 
    public void makeThreadSleep(int length){
        try{
           Thread.sleep(length); 
        } 
        catch(InterruptedException e){
            e.printStackTrace(); 
        }
    }


    public void startElevator(){
        // Carries out the elevators request in order of priority 

        // find smallest floor with a queue as elevator is initally on floor 1 
        int minFloor = 10; 
        Enumeration<Integer> keys = floorQueue.keys();
        while (keys.hasMoreElements()){
            int nextKey = keys.nextElement(); 
            if (nextKey < minFloor) {
                minFloor = nextKey; 
            }
         }
       
       // get queue for minimum floor 
       ConcurrentLinkedQueue<Person> tmpQueue = floorQueue.get(minFloor);
       Person tmpPerson = tmpQueue.poll();
       int currentFloor = minFloor; 

       // the destination floors of people in the elevator 
       ArrayList<Integer> destinationFloors = new ArrayList<Integer>();
       destinationFloors.add(tmpPerson.destinationFloor);

       // iterate request queue until all request have been fulfilled 
       while ((!floorQueue.isEmpty()) && (!destinationFloors.isEmpty())){
            Collections.sort(destinationFloors);
            System.out.println(destinationFloors); 
           System.out.printf("Person with id: %s is in the elevator on floor %s\n", tmpPerson.id,tmpPerson.arrivalFloor); 

           // update current floor to the persons arrival floor 
           currentFloor = tmpPerson.arrivalFloor;  

           // if the queue associated with the floor is now empty, remove that entry from the hashmap with all the floor queues 
           if(tmpQueue.isEmpty()){
             floorQueue.remove(currentFloor); 
           }

           // is the elevator going up or down
           if(tmpPerson.destinationFloor > currentFloor){
            System.out.println("The elevator is going upwards");
            // sleep between floors
            makeThreadSleep(100); 
            currentFloor++; 
           }
           else{
            System.out.println("The elevator is going downwards");
            // sleep between floors
            makeThreadSleep(100); 
            currentFloor--;   
            }

            System.out.printf("Current floor is %s\n",currentFloor); 
            tmpQueue = floorQueue.get(currentFloor); 
            tmpPerson = tmpQueue.poll(); 
            destinationFloors.add(tmpPerson.destinationFloor);
            // check if all request in elevator then move to destination floor
            System.out.println(floorQueue.isEmpty()); 
            if (floorQueue.isEmpty()){
              int tmp = destinationFloors.remove(0);   
              System.out.printf("Elevator letting person out at floor %s", tmp);         System.out.println("hdhdfhdh"); 
            
            }


       } 



    } 
    public void run(){
       try{
        System.out.printf("The current weight is: %s people, current floor: %s  Queue is:\n",weight,currentFloor);
        while(!queue.isEmpty()){
           Person p = queue.poll();
           System.out.printf("id: %s arrivalTime: %s arrivalFloor: %s destinationFloor: %s \n",p.id,p.arrivalTime,p.arrivalFloor,p.destinationFloor); 

        }
             
        System.out.printf("Floor Queue:\n%s\n", floorQueue); 
        startElevator(); 
        }


       catch(Exception e){



       }
   }

    






}
