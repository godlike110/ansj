package cn.focus.estatedic.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.focus.estatedic.config.AppConstants;
import cn.focus.estatedic.demo.Demo;
import cn.focus.estatedic.demo.Preprocess;
import cn.focus.estatedic.demo.WordProbability;
import cn.focus.estatedic.lda.LdaMethod;

/**
 * process docs Thread
 * @author zhiweiwen
 *
 */
public class ProcessThread  {//extends Thread{
	
	private Preprocess preprocess;
	private String estatePath;
	private int fgnum;
	private int fFiles;
	private int sgnum;
	private int sFiles;
	private String noEstatePath;
	private int weigth;
	private List<List<WordProbability>> list ;
	private CountDownLatch cdl;
	
	public ProcessThread(String estatepath,int fgnum,int fFiles,String noEstatePath,int sgnum,int sFiles,int weight,List<List<WordProbability>> list,CountDownLatch cdl) {
		this.estatePath = estatepath;
		this.fFiles = fFiles;
		this.fgnum = fgnum;
		this.noEstatePath = noEstatePath;
		this.sgnum = sgnum;
		this.sFiles = sFiles;
		this.weigth = weigth;
		this.list = list;
		this.cdl = cdl;
		this.preprocess = new Preprocess(weight);
		
	}
	
	
	public void run() {
		System.out.println("processing the " + this.fgnum + ":" + this.sgnum
				+ "　examples．．．．");
		this.preprocess.ldaPreprocess(this.estatePath, this.fgnum, this.fFiles, this.noEstatePath, this.sgnum, this.sFiles);
		if(null==AppConstants.PATH) {
			AppConstants.PATH = System.getProperty("user.dir");
			if(!AppConstants.PATH.endsWith(File.separator)) {
				AppConstants.PATH = AppConstants.PATH + File.separator;
			}
			AppConstants.PATH += "dicConfig/";
		}
		String resultFileName = AppConstants.PATH + "DemoResult/lda/lda_"
				+ this.fgnum + "_" + this.sgnum;
		String lmArgs[] = { resultFileName, "-N", 2 + "",
				resultFileName };
		LdaMethod.lda(lmArgs);
		ArrayList<WordProbability> wordspros = (ArrayList<WordProbability>) Demo.calLdaBeta(
				resultFileName, this.fgnum, this.sgnum);
		list.add(wordspros);
		Demo.findWordsByCount(this.fgnum,this.sgnum, list);
		Demo.findWordsByProbability(this.fgnum,this.sgnum,list);
		//this.cdl.countDown();
		System.out.println("processing the " + this.fgnum + ":" + this.sgnum
				+ "　examples　ended!．．．．");
	}

}
