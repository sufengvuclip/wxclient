DROP TABLE IF EXISTS feed;
CREATE TABLE `feed` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `url` varchar(256) NOT NULL,
  `domain` varchar(64) NOT NULL,
  `listJhql` varchar(1024) NOT NULL,
  `contentJhql` varchar(1024) NOT NULL,
  `parserClass` varchar(256) NOT NULL,
  `entityClass` varchar(256) DEFAULT NULL,
  `contentPagePreUrl` varchar(256) DEFAULT NULL,
  `contentPageRedirect` BOOLEAN,
  `listPageMobile` BOOLEAN,
  `contentPageMobile` BOOLEAN,
  `frequency` int(10) unsigned DEFAULT 1,
  `tags` varchar(256) DEFAULT NULL,
  `category` varchar(256) DEFAULT NULL,
  `loginJson` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `feed_field__url__value` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='The base table for feed entities.'

alter table feed add COLUMN `loginJson` varchar(1024) DEFAULT NULL

insert into `feed` (`id`, `name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) values('1','tensorflow_toutiao','http://m.toutiao.com/search/?keyword=tensorflow&count=20','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','tensorflow','news_machinelearning');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('jiqixuexi_toutiao','http://m.toutiao.com/search/?keyword=机器学习&count=50','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','机器学习','news_machinelearning');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('shenduxuexi_toutiao','http://m.toutiao.com/search/?keyword=深度学习&count=50','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','深度学习','news_machinelearning');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('shenjingwangluo_toutiao','http://m.toutiao.com/search/?keyword=神经网络&count=50','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','神经网络','news_machinelearning');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('caffe_toutiao','http://m.toutiao.com/search/?keyword=机器学习Caffe&count=20','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','caffe','news_machinelearning');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('machinelearning_toutiao','http://m.toutiao.com/search/?keyword=machine%20learning&count=20','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','machine learning','news_machinelearning');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('paddlepaddle_toutiao','http://m.toutiao.com/search/?keyword=paddlepaddle&count=20','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','paddlepaddle','news_machinelearning');

INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('paddlepaddle_toutiao','http://m.toutiao.com/search/?keyword=爬虫&count=50','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','爬虫','news_crawler');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('nutch_toutiao','http://m.toutiao.com/search/?keyword=nutch&count=10','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','nutch','news_crawler');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('wangyezhuaqu_toutiao','http://m.toutiao.com/search/?keyword=网页抓取&count=10','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','网页抓取','news_crawler');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('wangluozhuaqu_toutiao','http://m.toutiao.com/search/?keyword=网络抓取&count=10','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','网络抓取','news_crawler');

INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('solr_toutiao','http://m.toutiao.com/search/?keyword=solr&count=10','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','solr','news_search');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('elasticsearch_toutiao','http://m.toutiao.com/search/?keyword=elasticsearch&count=20','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','elasticsearch','news_search');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('sousuojishu_toutiao','http://m.toutiao.com/search/?keyword=搜索技术&count=10','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','搜索技术','news_search');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('lucene_toutiao','http://m.toutiao.com/search/?keyword=lucene&count=10','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','lucene','news_search');

INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('ziranyuyanchulinlp_toutiao','http://m.toutiao.com/search/?keyword=自然语言处理NLP&count=30','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','NLP','news_nlp');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('ziranyuyanchulixuexi_toutiao','http://m.toutiao.com/search/?keyword=自然语言处理学习&count=10','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','自然语言处理学习','news_nlp');

INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('dashuju_toutiao','http://m.toutiao.com/search/?keyword=大数据&count=30','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','大数据','news_bigdata');
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) VALUES('shujuwajue_toutiao','http://m.toutiao.com/search/?keyword=数据挖掘&count=20','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','数据挖掘','news_bigdata');

# tuicool search
INSERT INTO `feed` (`name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`, `loginJson`) VALUES('tensorflow_tuicool','http://www.tuicool.com/search?kw=tensorflow','tuicool.com','{"articles":{"_type":"list","from":".//div[@class=''list_article'']/div[@class=''single_fake'']","select":{"title":"text:.//a[@class=''article-list-title'']","url":"text:.//a[@class=''article-list-title'']/@href"}}}','{"content":"html:.//div[@class=''article_body'']/div[1]","author":"text:.//span[@class=''from'']/a[1]","originalUrl":"text:.//div[@class=''source'']/a[1]","tags":"text:.//div[@class=''article_meta'']//span[@class=''new-label'']"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.tuicool.com','0','0','0','1','tensorflow','machinelearning','{"loginurl":"http://www.tuicool.com/login","formdata":{"utf8":"✓","authenticity_token":"5vg/Z5gF6fAL2cgHONRAHjIqI2PG8zxjB5H5AzpwLOk=","email":"jiqixuexinlp@gmail.com","password":"meiyoumima","remember":"1"},"headers":{"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8","Accept-Encoding":"gzip, deflate","Accept-Language":"zh-CN,zh;q=0.8,en;q=0.6","Cache-Control":"max-age=0","Connection":"keep-alive","Content-Type":"application/x-www-form-urlencoded","Host":"www.tuicool.com","Origin":"http://www.tuicool.com","Referer":"http://www.tuicool.com/login","Upgrade-Insecure-Requests":"1","User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36"}}');

DROP TABLE IF EXISTS article;
CREATE TABLE `article` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `feedId` int(10) unsigned NOT NULL,
  `domain` varchar(64) NOT NULL,
  `title` varchar(256) NOT NULL,
  `url` varchar(256) NOT NULL,
  `originalUrl` varchar(256) DEFAULT NULL,
  `thumbnailUrl` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `content` TEXT NOT NULL,
  `author` varchar(256) DEFAULT NULL,
  `publishDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `grabDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tags` varchar(256) DEFAULT NULL,
  `category` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `article_field__url__value` (`url`),
  UNIQUE KEY `article_field__title__value` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='The base table for article entities.'

alter table article add COLUMN `originalUrl` varchar(256) DEFAULT NULL
  SELECT * FROM taxonomy_term_field_data a, taxonomy_term_data b WHERE a.vid='category' AND a.tid=b.tid


