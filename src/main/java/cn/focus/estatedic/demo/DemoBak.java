package cn.focus.estatedic.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import cn.focus.estatedic.config.AppConstants;
import cn.focus.estatedic.lda.LdaMethod;

public class DemoBak {
	public static String separator = File.separator;
	public static String estateNews = AppConstants.PATH + "estateNews";
	public static String nonEstateNews = AppConstants.PATH + "nonEstateNews";
	
	public int m1 = AppConstants.m1 ; //输入参数，每一组训练文本集合包含的房产新闻数目
	public int m2 = AppConstants.m2;  //输入参数，每一组训练文本集合包含的非房产新闻数目
	public int k1 = AppConstants.k1; // 根据总的房产新闻数目和m1计算得到k1，房产新闻总的组数
	public int k2 = AppConstants.k2; // 根据总的非房产新闻数目和m2计算得到k2，非房产新闻总的组数
	public int k1min = 0;
	public int k2min = 0;
	
	public int wordsPerLda = 500; //每一轮主题模型LDA算法挑选出来的单词数目
	public double ratio = 0; //筛选单词的控制变量
	int weight = 20; // 新词nw和用户自定义user define的权重
	//public static Map<String, Integer> matchtable = new HashMap();
	//public static Map<Integer, String> newMatchtable = new HashMap();

	
	public DemoBak(){}
	
	public DemoBak(int m1, int m2, int wordsPerLda, double ratio, int weight){
		this.m1 = m1;
		this.m2 = m2;
		this.wordsPerLda = wordsPerLda;
		this.ratio = ratio;
		this.weight = weight;
	}

	public static String DIC_FIAG = "焦点房产词典";

	public static String reg = "^[0-9]+.$";

	public static Pattern pattern;

	public static String rootPath = "";

	static {
		pattern = Pattern.compile(reg);
		rootPath = System.getProperty("user.dir");
	}

	public void demo() {
		long time1 = System.currentTimeMillis();
		if(null==AppConstants.PATH) {
			AppConstants.PATH = System.getProperty("user.dir");
			if(!AppConstants.PATH.endsWith(File.separator)) {
				AppConstants.PATH = AppConstants.PATH + File.separator;
			}
			AppConstants.PATH += "dicConfig/";
		}
		estateNews = AppConstants.PATH + "estateNews";
		nonEstateNews = AppConstants.PATH + "nonEstateNews";
		if(AppConstants.k1 == 0) {
			AppConstants.m1=1400;
			AppConstants.m2=1000;
			AppConstants.k1=10;
			AppConstants.k2=1;
			m1=1400;
			m2=1000;
			k1=10;
			k2=1;
		}

		System.out.println("estatepath:" + AppConstants.PATH);
		System.out.println("estatem1:" + AppConstants.m1);
		System.out.println("estatem2:" + AppConstants.m2);
		System.out.println("estatek1:" + AppConstants.k1);
		System.out.println("estatek2:" + AppConstants.k2);

		initEnv();
		 //calGroup();
		process();
		long time2 = System.currentTimeMillis();
		System.out.println("use time :" + (time2-time1));

	}
	
	/**
	 * clear data
	 */
	public void initEnv() {
		System.out.println("start clearing ..........");
		File file = new File(AppConstants.PATH + "DemoResult/");
		delFile(file);
		System.out.println("ended clearing data.......");
	}
	
	public void delFile(File file) {
		if(file.isDirectory()) {
			File[] fs = file.listFiles();
			for(File f:fs) {
				delFile(f);
			}
		} else {
			file.delete();
		}
	}

	/**
	 * 计算数据的分组情况
	 */
	public void calGroup() {
		int n1 = calFileCount(estateNews);
		System.out.println("estateNews:"+n1);
		int n2 = calFileCount(nonEstateNews);
		System.out.println("nonEstateNews:"+n2);
		if (n1 % m1 == 0) {
			k1 = n1 / m1;
		} else {
			k1 = n1 / m1 + 1;
		}
		System.out.println("get estate news collections : " + k1);
		if (n2 % m2 == 0) {
			k2 = n2 / m2;
		} else {
			k2 = n2 / m2 + 1;
		}
		System.out.println("get non estate news collections : " + k2);

	}

