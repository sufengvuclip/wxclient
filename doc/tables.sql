CREATE TABLE `feed` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `url` varchar(256) NOT NULL,
  `domain` varchar(64) NOT NULL,
  `listJhql` varchar(1024) NOT NULL,
  `contentJhql` varchar(1024) NOT NULL,
  `parserClass` varchar(256) NOT NULL,
  `entityClass` varchar(256) DEFAULT NULL,
  `contentPagePreUrl` varchar(64) DEFAULT NULL,
  `contentPageRedirect` BOOLEAN,
  `listPageMobile` BOOLEAN,
  `contentPageMobile` BOOLEAN,
  `frequency` int(10) unsigned DEFAULT 1,
  `tags` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `feed_field__url__value` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='The base table for feed entities.'