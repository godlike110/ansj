package cn.focus.estatedic.demo.faster;

import java.math.BigDecimal;

public class WordProbability {
	Integer id;
	String name;
	BigDecimal probability;
	Integer count ;
	public WordProbability(Integer id,String name, BigDecimal probability,Integer count) {
		super();
		this.id = id;
		this.name = name;
		this.probability = probability;
		this.count = count;
	}
 
	 
}