	/*
	 * 针对每一组数据进行分词，LDA学习过程
	 */
	public void process() {
		Preprocess pre = new Preprocess(weight);
		String estateFilePath = "";
		String nonEstateFilePath = "";
//		if (!rootPath.endsWith(separator)) {
//			estateFilePath = rootPath + separator;
//			nonEstateFilePath = rootPath + separator;
//		}
		estateFilePath = estateNews;
		nonEstateFilePath = nonEstateNews;
		System.out.println("i am start to process!!!!");
		for (int i = k1min; i < k1; i++) {
			System.out.println("outer1 : " + i);
			List<List<WordProbability>> list = new ArrayList();
			for (int j = k2min; j < k2; j++) {
				System.out.println("processing the " + i + ":" + j
						+ "　examples．．．．");
				pre.ldaPreprocess(estateFilePath, i, m1, nonEstateFilePath, j,
						m2);
				LdaMethod lm = new LdaMethod();
				String resultFileName = AppConstants.PATH + "DemoResult/lda/lda_"
						+ i + "_" + j;
				String lmArgs[] = { resultFileName, "-N", 2 + "",resultFileName };
				lm.lda(lmArgs);
				ArrayList<WordProbability> wordspros = (ArrayList<WordProbability>) calLdaBeta(
						resultFileName, i, j);
				list.add(wordspros);
				
				findWordsByCount(i,j, list);
				findWordsByProbability(i,j,list);
				System.out.println("processing the " + i + ":" + j
						+ "　examples　ended!．．．．");
			}
			//findWordsByCount(i,  list);
			//findWordsByProbability(i, list);
			
			System.out.println("outer1 : " + i + "end !");
			//Map<Integer, WordProbability> newlist = findWords(i, list);
			// calMatchTable(i);
			// inverseMatchTable();
			// matchtable.clear();
			// printWords(newlist,i);
			
		}
		mergeWordsByProbability();
		mergeWordsByCount();
	}

	
	/*
	public void inverseMatchTable() {
		System.out.println("start switch matchtable....");
		Iterator<String> it = matchtable.keySet().iterator();
		System.out.println("matchtable size: " + matchtable.size());
		while (it.hasNext()) {
			String str = it.next();
			newMatchtable.put(matchtable.get(str), str);
			System.out.println("id:" + matchtable.get(str) + " str: " + str);
		}
		System.out.println("newMatchtable size: " + newMatchtable.size());
		System.out.println("switch matchtable ended....");
	}

	public void printWords(List<Integer> newlist, int gi) {
		BufferedWriter writer = null;
		String resultFileName = AppConstants.PATH + "DemoResult/wordFinal_"
				+ gi + "";

		try {
			writer = new BufferedWriter(new FileWriter(resultFileName));
			StringBuffer sb = new StringBuffer();
			for (Integer id : newlist) {
				if (newMatchtable.get(id) == null) {
					System.out.println("null id :" + id);
				}
				String str = newMatchtable.get(id);
				sb.append(id + ":" + str + "\n");
			}

			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	 

	

	public void calMatchTable(int i) {
		String filePath = AppConstants.PATH + "DemoResult/matchTable/";
		try {
			File file = new File(filePath);
			if (!file.isDirectory()) {
				System.out.println("---------- 该文件不是一个目录文件 ----------");
			} else {
				System.out.println("---------- 这是一个目录文件夹 ----------");
				File[] filelist = file.listFiles();
				for (File f : filelist) {
					if (f.getName().indexOf("e_" + String.valueOf(i)) == -1) {
						continue;
					}
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(f)));

					String s = null;
					String sarray[] = null;

					while ((s = br.readLine()) != null) {
						sarray = s.split(":");
						matchtable.put(sarray[0],
								Integer.parseInt(sarray[sarray.length - 1]));
					}
					br.close();
				}
				System.out.println("---------- 所有文件操作完毕 ----------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	*/
	
