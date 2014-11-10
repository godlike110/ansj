package cn.focus.estatedic.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.springframework.stereotype.Component;

import cn.focus.estatedic.test.Word;

@Component
public class AppConstants {
	
//    @ConfigProperty("root.path")
//    public static String ROOTPATH;
	
    @ConfigProperty("estate.path")
	public static String PATH;
    
    @ConfigProperty("estate.m1")
	public static int m1;
    
    @ConfigProperty("estate.m2")
	public static int m2;
    
    @ConfigProperty("estate.k1")
	public static int k1;
    
    @ConfigProperty("estate.k2")
	public static int k2;

	//分词列表
	public static List<List<Term>> TERMLIST = new ArrayList<List<Term>>();
	
	//关键字列表
	public static List<List<Keyword>> KEYWORDLIST = new ArrayList<List<Keyword>>();
	//普通分词统计map
	public static Map<String,Integer> TERMMAP = new HashMap<String ,Integer>();
	
	//词对应编号
	public static Map<String,Integer> MATCHMAP = new HashMap<String,Integer>();
	
	//新出现分词统计map
	public static Map<String,Integer> NEWTERMMAP = new HashMap<String ,Integer>();
	
	//关键词统计map
	public static Map<String,Integer> KEYWORDMAP = new HashMap<String ,Integer>();
	
    //停词表路径
	//public static String STOPWORDSPATH =  "src/main/resources/dicConfig/stopword.txt";
	
	//单词词频统计
	public static List<Word> WORDS = new ArrayList<Word>();
	
	//词编号
	public static int WORDNUM = 1;
	
}
