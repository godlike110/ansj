package cn.focus.estatedic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.focus.dc.news.model.News;
import cn.focus.dc.news.model.NewsPage;
import cn.focus.estatedic.dao.NewsDAO;
import cn.focus.estatedic.dao.NewsPageDAO;


@Service
public class NewsService {
	
	@Autowired
	private NewsPageDAO newsPageDao;
	
	@Autowired
	private NewsDAO newsDao;

	/**
	 * 获取某篇新闻的每一页新闻
	 * @param newsId
	 * @return
	 */
	public List<NewsPage> getPageNews(long newsId) {
		
		List<NewsPage> pageNews = newsPageDao.getAll(newsId);
		return pageNews;
	
	}
	
	/**
	 * 
	 * @param cityId　城市Id
	 * @param offset 偏移量
	 * @param limit　　查找上限
	 * @return
	 */
	public List<News> getNews(int cityId,int offset,int limit) {
		
		List<News> newsList = newsDao.getNews(cityId, offset, limit);
		return newsList;
		
	}
	
}
