package com.cyrilleung.huawei.test;
//Core thinking: 
//	(1)use the floor status to drive the behavior.
//	(2)Back forward thinking.
//	(3)OO programming.
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.CRC32;

import org.omg.CORBA.Request;

public class HuaweiElevatorOop {
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		Elevator elevator = new Elevator(input.nextInt());
		int psgNum = input.nextInt();
		
		for(int i = 0; i < psgNum; ++i){
			PsgReq req = new PsgReq();
			req.from = input.nextInt();
			req.to = input.nextInt();
			elevator.addReq(req);
		}
		elevator.run();
	}
}

class Elevator{
	private int capacity;
	private ArrayList<Integer> insidePsg = new ArrayList<Integer>();
	private int floor = 1;
	private boolean up;
	private int time = 0;
	private PsgReq highestReq;
	private ArrayList<PsgReq> psgReqsLst = new ArrayList<PsgReq>();
	
	public Elevator(int cap){
		this.capacity = cap;
	}
	
	public boolean addReq(PsgReq req){
		return psgReqsLst.add(req);
	}
	
	public boolean allFinish(){
		for(PsgReq req : psgReqsLst){
			if(!req.hasDone)
				return false;
		}
		return true;
	}
	public void move(){
		++time;
		if(up){
			floor += 1;
		}
		else
			floor--;
	}
	
	private void updateReqPriority(){
		for(PsgReq req : psgReqsLst){
			if(!req.hasDone){
				highestReq = req;
				return;
			}
		}
		highestReq = null;
	}
	
	private boolean direct(PsgReq req){
		return req.to - req.from > 0;
	}
	
	private boolean isFull(){
		return capacity < insidePsg.size();
	}
	
	private void updateDirect(){
		if(highestReq == null)
			return;
		if(floor < highestReq.from && !insidePsg.contains(psgReqsLst.indexOf(highestReq)))
			up = true;
		else if(floor > highestReq.from && !insidePsg.contains(psgReqsLst.indexOf(highestReq)))
			up = false;
		else if(floor < highestReq.to && insidePsg.contains(psgReqsLst.indexOf(highestReq)))
			up = true;
		else if(floor > highestReq.to && insidePsg.contains(psgReqsLst.indexOf(highestReq)))
			up = false;
		else if(floor == highestReq.from){
			insidePsg.add(psgReqsLst.indexOf(highestReq));
			if(floor < highestReq.to)
				up = true;
			else
				up = false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void floorProcess(){
	//TODO: should solve this problem in a back forward way.
		//Off the elevator
		for(Integer i : (ArrayList<Integer>)insidePsg.clone()){
			if(floor == psgReqsLst.get(i).to){
				insidePsg.remove(i);
				psgReqsLst.get(i).hasDone = true;
			}
		}
		
		updateReqPriority();
		updateDirect();
		
		for(int i = psgReqsLst.indexOf(highestReq) + 1; i < psgReqsLst.size(); ++i){
			if(i >= psgReqsLst.size())
				break;
			PsgReq curReq = psgReqsLst.get(i);
			if(curReq.from == floor && up == direct(curReq) && !isFull()){
				int tmpCnt = 0;
				for(int j = psgReqsLst.indexOf(highestReq); j < i ; ++j){
					if(!insidePsg.contains(j) &&direct(psgReqsLst.get(j)) == up && psgReqsLst.get(j).from < curReq.to){
						++tmpCnt;
					}
				}
				if(tmpCnt + insidePsg.size() < capacity)
					insidePsg.add(i);
			}
		}
	}
	
	public void run(){
		updateReqPriority();
		while(!allFinish()){
			floorProcess();
			move();
		}	
		System.out.println("Total time:" + this.time);
	}
}

class PsgReq{
	public int from;
	public int to;
	public boolean hasDone;
}