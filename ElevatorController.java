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
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>> acceptedRequest; 
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
		this.acceptedRequest = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Person>>();
		this.currentFloor = 0; 
		this.currentTime = 0; 
		this.numPeople = 0 ;
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
		//System.out.println(person.arrivalTime); 
		//System.out.println(currentTime); 
		//if(person.arrivalTime <= currentTime){
		addPerson(person,person.arrivalFloor, request); 
	    //}
		//System.out.println(this.request); 
		
	}
   
    public  synchronized void acceptRequest(){
	
	    // if a person's arrivalTime is less then or equal to current time 
	    ConcurrentLinkedQueue<Person> queue = request.get(currentFloor); 
			
			for (Person p : queue){
				if (p.arrivalTime <= currentTime){
					
					addPerson(p,p.arrivalFloor, acceptedRequest); 
				}
			}
			
		
		
		
	}
    
    // this will decide which floor the elevator goes to next 
    public synchronized void changeFloor(int minFloor,int maxFloor) throws InterruptedException{
	    // add one to time at each floor 
	    //System.out.println(request); 
       //if (!(request.isEmpty())){
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
			 
		//	if(!inElevator.isEmpty()){
			System.out.printf("Elevator is on floor %s\n",this.currentFloor); 	   
		  //  }
		 //}

			 


    }


    public synchronized void enterElevator() throws InterruptedException{
		
		
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
		   	  // if (numPeople > 1){System.out.println("Lift Overweight" + inElevator);break;}

		   // when a person enters, update the weight 
				this.numPeople += 1; 

				addPerson(personEntering, personEntering.destinationFloor,inElevator);
				System.out.println(personEntering); 
		        personEntering = removeAPerson(currentFloor, request); 

	   }
	   
	   

	   notifyAll(); 
		
	}
   
   
   public synchronized void exitElevator() throws InterruptedException{
	   // if there are no more request or people in the elevator, sleep at that floor 
	   //while(request.isEmpty()){
		 //   System.out.printf("The elevator is sleeping on floor %s\n",currentFloor); 
		 //   wait(100); 
	   //}


	   Person personLeaving = removeAPerson(currentFloor, inElevator); 
       // if there is a person on the floor requesting to get in, let them in 
       if (personLeaving!=null){
       System.out.printf("****************\nLetting people out on floor %s...\n",currentFloor); 
       // while there are people at that floor let them enter the elevator
       // then add them to the inElevator hashmap with there destination floor 
       		   System.out.println(inElevator.isEmpty()); 

      while(personLeaving!=null){
        System.out.println(personLeaving); 
	    personLeaving = removeAPerson(currentFloor, inElevator); 
	    this.numPeople-=1;
     	}
		
	 } 
	   
	 notifyAll();   
	 }

	public int getTime(){
		return this.currentTime;
	}
}
