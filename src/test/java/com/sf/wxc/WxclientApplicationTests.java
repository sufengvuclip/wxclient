package com.sf.wxc;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sf.wxc.beans.Article;
import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.WXApp;
import com.sf.wxc.parser.BaseParser;
import com.sf.wxc.service.TT;
import com.sf.wxc.service.WX;
import com.sf.wxc.service.WXAppService;
import com.sf.wxc.util.HttpClientUtil;
import com.sf.wxc.util.WXUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		Feed feed = new Feed(0,"test","http://m.toutiao.com/search_content/?offset=0&count=100&from=search_tab&keyword=%E6%9C%BA%E5%99%A8%E5%AD%A6%E4%B9%A0&csrfmiddlewaretoken=undefined",
		"","","contentjhql","com.sf.wxc.parser.ArticleFeedParser","entityclass",1);
		Class clazz = Class.forName(feed.getParserClass());
		BaseParser parser = (BaseParser) clazz.newInstance();
		String doc = HttpClientUtil.httpGetRequest(feed.getUrl());
		System.out.println(doc.toString());

		ObjectMapper jsonMapper = new ObjectMapper();
		XmlMapper mapper = new XmlMapper();
		Map map = jsonMapper.readValue(doc,Map.class);
		System.out.println(mapper.writeValueAsString(map));

		parser.parseListPage(doc.toString(),feed.getListJhql());
	}

}
