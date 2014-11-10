package cn.focus.estatedic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

public class WordFilterUtil {
	
	/**
	 * 过滤一下不需要的词
	 * @param str
	 * @return
	 */
	public static boolean isIllegalWord(String str) {
		
		Pattern p = Pattern.compile("[\\|?|-|>|<]");
		boolean flag = false;
		if(StringUtils.isBlank(str) || str.equals(" ")) {
			flag =  false;
		} else {
			Matcher m = p.matcher(str);
			if(m.find()) {
				flag = true;
			}
		}
		return flag;
		
	}

}
