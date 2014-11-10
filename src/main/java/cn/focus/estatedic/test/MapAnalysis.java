package cn.focus.estatedic.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
 

public class MapAnalysis {

	public static void main(String args[]){
		StringBuffer text = new StringBuffer();
		Map<Integer,String> map = new TreeMap();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("result/docs5000,newweight10final,notnewweight1,count100.txt")));

			String s = null;
			while ((s = br.readLine()) != null) {
				String array[] = s.split(":");
				map.put(Integer.parseInt(array[1].trim()), array[0]);
			}
			 
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Iterator<Integer> it = map.keySet().iterator();
		while(it.hasNext()){
			Integer key = (int) it.next();
			System.out.println(key+":"+map.get(key));
		}
	}
}
