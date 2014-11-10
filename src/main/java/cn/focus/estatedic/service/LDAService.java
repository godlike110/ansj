package cn.focus.estatedic.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.FilterModifWord;
import org.ansj.util.MyStaticValue;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cn.focus.estatedic.config.AppConstants;
import cn.focus.estatedic.test.Word;

import com.alibaba.fastjson.JSONObject;
import com.sohu.sce.repackaged.net.rubyeye.xmemcached.utils.ByteUtils;

@Service
public class LDAService {
	
	
	public static Log logger = LogFactory.getLog(LDAService.class);
	
	public static String reg = "^[0-9]+.$";
	
	public static Pattern pattern ;
	
	public static String rootPath = "";
	
	
	static {
		pattern = Pattern.compile(reg);
		rootPath = System.getProperty("user.dir");
		System.out.println(rootPath);
	}
	
	/**
	 * 按路径分析
	 * @param path 文件路径
	 * @param type 1 分词　２　关键字提取　２新词发现
	 * @throws IOException 
	 */
	public void analysis(String pathName,int type,int page) throws IOException {
		//initNatures();
		//initStopWord();
		String separator = File.separator;
		String filePath = "";
		if(!rootPath.endsWith(separator)) {
			filePath = rootPath  + separator;
		}
		File file = new File(filePath + pathName);
		int count= 0;
		System.out.println("handle the " + page + " newsList");
		int rangel = page*1030;
		int rangeM = (page+1)*1030;
		Boolean flag = file.isDirectory();
		if(flag) {
			File[] files = file.listFiles();
			for(File f :files) {
				count ++;
				if(count>rangel && count<=rangeM) {
				analysisStr(getFileStr(f),type);
				}
			}
		} else {
			analysisStr(getFileStr(file),type);
		}
		
		//collectAnalysis(type);
	}
	
	
	/**
	 * 获取文件内容
	 * @param file
	 * @throws IOException 
	 */
	public String getFileStr(File file) throws IOException {
		
		StringBuffer sb = new StringBuffer();
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[1024];
		while(fis.read(buf)!=-1) {
			sb.append(new String(buf));
			buf = new byte[1024];
		}		
		fis.close();
		return sb.toString();
		
	}
	
