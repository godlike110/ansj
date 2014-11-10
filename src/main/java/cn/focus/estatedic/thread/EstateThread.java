package cn.focus.estatedic.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.focus.estatedic.demo.Demo;
import cn.focus.estatedic.demo.WordProbability;

/**
 * MAIN WORK THREAD
 * @author zhiweiwen
 *
 */
public class EstateThread extends Thread{

	private CountDownLatch cdl;
	private int step;
	private int k2min;
	private int k2;
	private String estateFilePath;
	private int fFiles;
	private String noEstateFilePath;
	private int sFiles;
	private int weight;
	
	public EstateThread(CountDownLatch cdl,int step, int k2min, int k2,
			String estateFilePath, int fFiles,
			String noEstateFilePath, int sFiles, int weight) {
		super();
		this.cdl = cdl;
		this.step = step;
		this.k2min = k2min;
		this.k2 = k2;
		this.estateFilePath = estateFilePath;
		this.fFiles = fFiles;
		this.noEstateFilePath = noEstateFilePath;
		this.sFiles = sFiles;
		this.weight = weight;
	}

	public void run() {
		System.out.println("starting the " + step + " EstateThread!");
		// TODO Auto-generated method stub
		List<List<WordProbability>> list = new ArrayList();
		for (int j = k2min; j < k2; j++) {
			
			ProcessThread pt = new ProcessThread(estateFilePath, step, fFiles, noEstateFilePath, j,sFiles,weight,list,null);
			pt.run();
		}
		this.cdl.countDown();
		System.out.println("ended the " + step + " EstateThread!");
	}
	
}
