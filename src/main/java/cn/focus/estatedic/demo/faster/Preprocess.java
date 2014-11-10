package cn.focus.estatedic.demo.faster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.FilterModifWord;

import cn.focus.estatedic.config.AppConstants;
import cn.focus.estatedic.utils.HtmlFilterUtil;

public class Preprocess {

	//Map<String, Integer> matchtable = new HashMap();
	int wordid = 1;
	List<String> docs = new ArrayList<String>();
	String result = new String();
	int weight = 1;
 
	static {
		if(null==AppConstants.PATH) {
			AppConstants.PATH = System.getProperty("user.dir");
			if(!AppConstants.PATH.endsWith(File.separator)) {
				AppConstants.PATH = AppConstants.PATH + File.separator;
			}
			AppConstants.PATH += "dicConfig/";
		}
		initStopWordsAndStopNatures();
		System.out.println("root path: " + AppConstants.PATH);
	}
	

	public Preprocess(int weight) {
		this.weight = weight;
	}

	public void ldaPreprocess(String estateFilePath, int i, int m1,
			String nonEstateFilePath, int j, int m2) {
		System.out.println("read estatenews collection " + i + " ......");
		readFolder(estateFilePath, i, m1);
		System.out.println("read estatenews collection " + i + " ended ......");
		System.out.println("read non estatenews collection " + i + " ......");
		readFolder(nonEstateFilePath, j, m2);
		System.out.println("read non estatenews collection " + i
				+ " ended ......");
		analysisStr(i, j);
		reset();
	}
	
	public void reset(){
		result= null;
		wordid = 1;
		 
	}

	public static void initStopWordsAndStopNatures() {
		
		List<String> stopWord = new ArrayList();

		System.out.println("init stopwords and natures");
		// 停词表
		String stopWordFile = AppConstants.PATH + "stopword.txt";
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
		stopWord.clear();
		FilterModifWord.insertStopNatures(" ");
		FilterModifWord.insertStopNatures("t");
		FilterModifWord.insertStopNatures("tg");
		FilterModifWord.insertStopNatures("s");
		FilterModifWord.insertStopNatures("f");
		FilterModifWord.insertStopNatures("vshi");
		FilterModifWord.insertStopNatures("vyou");
		FilterModifWord.insertStopNatures("a");
		FilterModifWord.insertStopNatures("ad");
		// FilterModifWord.insertStopNatures("an");
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
		FilterModifWord.insertStopNatures("v");

		System.out.println("init stopwords and natures success!!");
	}

