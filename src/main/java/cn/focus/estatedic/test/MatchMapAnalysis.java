package cn.focus.estatedic.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.sohu.sce.repackaged.net.rubyeye.xmemcached.utils.ByteUtils;

public class MatchMapAnalysis {

	public static void main(String args[]) throws IOException{
		StringBuffer text = new StringBuffer();
		Map<Integer,String> map = new TreeMap();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("result/matchtable")));

			String s = null;
			while ((s = br.readLine()) != null) {
				String array[] = s.split(":");
				int length = array.length;
				try{
					map.put(Integer.parseInt(array[length-1].trim()), array[0]);
				}catch(Exception e){
					
					e.printStackTrace();
				}
				
			}
			 
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Iterator<Integer> it = map.keySet().iterator();
		File file = new File("result/matchanalysis");
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		StringBuffer sb = new StringBuffer();
		while(it.hasNext()){
			Integer key = (int) it.next();
			sb.append(key+":"+map.get(key) + "\n");
		}
		fos.write(ByteUtils.getBytes(sb.toString()));
		fos.close();
	}
}