	/*
	 * 根据LDA算法得到的beta文件来找到房产相关词语
	 */
	public List<WordProbability> calLdaBeta(String resultFileName, int i, int j) {
		String filePath = AppConstants.PATH + "DemoResult/matchTable/matchtable_"
				+ i + "_" + j;
		Map<Integer, String> mt = new HashMap();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(filePath))));

			String s = null;
			String sarray[] = null;

			while ((s = br.readLine()) != null) {
				sarray = s.split(":");
				mt.put(Integer.parseInt(sarray[sarray.length - 1]), sarray[0]);
			}

			br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<WordProbability> m1 = new ArrayList();
		List<WordProbability> m2 = new ArrayList();
		BigDecimal b1 = new BigDecimal(0);
		BigDecimal b2 = new BigDecimal(0);
		boolean flag1 = true;//flag1=true m1 为房产相关主题
		resultFileName += ".beta";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(resultFileName)));
			int index = 1;
			String s = null;
			while ((s = br.readLine()) != null) {
				String pro[] = s.split("\t");
				if (StringUtils.isEmpty(s)) {
					System.out.println("出现一个空行：" + s);
					break;
				}
				BigDecimal d1 = new BigDecimal(pro[0]);
				String str = mt.get(index);
				m1.add(new WordProbability(index, str, d1, 0));
				BigDecimal d2 = new BigDecimal(pro[1]);
				;
				m2.add(new WordProbability(index, str, d2, 0));
				index++;

				if (str!=null  && str.contains("房产")) {
					b1= b1.add(d1);
					b2= b2.add(d2);
					System.out.println("判定主题词为："+str);
					
				}
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(b1.compareTo(b2)>0){
			flag1 = true;
		}else{
			flag1 = false;
		}

		System.out.println("flag1" + flag1);

		// m1为房产相关的主题词，默认m1为房产相关
		Collections.sort(m1, new Comparator<WordProbability>() {
			public int compare(WordProbability arg0, WordProbability arg1) {
				return arg1.probability.compareTo(arg0.probability);
			}
		});

		// m2为房产相关的主题词
		Collections.sort(m2, new Comparator<WordProbability>() {
			public int compare(WordProbability arg0, WordProbability arg1) {
				return arg1.probability.compareTo(arg0.probability);
			}
		});

		List<WordProbability> res = new ArrayList();
		if (flag1) {
			for (int k = 0; k < wordsPerLda; k++) {
				res.add(m1.get(k));
			}

			BufferedWriter writer = null;
			String nonEstateWordProbability = AppConstants.PATH + "DemoResult/nonEstateWordProbability_"
					+ i + j;

			try {
				writer = new BufferedWriter(new FileWriter(
						nonEstateWordProbability));
				StringBuffer sb = new StringBuffer();

				for (WordProbability wp : m2) {

					sb.append(wp.id + ":" + wp.name + ":" + wp.probability
							+ "\n");
				}

				writer.write(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			String estateWordProbability = AppConstants.PATH + "DemoResult/estateWordProbability_"
					+ i + j;
			try {
				writer = new BufferedWriter(new FileWriter(
						estateWordProbability));
				StringBuffer sb = new StringBuffer();

				for (WordProbability wp : m1) {

					sb.append(wp.id + ":" + wp.name + ":" + wp.probability
							+ "\n");
				}

				writer.write(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			for (int k = 0; k < wordsPerLda; k++) {
				res.add(m2.get(k));
			}

			BufferedWriter writer = null;
			String nonEstateWordProbability = AppConstants.PATH + "DemoResult/nonEstateWordProbability_"
					+ i + j;

			try {
				writer = new BufferedWriter(new FileWriter(
						nonEstateWordProbability));
				StringBuffer sb = new StringBuffer();

				for (WordProbability wp : m1) {

					sb.append(wp.id + ":" + wp.name + ":" + wp.probability
							+ "\n");
				}

				writer.write(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			String estateWordProbability = AppConstants.PATH + "DemoResult/estateWordProbability_"
					+ i + j;
			try {
				writer = new BufferedWriter(new FileWriter(
						estateWordProbability));
				StringBuffer sb = new StringBuffer();

				for (WordProbability wp : m2) {

					sb.append(wp.id + ":" + wp.name + ":" + wp.probability
							+ "\n");
				}

				writer.write(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return res;
	}
	
	/**
	 * 针对第gi组房产训练数据，来根据词频进行筛选
	 * @param gi 房产训练数据组次
	 * @param list 包含了房产单词的list
	 * @return 
	 */
	public Map<String, WordProbability> findWordsByCount(int gi, int gj,
			List<List<WordProbability>> list) {
		Map<String, WordProbability> m = new HashMap();
		System.out.println("findwords By count : " + gi +gj+ "start!");
		for (List<WordProbability> templist : list) {
			for (WordProbability w : templist) {
				if (m.containsKey(w.name)) {
					w.count=m.get(w.name).count+1;
					m.put(w.name, w);
				} else {
					w.count = 1;
					m.put(w.name, w);
				}
			}
		} 

		Map<String, WordProbability> newmap = new HashMap();
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			 
			 if(m.get(name).count>=k2*ratio+1){ 
				 newmap.put(name,m.get(name)); }
			 
			/*
			if (m.get(id).count >= 3) {
				newmap.put(id, m.get(id));
			}
			*/
		 
		}

		List<WordProbability> wplist = new ArrayList();
		it = newmap.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			wplist.add(newmap.get(name));
			 
		}
		
		Collections.sort(wplist, new Comparator<WordProbability>() {
			public int compare(WordProbability arg0, WordProbability arg1) {
				return arg1.count.compareTo(arg0.count);
			}
		}); 
		
		StringBuffer sb = new StringBuffer();
		for(WordProbability wp: wplist){
			sb.append(wp.id + ":" + wp.name + ":" + wp.count + "\n");
		}
		 

		BufferedWriter writer = null;
		String resultFileName = AppConstants.PATH + "DemoResult/word_"
				+ gi + gj;

		try {
			writer = new BufferedWriter(new FileWriter(resultFileName));
			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("findwords By count : " + gi + gj+"end!");
		return newmap;
	}
	
	
	
	
	
	public Map<String, WordProbability> findWordsByProbability(int gi, int gj,
			List<List<WordProbability>> list) {
			Map<String, WordProbability> m = new HashMap();
			System.out.println("findwords By probability : " + gi +gj+"start!");
			for (List<WordProbability> templist : list) {
			for (WordProbability w : templist) {
				if (m.containsKey(w.name)) {
					w.probability = m.get(w.name).probability.add(w.probability);
					m.put(w.name, w);
				} else {
					m.put(w.name, w);
				}
			}
			} 

		Map<String, WordProbability> newmap = new HashMap();
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			newmap.put(name,m.get(name)); 
			 
			/*
			if (m.get(id).count >= 3) {
				newmap.put(id, m.get(id));
			}
			*/
		 
		}

		List<WordProbability> wplist = new ArrayList();
		it = newmap.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			wplist.add(newmap.get(name));
			 
		}
		
		Collections.sort(wplist, new Comparator<WordProbability>() {
			public int compare(WordProbability arg0, WordProbability arg1) {
				return arg1.probability.compareTo(arg0.probability);
			}
		}); 
		
		StringBuffer sb = new StringBuffer();
		for(WordProbability wp: wplist){
			sb.append(wp.id + ":" + wp.name + ":" + wp.probability + "\n");
		}
		 

		BufferedWriter writer = null;
		String resultFileName = AppConstants.PATH + "DemoResult/wordProbability_"
				+ gi + gj+"";

		try {
			writer = new BufferedWriter(new FileWriter(resultFileName));
			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("findwords By probability : " + gi +gj+ "end!");
		return newmap;
	}
	
	
	
	public void mergeWordsByProbability(){ 
		String filePath = AppConstants.PATH + "DemoResult/";
		Map<String,BigDecimal> bigmap = new HashMap();
		try {
			File file = new File(filePath);
			if (!file.isDirectory()) {
				System.out.println("---------- this isn't a directory----------");
			} else {
				System.out.println("---------- this is a directory ----------");
				File[] filelist = file.listFiles();
				for (File f : filelist) {
					if (!f.getName().startsWith("wordProbability_")) {
						continue;
					}
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(f)));

					String s = null;
					String sarray[] = null;

					while ((s = br.readLine()) != null) {
						sarray = s.split(":");
						if(sarray.length==3){
							if(sarray[1].contains("userDefine")){
								continue;
							}
							BigDecimal bd = new BigDecimal(sarray[2]);
							if(bigmap.containsKey(sarray[1])){
								BigDecimal p = bigmap.get(sarray[1]).add(bd);
								bigmap.put(sarray[1], p);
							}else{
								bigmap.put(sarray[1],bd);
							}
						}
					}
					br.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<Map.Entry<String,BigDecimal>> mappingList = null; 
		 
		  
		  //通过ArrayList构造函数把map.entrySet()转换成list 
		  mappingList = new ArrayList<Map.Entry<String,BigDecimal>>(bigmap.entrySet()); 
		  //通过比较器实现比较排序 
		  Collections.sort(mappingList, new Comparator<Map.Entry<String,BigDecimal>>(){ 
		   public int compare(Map.Entry<String,BigDecimal> mapping1,Map.Entry<String,BigDecimal> mapping2){ 
		    return mapping2.getValue().compareTo(mapping1.getValue()); 
		   } 
		  }); 
		  
		  StringBuffer resultsb = new StringBuffer();
		  for(Map.Entry<String,BigDecimal> mapping:mappingList){ 
		   resultsb.append((mapping.getKey()+":"+mapping.getValue()+"\n")); 
		  } 
		  
		  
		   BufferedWriter writer = null;
			String resultFileName = AppConstants.PATH + "DemoResult/final_probability";

			try {
				writer = new BufferedWriter(new FileWriter(resultFileName));
				writer.write(resultsb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		

	}
	
	
	public void mergeWordsByCount(){
		String filePath = AppConstants.PATH + "DemoResult/";
		Map<String,Integer> bigmap = new HashMap();
		try {
			File file = new File(filePath);
			if (!file.isDirectory()) {
				System.out.println("---------- this isn't a directory----------");
			} else {
				System.out.println("---------- this is a directory ----------");
				File[] filelist = file.listFiles();
				for (File f : filelist) {
					if (!f.getName().startsWith("wordProbability_")) {
						continue;
					}
					BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(f)));

					String s = null;
					String sarray[] = null;

					while ((s = br.readLine()) != null) {
						sarray = s.split(":");
						if(sarray.length==3){
							if(sarray[1].contains("userDefine")){
								continue;
							}
							 
							if(bigmap.containsKey(sarray[1])){
								 
								bigmap.put(sarray[1], bigmap.get(sarray[1])+1);
							}else{
								bigmap.put(sarray[1],1);
							}
						}
					}
					br.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<Map.Entry<String,Integer>> mappingList = null; 
		 
		  
		  //通过ArrayList构造函数把map.entrySet()转换成list 
		  mappingList = new ArrayList<Map.Entry<String,Integer>>(bigmap.entrySet()); 
		  //通过比较器实现比较排序 
		  Collections.sort(mappingList, new Comparator<Map.Entry<String,Integer>>(){ 
		   public int compare(Map.Entry<String,Integer> mapping1,Map.Entry<String,Integer> mapping2){ 
		    return mapping2.getValue().compareTo(mapping1.getValue()); 
		   } 
		  }); 
		  
		  StringBuffer resultsb = new StringBuffer();
		  for(Map.Entry<String,Integer> mapping:mappingList){ 
		   resultsb.append((mapping.getKey()+":"+mapping.getValue()+"\n")); 
		  } 
		  
		  
		   BufferedWriter writer = null;
		   String resultFileName = AppConstants.PATH + "DemoResult/final_count";

			try {
				writer = new BufferedWriter(new FileWriter(resultFileName));
				writer.write(resultsb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	/**
	 * init estate news to a theme
	 * 
	 * @param pathName
	 * @throws IOException
	 */
	public void addThemeFlag(String pathName) throws IOException {
		System.out.println("start decrate estateNews........");
		String filePath = "";
		String path = filePath + pathName;
		if (!path.endsWith(separator)) {
			path = path + separator;
		}
		File file = new File(path);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
				FileWriter writer = new FileWriter(path + f.getName(), true);
				writer.write("\n" + this.DIC_FIAG);
				writer.close();
			}
		} else {
			System.out.println("error");
		}

		System.out.println(" decrate estateNews ended........");

	}

	/**
	 * 获得文件下下文件的个数
	 * 
	 * @return
	 */
	public int calFileCount(String pathName) {
		String filePath = "";
		File file = new File(pathName);
		System.out.println(filePath + pathName);
		if (!file.isDirectory()) {

			System.out.println("---------- 该文件不是一个目录文件 ----------");
			return 0;
		} else {
			System.out.println("---------- 这是一个目录文件夹 ----------");
			String[] filelist = file.list();
			return filelist.length;
		}

	}

	public static void main(String args[]) throws IOException {
		// add theme to estatenews just used the first one
		// new Demo().addThemeFlag(estateNews);

		new DemoBak().demo();
	}

}