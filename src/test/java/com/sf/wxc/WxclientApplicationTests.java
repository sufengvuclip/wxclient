package com.sf.wxc;

import com.sf.wxc.beans.Article;
import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.WXApp;
import com.sf.wxc.parser.BaseParser;
import com.sf.wxc.repository.db.feeddb.FeedArticleDbRepository;
import com.sf.wxc.service.TT;
import com.sf.wxc.service.WX;
import com.sf.wxc.service.WXAppService;
import com.sf.wxc.util.WXUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxclientApplicationTests {

	@Resource
	WXUtil wxUtil;

	@Resource
	TT tt;

	@Resource
	WX wx;

	@Resource
	WXAppService wxAppService;

	@MockBean
	FeedArticleDbRepository feedArticleDbRepository;

	//@Test
	public void contextLoads() {
	}


	public void uploadnews(){
		List<Article> articles = tt.search("搞笑");
		List<Article> uploadArticles = new ArrayList<>();
		WXApp testApp = wxAppService.getWXApp(wxAppService.TEST_XK);
		for(Article article: articles){
			if(article!=null && uploadArticles.size()<2){
				article = wxUtil.convertArticle2wx(article, testApp);
				if(article!=null)
					uploadArticles.add(article);
			}else {
				break;
			}
		}

		String ret = null;
		if(uploadArticles.size()>0)
			ret = wx.uploadnews(uploadArticles, testApp);
		System.out.println("ret: "+ret);
	}
	@Test
	public void testFeeds() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
		given(feedArticleDbRepository.existsUrl(anyString())).willReturn(false);
/*		String listHql = "{\n" +
				"    \"articles\": {\n" +
				"        \"_type\": \"list\",\n" +
				"        \"from\": \".//div[@class='list_content']/section\",\n" +
				"        \"select\": {\n" +
				"            \"title\": \"text:.//h3\",\n" +
				"            \"url\": \"text:./a[1]/@href\""+
				"        }\n" +
				"    }"+
				"}";
		String contentHql = "{\n" +
				"        \"content\": \"text:.//div[@class='article-content']\""+
				"}";
		Feed feed = new Feed(0,"test","http://m.toutiao.com/search/?keyword=tensorflow&count=10",
		"toutiao.com",listHql,contentHql,"com.sf.wxc.parser.ArticleFeedParser","entityclass",true,"http://www.toutiao.com",true,false,1,"tensorflow","","");*/

		String listHql = "{\n" +
				"    \"articles\": {\n" +
				"      \"_type\": \"list\",\n" +
				"      \"from\": \".//div[@class='aricle_item_info']/div[@class='title']\",\n" +
				"      \"select\": {\n" +
				"        \"title\": \"text:.//a[@target='_blank']\",\n" +
				"        \"url\": \"text:.//a[@target='_blank']/@href\"\n" +
				"      }\n" +
				"    }\n" +
				"  }";
		/*String contentHql = "{\n" +
				"    \"content\": \"html:.//div[@class='article_body']\",\n" +
				"    \"author\": \"text:.//span[@class='from']/a[1]\",\n" +
				"    \"tags\": \"text:.//div[@class='article_meta']//span[@class='new-label']\"\n" +
				"  }";*/

        String contentHql = "{\"content\":\"html:.//div[@class='article_body']\",\"author\":\"text:.//span[@class='from']/a[1]\",\"originalUrl\":\"text:.//div[@class='source']/a[1]\",\"tags\":\"text:.//div[@class='article_meta']//span[@class='new-label']\"}";
		String loginJson = "{\n" +
				"  \"loginurl\": \"https://www.tuicool.com/login\",\n" +
				"  \"formdata\":{\n" +
				"    \"utf8\":\"✓\",\n" +
				"    \"authenticity_token\":\"UhnqxBZZNVRDCz0jzOWTSNeazqevsIqrsZJOenY4aS4cESoR4NrqJR4PTTgWUvWBg9xldoZpAKDuW+3pxBgjLA==\",\n" +
				"    \"email\":\"sufengster@163.com\",\n" +
				"    \"password\":\"meiyoumima\",\n" +
				"    \"remember\":\"1\"\n" +
				"  },\n" +
				"  \"headers\":{\n" +
				"    \"Accept\":\"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",\n" +
				"    \"Accept-Encoding\":\"gzip, deflate\",\n" +
				"    \"Accept-Language\":\"zh-CN,zh;q=0.8,en;q=0.6\",\n" +
				"    \"Cache-Control\":\"max-age=0\",\n" +
				"    \"Connection\":\"keep-alive\",\n" +
				"    \"Content-Type\":\"application/x-www-form-urlencoded\",\n" +
				"    \"Host\":\"www.tuicool.com\",\n" +
				"    \"Origin\":\"https://www.tuicool.com\",\n" +
				"    \"Referer\":\"https://www.tuicool.com/login\",\n" +
				"    \"Upgrade-Insecure-Requests\":\"1\",\n" +
				"    \"User-Agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36\"\n" +
				"  }\n" +
				"}";
		Feed feed = new Feed(0,"test","https://www.tuicool.com/search?kw=万亿user_tags级实时推荐系统数据库设计&t=1",
				"tuicool.com",listHql,contentHql,"com.sf.wxc.parser.ArticleFeedParser","com.sf.wxc.beans.FeedArticle",false,"https://www.tuicool.com",false,false,1,"machinelearning","news_machinelearning",loginJson,true,true, false , false);
		Class clazz = Class.forName(feed.getParserClass());
		BaseParser parser = (BaseParser) clazz.newInstance();
		List<?> list = parser.parseListPage(feed);
		for(Object o: list){
			System.out.println(o.toString());
		}
	}

	@Test
	public void testToutiaoFeeds() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
		given(feedArticleDbRepository.existsUrl(anyString())).willReturn(false);
		String listHql = "{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class='list_content']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}";

		String contentHql = "{\"content\":\"html:.//div[@class='article-content']\",\"author\":\"text:.//div[@class='articleInfo']/span[@class='src']\",\"tags\":\"text:.//a[@class='label-link']\"}";
		String loginJson = null;
		Feed feed = new Feed(0,"test","http://m.toutiao.com/search/?keyword=tensorflow&count=20",
				"toutiao.com",listHql,contentHql,"com.sf.wxc.parser.ArticleFeedParser","com.sf.wxc.beans.FeedArticle",true,"http://www.toutiao.com",true,false,1,"machinelearning","news_machinelearning",loginJson,true,true, true, false);
		Class clazz = Class.forName(feed.getParserClass());
		BaseParser parser = (BaseParser) clazz.newInstance();
		List<?> list = parser.parseListPage(feed);
		for(Object o: list){
			System.out.println(o.toString());
		}
	}

}
