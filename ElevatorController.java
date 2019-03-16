// this clas will allow sychronized access to the elevator thread 
// will control the actions of the elevator 
import java.util.concurrent.*;
import java.util.*; 


public class ElevatorController{
    public boolean goingUp; 
    public boolean goingDown; 
    public int currentFloor; 
    public int currentTime; 
    public int numPeople;
    public int currentWeight;
    // a person will be added to the waiting queue when we the current time has reached there arrival time
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> waiting;
    // all the people who will want to use the elevator
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> request; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> inElevator;
    int passengerWeight;
    // the request hashmap maps the request to the floor they are at 
    // the person making the request will join the linked queue for that floor 
    // (hashmap) {floor 0 -> linkedQueue{0 : [person1 - > person 2 -> 3}
    
    public ElevatorController(){
		this.goingUp = true; 
		this.goingDown = false; 
		// all of the people who want to request the elevator
		// maps to their arrrival floor 
		this.waiting = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		// all of the people currently in the elevator  
		// maps the person to their destination floor
		this.inElevator = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>(); 
		this.request = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		this.currentFloor = 0; 
		this.currentTime = 0; 
		this.currentWeight = 0 ;
	}
	
	
	// add a person into the in elevator, request or waiting queue
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
	
	// remove a person from either the inElevator, request or waiting queue
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
		addPerson(person,person.arrivalTime, request);

		
	}


    // if the persons arrival time is <= current time add them to the waiting queue
    public  synchronized void acceptRequest(){

	   Person personWaiting = removeAPerson(currentTime, request);
	   while(personWaiting != null){
		   addPerson(personWaiting,personWaiting.arrivalFloor, waiting);
		   removeAPerson(personWaiting.arrivalFloor, request);
		   personWaiting  =   removeAPerson(currentTime, request);
	   }

		
		

	}
    

    // this will decide which floor the elevator goes to next
    public synchronized void changeFloor(int minFloor,int maxFloor) throws InterruptedException{




     // Example for below - this will tell which direction the elevator should go
     // Current floor -> 5
     // InElevator Keys -> 1,2,3
     // these keys are the peoples destination  floor
     // max = 3
     // since max is less then current floor the elevator must go down

	 if(!(inElevator.isEmpty())){
       int max = Collections.max(inElevator.keySet());

       if(max < currentFloor){
		   goingUp = false;
		   goingDown = true;
	   }
	   else{
		   goingUp = true;
		   goingDown = false;
	   }

       }



      if(!(waiting.isEmpty()) || !(inElevator.isEmpty()) ){
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


			 // print what floor the elevator is on
			System.out.printf("Elevator is on floor %s************\n",this.currentFloor);

 }



    }


    public synchronized void enterElevator() throws InterruptedException{




	   /*if ((personEntering.weight + currentWeight) >= 2 ){
		   System.out.printf("The elevator is overweight person with id: %s and weight %s must leave",personEntering.id,personEntering.weight);
		   addPerson(personEntering, currentFloor, waiting);
		   personEntering = null;

	   }*/

	   boolean headingPrinted = false;
	   // remove a person who's waiting at the current floor
	   Person personEntering = removeAPerson(currentFloor, waiting);
	   // while there is people waiting on the current floor
       while(personEntering!=null){

				// print heading
		   	    if (!(headingPrinted)){
					System.out.printf("****************\nAllowing people in on floor %s...\n",currentFloor);
					headingPrinted = true;
				}

				// add the persons weight to the current weight
				this.currentWeight += personEntering.personWeight + personEntering.baggageWeight;
				// add the person to the in elevator
				addPerson(personEntering, personEntering.destinationFloor,inElevator);
				// print persons details
				System.out.println(personEntering);
				// remove another waiting person on the current floor
		        personEntering = removeAPerson(currentFloor, waiting);

	   }


	   notifyAll(); 
		
	}
   
   
   public synchronized void exitElevator() throws InterruptedException{
     // System.out.println(inElevator.isEmpty());

      // remove a person from the current floor who's in the elevator
	  Person personLeaving = removeAPerson(currentFloor, inElevator);
      boolean headingPrinted = false;
      // while there are people on this floor remove them from the elevator
      while(personLeaving!=null){

		 // print heading to show people are leaving
		 if(!(headingPrinted)){
			System.out.printf("****************\nLetting people out on floor %s...\n",currentFloor);
			headingPrinted = true;
	     }

	    // print out who's leaving
        System.out.println(personLeaving);
        // adjust weight according to the people who are leaving
        this.currentWeight-= (personLeaving.personWeight + personLeaving.baggageWeight);
        // remove another person from this floor
	    personLeaving = removeAPerson(currentFloor, inElevator);

     	}


	   
	 notifyAll();   
	 }

	public int getTime(){
		return this.currentTime;
	}
}
