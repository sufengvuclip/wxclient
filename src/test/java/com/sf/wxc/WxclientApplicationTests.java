package com.sf.wxc;

import com.sf.wxc.beans.Article;
import com.sf.wxc.beans.WXApp;
import com.sf.wxc.service.TT;
import com.sf.wxc.service.WX;
import com.sf.wxc.service.WXAppService;
import com.sf.wxc.util.WXUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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

}