	/**
	 * 分析单个文件
	 * @param str
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void analysisStr(String str,int type) throws IOException {
		
		switch(type) {
		
		case 1:
			List<Term> pars = NlpAnalysis.parse(str);
			new NatureRecognition(pars).recognition();
			AppConstants.TERMLIST.add(FilterModifWord.modifResult(pars));
		case 2:
			KeyWordComputer kc = new KeyWordComputer(20);
			List<Keyword> kl = kc.computeArticleTfidf(str);		
		    AppConstants.KEYWORDLIST.add(kl);
		//System.out.println(pars);SAST
		
		//String[] parss = clearAnalysisResult(pars.toString().split(","));
				
		//List<String> listStr = pars.toArray();
		//System.out.println(JSONObject.toJSONString(parss));
		
//		KeyWordComputer kc = new KeyWordComputer(20);
//		List<Keyword> kl = kc.computeArticleTfidf(str);
//		for(Keyword kw:kl) {
//			System.out.println(kw);
//		}
		}
		
		
	}
	
	private void printResult(int type,String outpath) throws IOException {
		System.out.println("正在打印结果:");
		File file = new File(outpath);
		if(file.exists()) {
			file.delete();
		} else {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		StringBuffer sb = new StringBuffer();
		switch(type) {
		case 1:

			Set<String> ks = AppConstants.TERMMAP.keySet();
			Iterator it = ks.iterator();

			while(it.hasNext()) {
				sb.append(it.next());
				sb.append(" " + AppConstants.TERMMAP.get(sb.toString()));
				sb.append("\n");
				fos.write(ByteUtils.getBytes(sb.toString()));
				sb = new StringBuffer();
			}
		case 2:
			Set<String> kwm = AppConstants.KEYWORDMAP.keySet();
			Iterator kit = kwm.iterator();
			while(kit.hasNext()) {
				sb.append(kit.next());
				int value = AppConstants.KEYWORDMAP.get(sb.toString());
				if(value>10) {
					sb.append(" " + value);
					sb.append("\n");
					fos.write(ByteUtils.getBytes(sb.toString()));
				}
				sb = new StringBuffer();
			}
		}
		System.out.println("打印结束．．．．．．．．．．");	
	}
	
	/**
	 * 分析结果汇总
	 * @throws IOException 
	 */
	public void collectAnalysis(int type) throws IOException {
		
		System.out.println("start collecting date：．．．．．．．．．．");	
		String separator = File.separator;
		String filePath = "";
		if(!rootPath.endsWith(separator)) {
			filePath = rootPath  + separator;
		}
		File file = new File(filePath + "result" + separator + "lda");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		StringBuffer sb = new StringBuffer();
		switch(type) {
		case 1:
		for(List<Term> tl:AppConstants.TERMLIST) {	
			List<Word> words = new ArrayList<Word>();
			Map<String, Integer> termMap = new HashMap();
			Map<String, Integer> newTermMap = new HashMap();
			for(Term tm:tl) {	
				
				
				String name = tm.getName();
				if (name.length() == 1) {
					continue;
				}
				String nature = tm.getNatureStr();
				if((nature.equals("nw") || nature.equals("userDefine")) ) {
				    if(newTermMap.containsKey(name)) {		
				    	newTermMap.put(name,newTermMap.get(name) + 1);
				    } else {
				    	newTermMap.put(name,1);
				    }
				} else {
				    if(termMap.containsKey(name)) {		
				    	termMap.put(name,termMap.get(name) + 1);
				    } else {
				    	termMap.put(name,1);
				    }
				}
				if(!AppConstants.MATCHMAP.containsKey(name)) {
					AppConstants.MATCHMAP.put(name, AppConstants.WORDNUM++);
				}
			}
			
			Set<String> tk = termMap.keySet();
			Iterator tit = tk.iterator();
			String tkey = "";
			while(tit.hasNext()) {
				tkey = (String) tit.next();
				words.add(new Word(tkey,termMap.get(tkey)));
			}
			
			Set<String> nk = newTermMap.keySet();
			Iterator nit = nk.iterator();
			String nkey = "";
			while(nit.hasNext()) {
				nkey = (String) nit.next();
				AppConstants.WORDS.add(new Word(nkey,newTermMap.get(nkey)*10));
			}
			
			for(Word w:words) {
				String key = w.name;
				int num = AppConstants.MATCHMAP.get(key);
				sb.append(num + ":" + w.count + "\t");
			}
			sb.append("\n");
		}
		System.out.println("Start creating ida file..............");
		fos.write(ByteUtils.getBytes(sb.toString()));
		System.out.println("creating ida ended..............");
		fos.close();
		
		coutMatchs();
//		coutIDAFile();
		
//		System.out.println("输出结果：．．．．．．．．．．");
//		Set<String> ks = AppConstants.WORDMAP.keySet();
//		Iterator it = ks.iterator();
//		while(it.hasNext()) {
//			String key = (String) it.next();
//			System.out.println(key + " " + AppConstants.WORDMAP.get(key));
//		}
		
		case 2:
			for(List<Keyword> kl:AppConstants.KEYWORDLIST) {
				for(Keyword kw : kl) {
					String name = kw.getName();
					if(!leftOrMove(name)) {
						continue;
					}
					Integer count = AppConstants.KEYWORDMAP.get(name);
					if(count!=null) {
						AppConstants.KEYWORDMAP.put(name, count+1);
					} else if (StringUtils.isNotBlank(name)) {
						AppConstants.KEYWORDMAP.put(name, 1);
					}
				}
			}
//			System.out.println("输出结果：．．．．．．．．．．");
//			Set<String> kwm = AppConstants.KEYWORDMAP.keySet();
//			Iterator kit = kwm.iterator();
//			while(kit.hasNext()) {
//				String key = (String) kit.next();
//				int value = AppConstants.KEYWORDMAP.get(key);
//				if(value>10) {
//					System.out.println(key + " " + value);
//				}
//			}
		}
			
	}
	
	public void coutIDAFile() throws IOException {
		System.out.println("Start creating ida file..............");
		File file = new File("/home/zhiweiwen/workspace/estatedic/result/ida");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		StringBuffer sb = new StringBuffer();
		String key = "";
		int id = 0;
		int count = 0;
		for(Word w:AppConstants.WORDS) {
			key = w.name;
			count = w.count;
			id = AppConstants.MATCHMAP.get(key);
			sb.append(id + ":" + count + "\n");
		}
		fos.write(ByteUtils.getBytes(sb.toString()));
		fos.close();
		System.out.println("creating ida file ended..............");
	}
	