	public void analysisStr(int estateGroup, int nonEstateGroup) {
		Map<String, Integer> matchtable = new HashMap();
		System.out.println("analysising　" + estateGroup + " : "
				+ nonEstateGroup);
		String matchtableName = AppConstants.PATH + "DemoResult/matchTable/matchtable_"
				+ estateGroup + "_" + nonEstateGroup;
		String ldaName = AppConstants.PATH + "DemoResult/lda/lda_"
				+ estateGroup + "_" + nonEstateGroup;
		List<List<Term>> tmpTermList = new ArrayList<List<Term>>();
		int flag = 0;;
		try {
			for (String str : docs) {
				System.out.println("analsis doc "  + flag + "start!!");
				System.out.println("start cut word: " + flag);
				List<Term> parse = NlpAnalysis.parse(str);
				System.out.println("end cut word: " + flag);
				System.out.println("start nature word: " + flag);
				if(flag==16) {
					System.out.println("hell0");
				}
				new NatureRecognition(parse).recognition();
				System.out.println("end nature word: " + flag);
				System.out.println("start filter modifyword:" + flag);
				List<Term> terms = FilterModifWord.modifResult(parse);
				System.out.println("end filter modifyword:" + flag);
				//put List<term> int tmp List not grobal 
				tmpTermList.add(terms);
				
				Term t;
				String ts ;
				for (int j = 0; j < terms.size(); j++) {
					t = terms.get(j);
					ts = t.getRealName() + "\t" + t.getNatureStr();
					if (!matchtable.containsKey(ts)) {
						matchtable.put(ts, wordid);
						wordid++;
					}
				}
				
				System.out.println("analsis doc "  + flag + "end!!");
				flag ++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("start write lad file:" + estateGroup + "_" + nonEstateGroup);
		BufferedWriter matchWriter = null;
		StringBuffer sb2 = new StringBuffer();
		try {
			matchWriter = new BufferedWriter(new FileWriter(matchtableName));
			Iterator it = matchtable.keySet().iterator();
			String s;
			while (it.hasNext()) {
				s = (String) it.next();
				sb2.append(s + ":" + matchtable.get(s) + "\n");
			}
			matchWriter.write(sb2.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				matchWriter.flush();
				matchWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("end write lad file:" + estateGroup + "_" + nonEstateGroup);
		
		
		docs.clear();
		StringBuffer sb = new StringBuffer();
		int flag1 = 0;
		for (List<Term> terms : tmpTermList) {
			System.out.println("start analysis term " + flag1);
			Map<String, Integer> termmap = new HashMap();
			Map<String, Integer> newtermmap = new HashMap();
			for (int j = 0; j < terms.size(); j++) {
				Term t = terms.get(j);
				String ts = t.getRealName() + "\t" + t.getNatureStr();
				String tsname = t.getName();
				String tsnature = t.getNatureStr();
				if (tsname.length() == 1) {
					continue;
				}
				if (tsnature.equals("nw") || tsnature.equals("userDefine")) {
					if (newtermmap.containsKey(ts)) {
						newtermmap.put(ts, newtermmap.get(ts) + 1);
					} else {
						newtermmap.put(ts, 1);
					}
				} else {
					if (termmap.containsKey(ts)) {
						termmap.put(ts, termmap.get(ts) + 1);
					} else {
						termmap.put(ts, 1);
					}
				}
			}
			List<Word> words = new ArrayList();
			Iterator<String> it = termmap.keySet().iterator();
			while (it.hasNext()) {
				String ts = it.next();
				int count = termmap.get(ts);
				words.add(new Word(ts, count));
			}

			Iterator<String> newit = newtermmap.keySet().iterator();
			while (newit.hasNext()) {
				String ts = newit.next();
				int count = newtermmap.get(ts);
				words.add(new Word(ts, count * weight));
			}

			for (int j = 0; j < words.size(); j++) {
				Word w = words.get(j);
				String s = w.name;
				int id = matchtable.get(s);
				sb.append(id + ":" + w.count + "\t");
			}
			sb.append("\n");
			System.out.println("end analysis term " + flag1);
		}

		System.out.println("write lda file : " + flag1);
		result = sb.toString();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(ldaName));
			writer.write(result);
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
		System.out.println("write lda file ended : " + flag1);
	}

	/**
	 * 读取文件夹
	 * 
	 * @return
	 */
	public void readFolder(String filePath, int group, int groupSize) {
		try {
			File file = new File(filePath);
			if (!file.isDirectory()) {
				System.out.println("---------- this isn't a directory ----------");
			} else {
				System.out.println("---------- this is a directory ----------");
				String[] filelist = file.list();
				int min = group * groupSize;
				int max = min + groupSize;
				File readfile;
				String absolutepath;
				String filename;
				for (int i = min; i < filelist.length && i < max; i++) {
					readfile = new File(filePath + Demo.separator
							+ filelist[i]);
					absolutepath = readfile.getAbsolutePath();// 文件的绝对路径
					filename = readfile.getName();// 读到的文件名
					readFile(absolutepath, filename);// 调用readFile方法读取文件夹下所有文件
				}
				//System.out.println("---------- 所有文件操作完毕 ----------");
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
	public void readFile(String absolutepath, String filename) {
		StringBuffer text = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(absolutepath)));

			String s = null;
			while ((s = br.readLine()) != null) {
				text.append(s);
			}
			docs.add(HtmlFilterUtil.htmlFilter(text.toString()));
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getResult() {
		return result;
	}

}
