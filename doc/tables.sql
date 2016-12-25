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
  PRIMARY KEY (`id`),
  UNIQUE KEY `feed_field__url__value` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='The base table for feed entities.'

  insert into `feed` (`id`, `name`, `url`, `domain`, `listJhql`, `contentJhql`, `parserClass`, `entityClass`, `contentPagePreUrl`, `contentPageRedirect`, `listPageMobile`, `contentPageMobile`, `frequency`, `tags`, `category`) values('1','tensorflow_toutiao','http://m.toutiao.com/search/?keyword=tensorflow&count=20','toutiao.com','{\"articles\":{\"_type\":\"list\",\"from\":\".//div[@class=\'list_content\']/section\",\"select\":{\"title\":\"text:.//h3\",\"url\":\"text:./a[1]/@href\"}}}','{\"content\":\"html:.//div[@class=\'article-content\']\",\"author\":\"text:.//div[@class=\'articleInfo\']/span[@class=\'src\']\",\"tags\":\"text:.//a[@class=\'label-link\']\"}','com.sf.wxc.parser.ArticleFeedParser','com.sf.wxc.beans.FeedArticle','http://www.toutiao.com','1','1','0','1','tensorflow','news_machinelearning');


DROP TABLE IF EXISTS article;
CREATE TABLE `article` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `feedId` int(10) unsigned NOT NULL,
  `domain` varchar(64) NOT NULL,
  `title` varchar(256) NOT NULL,
  `url` varchar(256) NOT NULL,
  `thumbnailUrl` varchar(256) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `content` TEXT NOT NULL,
  `author` varchar(256) DEFAULT NULL,
  `publishDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `grabDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tags` varchar(256) DEFAULT NULL,
  `category` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `article_field__url__value` (`url`),
  UNIQUE KEY `article_field__title__value` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='The base table for article entities.'