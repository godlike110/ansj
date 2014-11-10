package cn.focus.estatedic.dao;

import java.util.List;

import cn.focus.dc.news.model.News;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.ShardBy;

@DAO(catalog="newscenter")
public interface NewsDAO {
	
    String SELECT_ALL_SQL = "SELECT  news_id, hot, show_time, title, short_title, source_name, author, "
            + "author_link, title_color, creator_id, update_time, city_id, html_title, if_comment, if_mark,"
            + " if_copyright, hidden_mark, split_page, editor_id, photo_grapher, news_type, img_logo, keywords,"
            + " news_desc, template_type, source_site_link, source_page_link, status FROM $news ";

    @SQL(SELECT_ALL_SQL + "WHERE news_id = :1 and status!=2")
    News get(Long newsid, @ShardBy int cityid);
    
    @SQL(SELECT_ALL_SQL + "WHERE city_id = :1 and status!=2 limit :2,:3")
    List<News> getNews(@ShardBy int cityid,int offset,int limit);
    
    @SQL("SELECT city_id FROM $news WHERE news_id = :1 and status!=2")
    int getCityId(@ShardBy Long newsId);

    @SQL(SELECT_ALL_SQL + "WHERE news_id IN (:1) and status!=2")
    List<News> getList(List<Long> newsIdList, @ShardBy int cityid,int offset,int limit);

}
