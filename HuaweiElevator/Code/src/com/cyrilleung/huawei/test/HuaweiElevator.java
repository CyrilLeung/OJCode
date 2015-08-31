package com.cyrilleung.huawei.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class HuaweiElevator {
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		int size = input.nextInt();
		int psgNum = input.nextInt();
		
		ArrayList<PsgInfo> psgReqs = new ArrayList<PsgInfo>();
		for(int i = 0; i < psgNum; ++i){
			PsgInfo psgInfo = new PsgInfo();
			psgInfo.from = input.nextInt();
			psgInfo.to = input.nextInt();
			psgReqs.add(psgInfo);
		}
		
		int floor = 1;
		int cap = size;
		LinkedList<Integer> inElevator = new LinkedList<Integer>();
		
		for(int i = 0; i < psgNum; ++i){
			if(!psgReqs.get(i).hasDone){
				while(floor != psgReqs.get(i).to){
					if(floor > psgReqs.get(i).from){
						while(floor > psgReqs.get(i).from){
							floor--;
							for(int j = i+1; j < psgNum; ++j){
								if(j > psgNum)
									break;
								for(int k : inElevator){
									if(psgReqs.get(k).to == floor){
										--cap;
										inElevator.remove(k);
										psgReqs.get(k).hasDone = true;
									}
								}
								if(psgReqs.get(j).from == floor && psgReqs.get(j).to > psgReqs.get(i).to && cap > 0){
									--cap;
									inElevator.add(j);
								}
								
							}
						}
					}
				}
				//TODO: Judge the difference logic path....
				//尼玛好复杂，老子不干了，换种思维方式吧
			}
		}
	}
}


class PsgInfo{
	public int from;
	public int to;
	public boolean hasDone = false;
}