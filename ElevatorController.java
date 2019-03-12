// this clas will allow sychronized access to the elevator thread 
// will control the actions of the elevator 
import java.util.concurrent.*;
import java.util.*; 


public class ElevatorController{
    public boolean goingUp; 
    public boolean goingDown; 
    public int currentFloor; 
    public int currentTime; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> request; 
     public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> inElevator;   
    // the request hashmap maps the request to the floor they are at 
    // the person making the request will join the linked queue for that floor 
    // (hashmap) {floor 0 -> linkedQueue{0 : [person1 - > person 2 -> 3}
    
    public ElevatorController(){
		this.goingUp = true; 
		this.goingDown = false; 
		// all of the people who want to request the elevator
		// maps to their arrrival floor 
		this.request = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		// all of the people currently in the elevator  
		// maps the person to their destination floor
		this.inElevator = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>(); 
		this.currentFloor = 0; 
		this.currentTime = 0; 
		
	}
	
	
	// add a person into the elevator or the request queue 
	public synchronized void addPerson(Person person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap){
		ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			tmp.add(person);
			hashmap.put(floor, tmp); 
		}
		else{
			tmp.add(person); 
			hashmap.put(floor, tmp); 
			
		}
		
	}
	
	// remove a person from either the inElevator hashmap or request hashmap 
	public synchronized Person removeAPerson(int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> hashmap) {
		ConcurrentLinkedQueue<Person> tmp = new ConcurrentLinkedQueue<Person>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			Person personRemoved = tmp.poll();
			hashmap.put(floor, tmp); 
			if (hashmap.get(floor).isEmpty()){
				hashmap.remove(floor); 
			}
			return personRemoved; 
		}
		
		return null; 
		
	}
	
	// A person wants to request the elevator, add them into request hashmap 
	public synchronized void makeRequest(Person person){
		addPerson(person,person.arrivalFloor, request); 
		//System.out.println(this.request); 
		
	}
    
    
    // this will decide which floor the elevator goes to next 
    public synchronized void changeFloor(int minFloor,int maxFloor){
		   // add one to time at each floor 
		   currentTime++; 
		   if (goingUp == true){
			   
			   if(currentFloor == maxFloor){
				    currentFloor--;	   
				    goingUp = false; 
				    goingDown = true; 			    
			   }
		       else{
				   currentFloor++;
				}
	       }
			
		   else{
			   
			   if(currentFloor == minFloor){
					goingUp = true; 
				    goingDown = false; 	
				    currentFloor++;
		   
				 }
				else{
			      currentFloor--;
                 }
			   
			 }  

    }


    public synchronized void enterElevator(){
		
		
       // if no person waiting to enter, will return null 
	    // find out if the elevator should be going up or down 
	    // compare the current floor with the floors in the in elevator hashmap 
	    
	    
	    for (int key : inElevator.keySet()){
			if (key > currentFloor){
				goingUp = true;
				goingDown = false; 
				break ; 
			}
			else{
				goingUp =false; 
				goingDown = true; 
			}
			
		}
		// System.out.printf("going up %s and going down %s\n", goingUp, goingDown); 
     
	   
	   Person personEntering = removeAPerson(currentFloor, request); 
	   // check if there is a person waiting on the elevator 
       if (personEntering!=null){
		   System.out.printf("****************\nAllowing people in on floor %s...\n",currentFloor);
	   }  
       // while there are people at that floor let them enter the elevator
       // then add them to the inElevator hashmap with there destination floor 
       while(personEntering!=null){
		   // when a person enters, update the weight 
		   if (currentTime >= personEntering.arrivalTime ){
				Elevator.currentWeight += 4; 
				addPerson(personEntering, personEntering.destinationFloor,inElevator);
				System.out.println(personEntering); 
			}
			
			else{
				 makeRequest(personEntering); 
				 break; 
				}
				personEntering = removeAPerson(currentFloor, request); 

	   }
	   
	   

	   notifyAll(); 
		
	}
   
   
   public synchronized void exitElevator() throws InterruptedException{
	   // if there are no more request or people in the elevator, sleep at that floor 
	   while(request.isEmpty() && inElevator.isEmpty()){
		   System.out.printf("The elevator is sleeping on floor %s\n",currentFloor); 
		   wait(); 
	   }


	   Person personLeaving = removeAPerson(currentFloor, inElevator); 
       // if there is a person on the floor requesting to get in, let them in 
       if (personLeaving!=null){
       System.out.printf("****************\nLetting people out on floor %s...\n",currentFloor); 
       // while there are people at that floor let them enter the elevator
       // then add them to the inElevator hashmap with there destination floor 
       while(personLeaving!=null){
       System.out.println(personLeaving); 
	    personLeaving = removeAPerson(currentFloor, inElevator); 
     	}
		
	 } 
	   
	 notifyAll();   
	 }

}
