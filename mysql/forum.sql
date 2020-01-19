/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80013
Source Host           : localhost:3306
Source Database       : forum

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2019-05-08 07:40:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '讨论内容ID',
  `title` varchar(64) NOT NULL COMMENT '文章标题',
  `userId` varchar(50) NOT NULL COMMENT '用户ID',
  `time` date NOT NULL COMMENT '发布时间',
  `sortId` int(2) NOT NULL COMMENT '分类ID',
  `abstractContent` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '摘要',
  `context` text NOT NULL COMMENT '正文',
  `readNumber` int(3) NOT NULL DEFAULT '0' COMMENT '阅读次数',
  `top` int(1) NOT NULL DEFAULT '0' COMMENT '置顶（1置顶，0不置顶）',
  PRIMARY KEY (`id`),
  KEY `article_sort_id` (`sortId`),
  KEY `article_userinfo_id` (`userId`),
  CONSTRAINT `article_sort_id` FOREIGN KEY (`sortId`) REFERENCES `sort` (`id`),
  CONSTRAINT `article_userinfo_id` FOREIGN KEY (`userId`) REFERENCES `userinfo` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for discuss
-- ----------------------------
DROP TABLE IF EXISTS `discuss`;
CREATE TABLE `discuss` (
  `id` int(11) NOT NULL COMMENT '评论的ID',
  `articleId` int(11) NOT NULL COMMENT '被评论的文章',
  `userId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论人',
  `time` date NOT NULL COMMENT '评论时间',
  `content` varchar(100) NOT NULL COMMENT '评论内容',
  PRIMARY KEY (`id`),
  KEY `discuss_article_id` (`articleId`),
  KEY `discuss_userInfo_id` (`userId`),
  CONSTRAINT `discuss_article_id` FOREIGN KEY (`articleId`) REFERENCES `article` (`id`),
  CONSTRAINT `discuss_userInfo_id` FOREIGN KEY (`userId`) REFERENCES `userinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of discuss
-- ----------------------------

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL COMMENT '专业名称',
  `schoolId` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `major_school_id` (`schoolId`),
  CONSTRAINT `major_school_id` FOREIGN KEY (`schoolId`) REFERENCES `school` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES ('1', '软件工程', '1');
INSERT INTO `major` VALUES ('2', '计算机科学与技术', '1');
INSERT INTO `major` VALUES ('3', '网络工程', '1');
INSERT INTO `major` VALUES ('4', '信息与计算科学', '1');
INSERT INTO `major` VALUES ('5', '英语', '2');
INSERT INTO `major` VALUES ('6', '商务英语', '2');
INSERT INTO `major` VALUES ('8', '法学', '3');
INSERT INTO `major` VALUES ('9', '汉语言学', '3');
INSERT INTO `major` VALUES ('10', '行政管理', '3');
INSERT INTO `major` VALUES ('11', '旅游管理', '3');
INSERT INTO `major` VALUES ('12', '秘书', '3');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(50) NOT NULL COMMENT '用户ID',
  `articleId` int(11) NOT NULL COMMENT '文章ID',
  PRIMARY KEY (`id`),
  KEY `record_userInfo_id` (`userId`),
  KEY `record_article_id` (`articleId`),
  CONSTRAINT `record_article_id` FOREIGN KEY (`articleId`) REFERENCES `article` (`id`),
  CONSTRAINT `record_userInfo_id` FOREIGN KEY (`userId`) REFERENCES `userinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of record
-- ----------------------------

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply` (
  `id` int(11) NOT NULL COMMENT '回复ID',
  `articleId` int(11) NOT NULL COMMENT '被评论的文章',
  `userId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `time` date NOT NULL COMMENT '回复时间',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '回复内容',
  `replyTo` int(11) NOT NULL COMMENT '文章的评论的ID',
  PRIMARY KEY (`id`),
  KEY `reply_artile_id` (`articleId`),
  KEY `reply_user_id` (`userId`),
  KEY `reply_discuss_id` (`replyTo`),
  CONSTRAINT `reply_artile_id` FOREIGN KEY (`articleId`) REFERENCES `article` (`id`),
  CONSTRAINT `reply_discuss_id` FOREIGN KEY (`replyTo`) REFERENCES `discuss` (`id`),
  CONSTRAINT `reply_user_id` FOREIGN KEY (`userId`) REFERENCES `userinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reply
-- ----------------------------

-- ----------------------------
-- Table structure for school
-- ----------------------------
DROP TABLE IF EXISTS `school`;
CREATE TABLE `school` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL COMMENT '学院名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of school
-- ----------------------------
INSERT INTO `school` VALUES ('1', '数学与计算机学院');
INSERT INTO `school` VALUES ('2', '外国语学院');
INSERT INTO `school` VALUES ('3', '人文社科学院');
INSERT INTO `school` VALUES ('4', '土木与建筑工程学院');
INSERT INTO `school` VALUES ('5', '艺术学院');
INSERT INTO `school` VALUES ('6', '钒钛学院');
INSERT INTO `school` VALUES ('7', '经济与管理学院');

-- ----------------------------
-- Table structure for sort
-- ----------------------------
DROP TABLE IF EXISTS `sort`;
CREATE TABLE `sort` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sort
-- ----------------------------
INSERT INTO `sort` VALUES ('1', 'C语言');
INSERT INTO `sort` VALUES ('2', 'Java');
INSERT INTO `sort` VALUES ('4', 'JavaScript');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱，用户ID',
  `qq` int(13) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '用户状态0：未激活，1：激活',
  `identify` int(1) NOT NULL DEFAULT '0' COMMENT '1为管理员。0为普通成员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1963559122@qq.com', null, '3944b28dbc617f97ce0d5a01d0ad7093', '0', '0');
INSERT INTO `user` VALUES ('impassive@aliyun.com', null, 'b0cce022c9c173b67ef8b684d13a8dbc', '0', '1');
INSERT INTO `user` VALUES ('yangbin51@outlook.com', null, '656aeecfa0877e56d7168c8b22cd35db', '0', '0');

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `id` varchar(50) NOT NULL,
  `nickName` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `school` int(2) DEFAULT NULL COMMENT '学院',
  `major` int(2) DEFAULT NULL COMMENT '专业',
  `avatar` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `phone` varchar(14) DEFAULT NULL COMMENT '电话',
  `description` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '个人简介',
  PRIMARY KEY (`id`),
  KEY `userinfo_school_id` (`school`) USING BTREE,
  KEY `userinfo_major_id` (`major`) USING BTREE,
  CONSTRAINT `userinfo_major_id` FOREIGN KEY (`major`) REFERENCES `major` (`id`),
  CONSTRAINT `userinfo_school_id` FOREIGN KEY (`school`) REFERENCES `school` (`id`),
  CONSTRAINT `userinfo_user_id` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('1963559122@qq.com', '杨彬', '1', '1', '1963559122@qq.com-Avatar.png', '13183719338', '');
INSERT INTO `userinfo` VALUES ('impassive@aliyun.com', '杨廷发', '1', '1', 'impassive@aliyun.com-Avatar.png', '18180303132', '啊萨达萨达撒的');
INSERT INTO `userinfo` VALUES ('yangbin51@outlook.com', '罗文', '1', '1', 'yangbin51@outlook.com-Avatar.png', '13183719338', '');
DROP TRIGGER IF EXISTS `after_user_insert`;
DELIMITER ;;
CREATE TRIGGER `after_user_insert` AFTER INSERT ON `user` FOR EACH ROW INSERT into userinfo SET id = (
    select a.id from 
    (
        select id from user a where NOT EXISTS
        (
            select id from userinfo b where a.id=b.id 
        )
        
    ) a)
;;
DELIMITER ;
