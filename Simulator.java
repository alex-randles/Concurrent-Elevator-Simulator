import java.util.*;


public class Simulator{
  
  public static void main(String [] args){
      

      Person p1 = new Person(2,1600, 4, 7,true); // (ID, ArrivalTime, ArrivalFloor, DesitinationFloor, hasTrolley) 
      Person p2 = new Person(3,1600, 2, 8,false); 
      Person p3 = new Person(4,1600, 1, 7, false); 
      Elevator e1 = new Elevator(); 
      e1.addRequest(p1); 
      e1.addRequest(p2); 
      e1.addRequest(p3); 
      Thread t3 = new Thread(e1);
      t3.start(); 
      
  } 

  public static void  ArrivingGoingFromTo(int atFloor, int toFloor){
         


  } 



} 
