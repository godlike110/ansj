package cn.focus.estatedic.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.FilterModifWord;

public class FindStopWords2 {

	static List<String> docs = new ArrayList<String>();
	static Map<String, Integer> map = new TreeMap();
	static String result = new String();

	public static void main(String args[]) throws Exception {
		readFolder("news");
		/*for (String str : docs) {
			System.out.println(str);
		}*/

		// 停词表
		String stopWordFile = "stopword.txt";
		List<String> stopWord = new ArrayList();

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(stopWordFile), "UTF-8"));
			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				stopWord.add(line);
			}
		} catch (Exception e) {
			System.out.println("创建停词表失败");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FilterModifWord.insertStopWords(stopWord);
		FilterModifWord.insertStopNatures(" ");
		FilterModifWord.insertStopNatures("t");
		FilterModifWord.insertStopNatures("tg");
		FilterModifWord.insertStopNatures("s");
		FilterModifWord.insertStopNatures("f");
		FilterModifWord.insertStopNatures("vshi");
		FilterModifWord.insertStopNatures("vyou");
		FilterModifWord.insertStopNatures("a");
		FilterModifWord.insertStopNatures("ad");
		//FilterModifWord.insertStopNatures("an");
		FilterModifWord.insertStopNatures("ag");
		FilterModifWord.insertStopNatures("al");
		FilterModifWord.insertStopNatures("b");
		FilterModifWord.insertStopNatures("bl");
		FilterModifWord.insertStopNatures("z");
		FilterModifWord.insertStopNatures("r");
		FilterModifWord.insertStopNatures("rr");
		FilterModifWord.insertStopNatures("rz");
		FilterModifWord.insertStopNatures("rzt");
		FilterModifWord.insertStopNatures("rzs");
		FilterModifWord.insertStopNatures("rzv");
		FilterModifWord.insertStopNatures("ry");
		FilterModifWord.insertStopNatures("ryt");
		FilterModifWord.insertStopNatures("rys");
		FilterModifWord.insertStopNatures("ryv");
		FilterModifWord.insertStopNatures("rg");
		FilterModifWord.insertStopNatures("m");
		FilterModifWord.insertStopNatures("mq");
		FilterModifWord.insertStopNatures("q");
		FilterModifWord.insertStopNatures("qv");
		FilterModifWord.insertStopNatures("qt");
		FilterModifWord.insertStopNatures("d");
		FilterModifWord.insertStopNatures("p");
		FilterModifWord.insertStopNatures("pba");
		FilterModifWord.insertStopNatures("pbei");
		FilterModifWord.insertStopNatures("c");
		FilterModifWord.insertStopNatures("cc");
		FilterModifWord.insertStopNatures("u");
		FilterModifWord.insertStopNatures("uzhe");
		FilterModifWord.insertStopNatures("ule");
		FilterModifWord.insertStopNatures("uguo");
		FilterModifWord.insertStopNatures("ude1");
		FilterModifWord.insertStopNatures("ude2");
		FilterModifWord.insertStopNatures("ude3");
		FilterModifWord.insertStopNatures("usuo");
		FilterModifWord.insertStopNatures("udeng");
		FilterModifWord.insertStopNatures("uyy");
		FilterModifWord.insertStopNatures("udh");
		FilterModifWord.insertStopNatures("uls");
		FilterModifWord.insertStopNatures("uzhi");
		FilterModifWord.insertStopNatures("ulian");
		FilterModifWord.insertStopNatures("e");
		FilterModifWord.insertStopNatures("y");
		FilterModifWord.insertStopNatures("o");
		FilterModifWord.insertStopNatures("h");
		FilterModifWord.insertStopNatures("k");
		FilterModifWord.insertStopNatures("x");
		FilterModifWord.insertStopNatures("xx");
		FilterModifWord.insertStopNatures("xu");
		FilterModifWord.insertStopNatures("w");
		FilterModifWord.insertStopNatures("wkz");
		FilterModifWord.insertStopNatures("wky");
		FilterModifWord.insertStopNatures("wyz");
		FilterModifWord.insertStopNatures("wyy");
		FilterModifWord.insertStopNatures("wj");
		FilterModifWord.insertStopNatures("ww");
		FilterModifWord.insertStopNatures("wt");
		FilterModifWord.insertStopNatures("wd");
		FilterModifWord.insertStopNatures("wf");
		FilterModifWord.insertStopNatures("wn");
		FilterModifWord.insertStopNatures("wm");
		FilterModifWord.insertStopNatures("ws");
		FilterModifWord.insertStopNatures("wp");
		FilterModifWord.insertStopNatures("wb");
		FilterModifWord.insertStopNatures("wh");
		FilterModifWord.insertStopNatures("en");
		FilterModifWord.insertStopNatures("nr1");
		FilterModifWord.insertStopNatures("vd");
		FilterModifWord.insertStopNatures("vf");
		FilterModifWord.insertStopNatures("vx");
		FilterModifWord.insertStopNatures("vi");
		FilterModifWord.insertStopNatures("nw");
		for (String str : docs) {
			List parse = NlpAnalysis.parse(str);
			new NatureRecognition(parse).recognition() ;
			List<Term> terms = FilterModifWord.modifResult(parse) ;
			Map<String,Integer> termmap = new HashMap();
			
			for (int j = 0; j < terms.size(); j++) {
				Term t = terms.get(j);
				String ts = t.getRealName();
				if(ts.length() == 1){
					continue;
				}
				
					if (termmap.containsKey(ts)) {
						termmap.put(ts, termmap.get(ts) + 1);
					} else {
						termmap.put(ts, 1);
					}
				
			}
			Iterator<String> it = termmap.keySet().iterator();
			while(it.hasNext()){
				String ts = it.next();
				int count = termmap.get(ts);
				if (count >= 5) {
					 if(map.containsKey(ts)){
						 map.put(ts, map.get(ts)+count);
					 }else{
						 map.put(ts, count);
					 }
				}
				
			}
			
		}
		
		docs.clear();
		StringBuffer sb = new StringBuffer();
		Set<String> set = map.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String t = (String) it.next();
			if (map.get(t) >= 100) {
				System.out.println(t + ":" + map.get(t));
				sb.append(t + ":" + map.get(t) + "\n");
			}
		}
		result = sb.toString();
	}

	/**
	 * 读取文件夹
	 * 
	 * @return
	 */
	public static void readFolder(String filePath) {
		try {
			 
			// 读取指定文件夹下的所有文件
			File file = new File(filePath);
			if (!file.isDirectory()) {
				System.out.println("---------- 该文件不是一个目录文件 ----------");
			} else if (file.isDirectory()) {
				System.out.println("---------- 这是一个目录文件夹 ----------");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filePath + "\\" + filelist[i]);
					String absolutepath = readfile.getAbsolutePath();// 文件的绝对路径
					String filename = readfile.getName();// 读到的文件名
					readFile(absolutepath, filename, i);// 调用readFile方法读取文件夹下所有文件
					if(i>5000){
						break;
					}
				}
				System.out.println("---------- 所有文件操作完毕 ----------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件夹下的文件
	 * 
	 * @return
	 */
	public static void readFile(String absolutepath, String filename, int index) {
		StringBuffer text = new StringBuffer();
		//String title = filename.split("\\.")[0] + " ";
		//text.append(title);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(absolutepath)));

			String s = null;
			while ((s = br.readLine()) != null) {
				text.append(s);
			}
			docs.add(text.toString());
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getResult() {
		return result;
	}
}
