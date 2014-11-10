package cn.focus.estatedic.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.ansj.splitWord.analysis.NlpAnalysis;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.focus.dc.news.model.News;
import cn.focus.dc.news.model.NewsPage;
import cn.focus.estatedic.dao.NewsDAO;
import cn.focus.estatedic.demo.DemoBak;
import cn.focus.estatedic.service.NewsService;
import cn.focus.estatedic.test.NewsAnalysisWeb;

@Path("index")
public class indexController {
	
	@Autowired
	private NewsService newsService;
	
	

	@Get("")
	public String index(Invocation inv) {
		return "index";
	}
	
	@Post("demopro")
	@Get("demopro")
	public String index(Invocation inv,@Param("choose") String choose,@Param("param1") String param1,@Param("param2") String param2) {
		
		/**
		 * do someting
		 */
		inv.addModel("result", JSONObject.toJSON(null));
		return "result";
		
		
	}
	
	@Get("demorun")
	public String demorun(Invocation inv) {
		new DemoBak().demo();
		return "@sucess";
	}
	
	
	@Get("page")
	public String page(Invocation inv) {
		inv.addModel("test", "tst");
		return "index";
	}
	
	@Get("demo")
	public String demo() {
		
		return "@" + NlpAnalysis.parse("我的我的你的东西");
		
	}
	
	@Get("newsAnalysis")
	public String newsAnalysis() {
		NewsAnalysisWeb na = new NewsAnalysisWeb();
		try {
			na.mainFun();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "@" + na.getResult();
		
	}
	
	/**
	 * 获取某个城市的新闻
	 * 测试：http://127.0.0.1:8080/index/news?cityId=1&offset=23&limit=10
	 * @param cityid　　城市
	 * @param limit　　查多少
	 * @param offset　表偏移
	 * @return
	 */
	@Get("news")
	public String getNews(@Param("cityId") int cityid,@Param("limit") int limit, @Param("offset") int offset) {
		
		List<News> newsList = newsService.getNews(cityid, offset, limit);
		
		return "@" + JSONObject.toJSONString(newsList);
	}
	
	/**
	 * 获取某个新闻的所以页面
	 * 测试：newsId=128209
	 * @param newsId
	 * @return
	 */
	@Get("pageNews") 
	public String getPageNows(@Param("newsId") Long newsId) {
		List<NewsPage> pageNews = newsService.getPageNews(newsId);
		return "@" + JSONObject.toJSONString(pageNews);
	}
	
	@Get("path")
	public String getPath() {
		String cp = System.getProperty("java.class.path");//系统的classpaht路径
		String udir = System.getProperty("user.dir");//用户的当前路径 
		
		return "@cp" + cp + "   udir:" + udir;
	}
	
	
    
	
}
