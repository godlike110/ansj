package cn.focus.estatedic.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
 

public class Test {

	public static void main(String[] args) {
		List<Term> list  =NlpAnalysis.parse("三分之二 我的你的 瀚林美居 我的你的");
		Map<Term,Integer> map = new HashMap();
		for(Term term:list){
			System.out.println(term);
			if(map.containsKey(term)){
				map.put(term, map.get(term)+1);
			 }else{
				 map.put(term, 1);
			 }
		}
		
		java.util.Iterator<Term> it = map.keySet().iterator();
		while(it.hasNext()){
			Term temp = it.next();
			System.out.println(temp+":"+map.get(temp));
		}
		
		
	}
}
