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

	@Test
	public void contextLoads() {
	}

	@Test
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
		String listHql = "{\n" +
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
		"toutiao.com",listHql,contentHql,"com.sf.wxc.parser.ArticleFeedParser","entityclass",true,"http://www.toutiao.com",true,false,1,"tensorflow","");
		Class clazz = Class.forName(feed.getParserClass());
		BaseParser parser = (BaseParser) clazz.newInstance();
//		String doc = HttpClientUtil.httpGetRequest(feed.getUrl());
//		System.out.println(doc.toString());

/*		ObjectMapper jsonMapper = new ObjectMapper();
		XmlMapper mapper = new XmlMapper();
		Map map = jsonMapper.readValue(doc,Map.class);
		System.out.println(mapper.writeValueAsString(map));*/
//		JSONObject json = new JSONObject(doc.toString());
//		String xml = XML.toString(json);
		//System.out.println(xml);

		List<?> list = parser.parseListPage(feed);
		for(Object o: list){
			System.out.println(o.toString());
		}
	}

}