	/**
	 * 打印词和对应的编号
	 * @throws IOException 
	 */
	public void coutMatchs() throws IOException {
		System.out.println("start creating match file.......");
		String separator = File.separator;
		String filePath = "";
		if(!rootPath.endsWith(separator)) {
			filePath = rootPath  + separator;
		}
		File file = new File(filePath + "result" + separator + "matchtable");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		StringBuffer sb = new StringBuffer();
		Set<String> keySet = AppConstants.MATCHMAP.keySet();
		Iterator it = keySet.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			sb.append( key + ":" + AppConstants.MATCHMAP.get(key) + "\n");
			
		}
		fos.write(ByteUtils.getBytes(sb.toString()));
		fos.close();
		System.out.println("creating match file ended .......");
	}
	
	/**
	 * 判断这条记录是否保留
	 * @param str
	 * @return true 保留　false 去除
	 */
	public boolean leftOrMove(String str) {
		
		if(str!=null && str.length()<=1) {
			return false;
		}
		
		if(StringUtils.isNumeric(str)) {
			return false;
		}
		Matcher m = pattern.matcher(str);
		while(m.find()) {
			return false;
		}
		return true;
		
	}
	
	/**
	 * 初始化停用属性
	 */
	private void initNatures() {
		
		System.out.println("*********************init natures start!***************************");
		
		FilterModifWord.insertStopNatures(" ");
		FilterModifWord.insertStopNatures("t");
		FilterModifWord.insertStopNatures("tg");
		FilterModifWord.insertStopNatures("s");
		FilterModifWord.insertStopNatures("f");
		FilterModifWord.insertStopNatures("vshi");
		FilterModifWord.insertStopNatures("vyou");
		FilterModifWord.insertStopNatures("a");
		FilterModifWord.insertStopNatures("ad");
		FilterModifWord.insertStopNatures("an");
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
		
		System.out.println("*************************init natures done!****************************");
		
	}
	
	/**
	 * 初始化停用词
	 * @param list
	 * @throws IOException 
	 */
	public void initStopWord() throws IOException {
		System.out.println("***************init stop words***************");
		List<String> stopWordList = new ArrayList<String>();
		File file = new File(AppConstants.PATH + "stopword.txt");
		BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		String line = fr.readLine();
		while(line!=null) {
			stopWordList.add(line);
			line = fr.readLine();
		}
		fr.close();
		FilterModifWord.insertStopWords(stopWordList);
		System.out.println("***************init stop words done!*************");
	}
	
	public static void main(String argv[]) throws IOException {
		
	//	System.out.println(System.getProperties());
		
		LDAService as = new LDAService();
		//ResourceBundle rsb = ResourceBundle.getBundle("library",Locale.getDefault());
		//MyStaticValue.userLibrary = System.getProperty("user.dir") + File.separator + "src/main/resources/dicConfig/library";
		//MyStaticValue.userLibrary = System.getProperty("user.dir") + File.separator + "src/main/resources/dicConfig/library/ambiguity.dic";
	//	as.analysis("/home/zhiweiwen/workspace/estatedic/news/",2);
	//	as.analysis("/home/zhiweiwen/workspace/estatedic/othernews/",2);
		as.initNatures();
		as.initStopWord();
		for(int i=0;i<2;i++) {
			as.analysis("diffnews",1,i);
		}
		as.collectAnalysis(1);
		
//		
//		for(int i=0;i<20;i++) {
//			as.analysis("/home/zhiweiwen/workspace/estatedic/news/",2,i);
//		}
//		as.printResult(2,"/home/zhiweiwen/workspace/estatedic/result/fangchanguanjianzi.txt");
//		
//		
//		
//		for(int i=0;i<20;i++) {
//			as.analysis("/home/zhiweiwen/workspace/estatedic/othernews/",1,i);
//		}
//		as.printResult(1,"/home/zhiweiwen/workspace/estatedic/result/feifangcanfenci.txt");
//		
//		
//		for(int i=0;i<20;i++) {
//			as.analysis("/home/zhiweiwen/workspace/estatedic/othernews/",2,i);
//		}
//		as.printResult(2,"/home/zhiweiwen/workspace/estatedic/result/feifangchanguanjianzi.txt");
	//	as.analysis("/home/zhiweiwen/workspace/estatedic/othernews/",1);
		//as.leftOrMove("4.");
		//as.initStopWord();
//		String s = " 　　/nw";
//		int index = s.indexOf("/");
//		
//		s = s.substring(0, index);
//		if(!StringUtils.isNotBlank(s)) {
//			int a = 1;
//		}
//		System.out.println(s);
		//as.analysisOneFile("3667817.txt");
		
		//提取关键词
//		KeyWordComputer kc = new KeyWordComputer(10);
//		String title = "维基解密否认斯诺登接受委内瑞拉庇护";
//		String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
//		List<Keyword> kl = kc.computeArticleTfidf(title, content);
//		System.out.println(kl);
		
		
	}
	

}
