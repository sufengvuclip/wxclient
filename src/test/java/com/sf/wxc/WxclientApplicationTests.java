package com.sf.wxc;

import com.sf.wxc.beans.Article;
import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.WXApp;
import com.sf.wxc.parser.BaseParser;
import com.sf.wxc.service.TT;
import com.sf.wxc.service.WX;
import com.sf.wxc.service.WXAppService;
import com.sf.wxc.util.WXUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
				"      \"from\": \".//div[@class='list_article']/div[@class='single_fake']\",\n" +
				"      \"select\": {\n" +
				"        \"title\": \"text:.//a[@class='article-list-title']\",\n" +
				"        \"url\": \"text:.//a[@class='article-list-title']/@href\"\n" +
				"      }\n" +
				"    }\n" +
				"  }";
		String contentHql = "{\n" +
				"    \"content\": \"html:.//div[@class='article_body']\",\n" +
				"    \"author\": \"text:.//span[@class='from']/a[1]\",\n" +
				"    \"tags\": \"text:.//div[@class='article_meta']//span[@class='new-label']\"\n" +
				"  }";
		String loginJson = "{\n" +
				"  \"loginurl\": \"http://www.tuicool.com/login\",\n" +
				"  \"formdata\":{\n" +
				"    \"utf8\":\"✓\",\n" +
				"    \"authenticity_token\":\"5vg/Z5gF6fAL2cgHONRAHjIqI2PG8zxjB5H5AzpwLOk=\",\n" +
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
				"    \"Origin\":\"http://www.tuicool.com\",\n" +
				"    \"Referer\":\"http://www.tuicool.com/login\",\n" +
				"    \"Upgrade-Insecure-Requests\":\"1\",\n" +
				"    \"User-Agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36\"\n" +
				"  }\n" +
				"}";
		Feed feed = new Feed(0,"test","http://www.tuicool.com/search?kw=Paddle8：艺术品在线拍卖行",
				"tuicool.com",listHql,contentHql,"com.sf.wxc.parser.ArticleFeedParser","com.sf.wxc.beans.FeedArticle",false,"http://www.tuicool.com",false,false,1,"machinelearning","news_machinelearning",loginJson,true);
		Class clazz = Class.forName(feed.getParserClass());
		BaseParser parser = (BaseParser) clazz.newInstance();
		List<?> list = parser.parseListPage(feed);
		for(Object o: list){
			System.out.println(o.toString());
		}
	}

}
