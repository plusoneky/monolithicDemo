/*
Navicat MySQL Data Transfer

Source Server         : 39.108.61.183_testdb
Source Server Version : 50717
Source Host           : 39.108.61.183:3306
Source Database       : firmware_version_db

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-04-17 09:03:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for firmware_version
-- ----------------------------
DROP TABLE IF EXISTS `firmware_version`;
CREATE TABLE `firmware_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '设备类型',
  `unzip_pwd` varchar(255) NOT NULL COMMENT '解压密码（明文密码）',
  `new_ver` varchar(255) NOT NULL COMMENT '新固件版本',
  `firm_file_path` varchar(800) DEFAULT NULL COMMENT '固件安装文件路径(相对路径）',
  `md5` varchar(255) DEFAULT NULL COMMENT '安装文件的MD5编码',
  `status` int(2) DEFAULT '1' COMMENT '状态（0 未投用  1已投用  2已删除）',
  `create_operator` varchar(255) DEFAULT NULL COMMENT '创建操作员账号（历史）',
  `create_operator_id` bigint(255) DEFAULT NULL COMMENT '创建操作员账号（历史）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_operator` varchar(255) DEFAULT NULL COMMENT '修改操作员账号（历史）',
  `modify_operator_id` bigint(255) DEFAULT NULL COMMENT '修改操作员账号',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_unique_index` (`type_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of firmware_version
-- ----------------------------
INSERT INTO `firmware_version` VALUES ('11', 'mb2', '123abc', 'v1.2.0', 'http://uf.tzzhcom.com/updown/apk/2018-03/13/f13c830f285f4d4e81962ea3e3fad27d-mb1_ota_v1.1.0_20180303_encry.zip', 'f13d5767ba1ea309595165774b62ac7b', '1', 'admin', '2', '2018-03-13 23:23:59', 'admin', '2', '2018-03-13 23:23:59');
INSERT INTO `firmware_version` VALUES ('26', 'mb1', 'unencrypt', 'v1.1.3', 'http://uf.tzzhcom.com/updown/apk/2018-04/07/7f81af9fd13d4ef0893a228da87140e5-mb1_ota_v1.1.3_testing.zip', 'b09b650659d7576b831ad91a74341189', '1', 'admin', '2', '2018-04-07 22:00:16', 'admin', '2', '2018-04-07 22:00:16');

-- ----------------------------
-- Table structure for frimware_update_log
-- ----------------------------
DROP TABLE IF EXISTS `frimware_update_log`;
CREATE TABLE `frimware_update_log` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT,
  `sn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '设备序列号',
  `type_id` bigint(32) NOT NULL,
  `type_name` varchar(255) NOT NULL COMMENT '设备类型（冗余）',
  `mac` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '设备MAC地址',
  `status` varchar(255) DEFAULT NULL COMMENT '设备状态（online   offline）',
  `update_time` datetime DEFAULT NULL COMMENT '最近一次上送更新结果的时间',
  `ver` varchar(255) NOT NULL DEFAULT '' COMMENT '当前固件版本',
  `update_result_report` varchar(255) DEFAULT NULL COMMENT '更新结果报告（initial success fail）',
  `update_log` varchar(2000) DEFAULT NULL COMMENT '更新日志',
  `import_operator` varchar(255) DEFAULT NULL COMMENT '导入操作员账号（历史）',
  `import_operator_id` bigint(20) DEFAULT NULL COMMENT '导入操作员账号',
  `import_time` datetime DEFAULT NULL COMMENT '导入时间',
  `flag` varchar(255) NOT NULL COMMENT '是否有允许更新标识，默认为no（yes  no）',
  `get_new_ver_time` datetime DEFAULT NULL COMMENT '最近一次成功拉取新版本的时间',
  `first_login_time` datetime DEFAULT NULL COMMENT '首次登入时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn_unique_index` (`sn`) USING BTREE,
  UNIQUE KEY `sn_unique_mac` (`mac`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=940 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` bigint(20) DEFAULT NULL COMMENT '上级ID',
  `name` varchar(50) DEFAULT NULL COMMENT '权限名',
  `type` smallint(6) DEFAULT NULL COMMENT '类型 0、菜单 1、功能  2、按钮',
  `sort` smallint(6) DEFAULT NULL COMMENT '排序',
  `url` varchar(100) DEFAULT NULL COMMENT '地址',
  `perm_code` varchar(30) DEFAULT NULL COMMENT '权限编码',
  `icon` varchar(30) DEFAULT NULL COMMENT '图标',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `status` smallint(6) DEFAULT NULL COMMENT '状态 0、禁用 1、正常',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `perm_name_index` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='permission 权限表';

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '0', '系统管理', '0', '100', '-', '-', 'fa fa-tag', '系统管理员目录', '1', '2017-09-12 16:48:53', '2018-01-15 10:58:46');
INSERT INTO `permission` VALUES ('2', '1', '用户管理', '1', '110', '/admin/user/list', 'sys.user:list', 'fa fa-user', '用户管理', '1', '2017-09-12 16:52:28', '2017-09-21 16:57:31');
INSERT INTO `permission` VALUES ('3', '1', '角色管理', '1', '120', '/admin/role/list', 'sys.role:list', 'fa fa-circle', '角色管理', '1', '2017-09-12 16:52:34', '2017-09-21 16:58:33');
INSERT INTO `permission` VALUES ('4', '1', '权限管理', '1', '130', '/admin/permission/list', 'sys.permisssion:list', 'fa fa-shirtsinbulk', '权限管理', '1', '2017-09-12 16:52:40', '2017-09-21 16:58:40');
INSERT INTO `permission` VALUES ('10', '0', '业务管理', '0', '300', '-', '-', 'fa fa-cog', '业务管理', '1', '2017-09-21 09:34:26', '2017-09-21 16:59:01');
INSERT INTO `permission` VALUES ('17', '2', '添加用户', '2', '110', '-', 'sys.user:add', '-', '用户管理-添加用户', '1', '2017-10-12 00:58:21', null);
INSERT INTO `permission` VALUES ('18', '2', '修改用户', '2', '110', '-', 'sys.user:update', '-', '用户管理-修改用户', '1', '2017-10-12 00:58:21', null);
INSERT INTO `permission` VALUES ('19', '2', '分配角色', '2', '110', '-', 'sys.user.role:add', '-', '用户管理-分配角色', '1', '2017-10-12 00:58:21', null);
INSERT INTO `permission` VALUES ('20', '2', '审核通过', '2', '110', '-', 'sys.user:optionUserStatus', '-', '用户管理-审核通过', '1', '2017-10-12 00:58:21', null);
INSERT INTO `permission` VALUES ('21', '2', '删除用户', '2', '110', '-', 'sys.user:delete', '-', '用户管理-删除用户', '1', '2017-10-12 00:58:21', null);
INSERT INTO `permission` VALUES ('23', '10', '固件版本维护', '1', '350', '/biz/f_ver/versionlist', 'biz.version:list', 'glyphicon glyphicon-open', '业务管理-固件版本维护', '1', '2017-11-30 11:22:55', '2017-11-30 11:23:02');
INSERT INTO `permission` VALUES ('24', '10', '终端信息', '1', '355', '/biz/f_log/loglist', 'biz.log:list', 'glyphicon glyphicon-wrench', '业务管理-固件导入升级', '1', null, null);
INSERT INTO `permission` VALUES ('25', '23', '添加版本', '2', '400', '', 'biz.version:add', null, '版本管理-添加版本', '1', null, null);
INSERT INTO `permission` VALUES ('26', '23', '修改版本', '2', '401', '', 'biz.version:update', null, '版本管理-修改版本', '1', null, null);
INSERT INTO `permission` VALUES ('27', '23', '删除版本', '2', '402', '', 'biz.version:delete', null, '版本管理-删除版本', '1', null, null);
INSERT INTO `permission` VALUES ('28', '24', '导入固件', '2', '405', null, 'biz.log:import', null, '固件管理-导入固件', '1', null, null);
INSERT INTO `permission` VALUES ('29', '24', '添加固件', '2', '406', null, 'biz.log:add', null, '固件管理-添加固件', '1', null, null);
INSERT INTO `permission` VALUES ('30', '24', '修改固件', '2', '407', null, 'biz.log:update', null, '固件管理-修改固件', '1', null, null);
INSERT INTO `permission` VALUES ('31', '24', '删除固件', '2', '408', null, 'biz.log:delete', null, '固件管理-删除固件', '1', null, null);
INSERT INTO `permission` VALUES ('40', '24', '批量允许更新', '2', '409', '-', 'biz.log:batchallow', null, '固件管理-批量允许更新', '1', '2018-02-07 16:42:26', '2018-02-07 16:42:30');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) DEFAULT NULL COMMENT '角色名',
  `sort` smallint(6) DEFAULT NULL COMMENT '排序',
  `description` varchar(60) DEFAULT NULL COMMENT '描述',
  `status` smallint(5) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name_index` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='role 角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('2', '超级管理员', '1', '超级管理员', '1', '2017-09-12 17:04:33', '2017-09-12 17:04:35');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rid` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  `pid` bigint(20) unsigned DEFAULT NULL COMMENT '权限ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=422 DEFAULT CHARSET=utf8 COMMENT='role_permission 角色权限表';

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('209', '9', '10', '2017-12-26 18:23:56', null);
INSERT INTO `role_permission` VALUES ('210', '9', '23', '2017-12-26 18:23:56', null);
INSERT INTO `role_permission` VALUES ('211', '9', '25', '2017-12-26 18:23:56', null);
INSERT INTO `role_permission` VALUES ('212', '9', '26', '2017-12-26 18:23:56', null);
INSERT INTO `role_permission` VALUES ('213', '9', '27', '2017-12-26 18:23:56', null);
INSERT INTO `role_permission` VALUES ('371', '2', '33', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('372', '2', '35', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('373', '2', '36', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('374', '2', '37', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('375', '2', '38', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('376', '2', '1', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('377', '2', '2', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('378', '2', '17', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('379', '2', '18', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('380', '2', '19', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('381', '2', '20', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('382', '2', '21', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('383', '2', '3', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('384', '2', '4', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('385', '2', '10', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('386', '2', '23', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('387', '2', '25', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('388', '2', '26', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('389', '2', '27', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('390', '2', '24', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('391', '2', '28', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('392', '2', '29', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('393', '2', '30', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('394', '2', '31', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('395', '2', '40', '2018-02-07 16:43:29', null);
INSERT INTO `role_permission` VALUES ('396', '10', '10', '2018-02-07 16:43:44', null);
INSERT INTO `role_permission` VALUES ('397', '10', '24', '2018-02-07 16:43:44', null);
INSERT INTO `role_permission` VALUES ('398', '10', '28', '2018-02-07 16:43:44', null);
INSERT INTO `role_permission` VALUES ('399', '10', '29', '2018-02-07 16:43:44', null);
INSERT INTO `role_permission` VALUES ('400', '10', '30', '2018-02-07 16:43:44', null);
INSERT INTO `role_permission` VALUES ('401', '10', '31', '2018-02-07 16:43:44', null);
INSERT INTO `role_permission` VALUES ('402', '10', '40', '2018-02-07 16:43:44', null);
INSERT INTO `role_permission` VALUES ('403', '11', '1', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('404', '11', '2', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('405', '11', '17', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('406', '11', '18', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('407', '11', '19', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('408', '11', '20', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('409', '11', '21', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('410', '11', '3', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('411', '11', '10', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('412', '11', '23', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('413', '11', '25', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('414', '11', '26', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('415', '11', '27', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('416', '11', '24', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('417', '11', '28', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('418', '11', '29', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('419', '11', '30', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('420', '11', '31', '2018-02-07 16:43:53', null);
INSERT INTO `role_permission` VALUES ('421', '11', '40', '2018-02-07 16:43:53', null);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `salt` varchar(50) DEFAULT NULL COMMENT '盐',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` smallint(6) DEFAULT NULL COMMENT '0、禁用 1、正常  5、待审核',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_index` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8 COMMENT='user 用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('2', 'admin', '超级用户', '9dd56f503ba62c8d100e4b6486955e21', '1514292542212', 'admin@163.com', '1', null, '2017-12-26 20:49:02');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `rid` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COMMENT='user_role 用户角色关联表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('29', '125', '10', '2017-12-07 15:33:05', null);
INSERT INTO `user_role` VALUES ('35', '119', '9', '2017-12-27 19:06:40', null);
INSERT INTO `user_role` VALUES ('36', '119', '10', '2017-12-27 19:06:40', null);
INSERT INTO `user_role` VALUES ('37', '119', '11', '2017-12-27 19:06:40', null);
INSERT INTO `user_role` VALUES ('40', '2', '2', '2017-12-27 19:08:03', null);
INSERT INTO `user_role` VALUES ('44', '128', '9', '2017-12-29 09:45:55', null);
INSERT INTO `user_role` VALUES ('45', '128', '10', '2017-12-29 09:45:55', null);
INSERT INTO `user_role` VALUES ('46', '128', '11', '2017-12-29 09:45:55', null);
