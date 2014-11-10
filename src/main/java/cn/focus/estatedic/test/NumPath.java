package cn.focus.estatedic.test;

import java.util.HashMap;
import java.util.Map;

public class NumPath {
	
	private static Map<Integer,Integer> map = new HashMap<Integer,Integer>();
	
	public static int find(int n){
		if(n==1){
			return 0;
		}else{
			if(n%2==1){
				return Math.min(find(n+1), find(n-1))+1;
			}else{
				return find(n/2)+1;
			}
		}
	}
	
	public static int find1(int n) {
		if (n == 1) {
			return 0;
		} else {
			if (n % 2 == 1) {
				int c = Integer.MAX_VALUE;
				int d = Integer.MAX_VALUE;
				if(map.containsKey(n-1)) {
					c = map.get(n-1);
				}
				if(map.containsKey(n+1)) {
					d = map.get(n+1);
				}
				
				if(c<Integer.MAX_VALUE|| d<Integer.MAX_VALUE) {
					return Math.max(c, d);
				} else {
					
				}
				

			} else {
				//int a = find(n / 2)+1;
				//if (!map.containsKey(n / 2)) {
				//	map.put(n / 2, a);
				//}
				//return find(n / 2) + 1;
				if(map.containsKey(n/2)) {
					return map.get(n/2);
				} else {
					int a = find(n/2)+1;
					map.put(n/2, a);
				}
			}
		}
		return null;
	}
	
	
	public static void main(String argx[]) {
		long start = System.currentTimeMillis();
		System.out.println(find1(199999));
		long end = System.currentTimeMillis();
		System.out.println("time: " + (end-start));
		
		long start1 = System.currentTimeMillis();
		System.out.println(find(199999));
		long end1 = System.currentTimeMillis();
		System.out.println("time1: " + (end1-start1));
	}

}
