package cn.focus.estatedic.dao;

import java.util.List;

import cn.focus.dc.news.model.NewsPage;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.ShardBy;


@DAO(catalog = "newscenter")
public interface NewsPageDAO {
	

    String SELECT_ALL_SQL = "SELECT  id, news_id, page, content, front_content, page_title FROM $news_page ";

    @SQL(SELECT_ALL_SQL + "WHERE news_id = :1 and page = :2")
    NewsPage get(@ShardBy Long newsid, int page);

    @SQL(SELECT_ALL_SQL + "WHERE news_id=:1 order by page")
    List<NewsPage> getAll(@ShardBy Long newsid);

}
