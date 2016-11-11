package amu.areeb.zhcet.utils;

import java.util.ArrayList;
import java.util.Collections;

public class Random
{
	private ArrayList<Integer> list = new ArrayList<Integer>();
	private int index;
	
	public Random(int n){
		if(n<0)
			throw new IllegalStateException("Upper limit less than Lower limit");
		for(int i = 0; i < n; i++){
			list.add(i);
		}
		Collections.shuffle(list);
	}
	
	public Random(int m, int n){
		if(m<n)
			throw new IllegalStateException("Upper limit less than Lower limit");
		for(int i = m; i < n; i++){
			list.add(i);
		}
		Collections.shuffle(list);
	}
	
	public int getNext(){
		int rand = 0;
		try{
			rand = list.get(index);
		} catch(IndexOutOfBoundsException e){
			Collections.shuffle(list);
			index = 0;
			rand = list.get(index);
		}
		index++;
		return rand;
	}
	
}
