SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for k_category
-- ----------------------------
DROP TABLE IF EXISTS `k_category`;
CREATE TABLE `k_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `category_name_unique_index`(`category_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_database_type
-- ----------------------------
DROP TABLE IF EXISTS `k_database_type`;
CREATE TABLE `k_database_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '数据库连接类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_database_type
-- ----------------------------
INSERT INTO `k_database_type` VALUES (1, 'DERBY', 'Apache Derby', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (2, 'AS/400', 'AS/400', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (3, 'INTERBASE', 'Borland Interbase', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (4, 'INFINIDB', 'Calpont InfiniDB', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (5, 'IMPALASIMBA', 'Cloudera Impala', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (6, 'DBASE', 'dBase III, IV or 5', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (7, 'EXASOL4', 'Exasol 4', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (8, 'EXTENDB', 'ExtenDB', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (9, 'FIREBIRD', 'Firebird SQL', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (10, 'GENERIC', 'Generic database', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (11, 'GOOGLEBIGQUERY', 'Google BigQuery', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (12, 'GREENPLUM', 'Greenplum', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (13, 'SQLBASE', 'Gupta SQL Base', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (14, 'H2', 'H2', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (15, 'HIVE', 'Hadoop Hive', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (16, 'HIVE2', 'Hadoop Hive 2/3', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (17, 'HYPERSONIC', 'Hypersonic', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (18, 'DB2', 'IBM DB2', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (19, 'IMPALA', 'Impala', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (20, 'INFOBRIGHT', 'Infobright', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (21, 'INFORMIX', 'Informix', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (22, 'INGRES', 'Ingres', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (23, 'VECTORWISE', 'Ingres VectorWise', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (24, 'CACHE', 'Intersystems Cache', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (25, 'KINGBASEES', 'KingbaseES', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (26, 'LucidDB', 'LucidDB', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (27, 'MARIADB', 'MariaDB', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (28, 'SAPDB', 'MaxDB (SAP DB)', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (29, 'MONETDB', 'MonetDB', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (30, 'MSACCESS', 'MS Access', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (31, 'MSSQL', 'MS SQL Server', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (32, 'MSSQLNATIVE', 'MS SQL Server (Native)', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (33, 'MYSQL', 'MySQL', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (34, 'MONDRIAN', 'Native Mondrian', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (35, 'NEOVIEW', 'Neoview', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (36, 'NETEZZA', 'Netezza', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (37, 'ORACLE', 'Oracle', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (38, 'ORACLERDB', 'Oracle RDB', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (39, 'PALO', 'Palo MOLAP Server', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (40, 'KettleThin', 'Pentaho Data Services', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (41, 'POSTGRESQL', 'PostgreSQL', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (42, 'REDSHIFT', 'Redshift', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (43, 'REMEDY-AR-SYSTEM', 'Remedy Action Request System', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (44, 'SAPR3', 'SAP ERP System', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (45, 'SNOWFLAKEHV', 'Snowflake', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (46, 'SPARKSIMBA', 'SparkSQL', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (47, 'SQLITE', 'SQLite', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (48, 'SYBASE', 'Sybase', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (49, 'SYBASEIQ', 'SybaseIQ', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (50, 'TERADATA', 'Teradata', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (51, 'UNIVERSE', 'UniVerse database', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (52, 'VERTICA', 'Vertica', NULL, NULL, NULL, NULL, 0);
INSERT INTO `k_database_type` VALUES (53, 'VERTICA5', 'Vertica 5+', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for k_job
-- ----------------------------
DROP TABLE IF EXISTS `k_job`;
CREATE TABLE `k_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作业ID',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '分类ID（外键ID）',
  `job_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业名称',
  `job_description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `job_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行类型（rep：资源库；file：文件）',
  `job_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）',
  `job_repository_id` int(11) NULL DEFAULT NULL COMMENT '作业的资源库ID',
  `job_quartz` int(11) NULL DEFAULT 1 COMMENT '定时策略（外键ID）',
  `sync_strategy` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步策略(T+n)',
  `job_log_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志级别(Basic，Detailed，Error，Debug，Minimal，Rowlevel）',
  `job_status` int(11) NULL DEFAULT NULL COMMENT '状态（1：正在运行；2：已停止）',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `job_name_unique_index`(`job_name`) USING BTREE COMMENT '作业名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_job_monitor
-- ----------------------------
DROP TABLE IF EXISTS `k_job_monitor`;
CREATE TABLE `k_job_monitor`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '监控作业ID',
  `monitor_job_id` int(11) NULL DEFAULT NULL COMMENT '监控的作业ID',
  `monitor_success` int(11) NULL DEFAULT NULL COMMENT '成功次数',
  `monitor_fail` int(11) NULL DEFAULT NULL COMMENT '失败次数',
  `monitor_status` int(11) NULL DEFAULT NULL COMMENT '监控状态（是否启动，1:启动；2:停止）',
  `run_status` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '运行状态（起始时间-结束时间,起始时间-结束时间……）',
  `last_execute_time` datetime(0) NULL DEFAULT NULL COMMENT '上一次执行时间',
  `next_execute_time` datetime(0) NULL DEFAULT NULL COMMENT '下一次执行时间',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业监控表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_job_record
-- ----------------------------
DROP TABLE IF EXISTS `k_job_record`;
CREATE TABLE `k_job_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作业记录ID',
  `record_job_id` int(11) NULL DEFAULT NULL COMMENT '作业ID',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '启动时间',
  `stop_time` datetime(0) NULL DEFAULT NULL COMMENT '停止时间',
  `record_status` int(11) NULL DEFAULT NULL COMMENT '任务执行结果（1：成功；0：失败）',
  `log_file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业日志记录文件保存位置',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '作业执行记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_quartz
-- ----------------------------
DROP TABLE IF EXISTS `k_quartz`;
CREATE TABLE `k_quartz`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `quartz_description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `quartz_cron` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时策略',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_quartz
-- ----------------------------
INSERT INTO `k_quartz` VALUES (1, '立即执行一次', NULL, '2017-05-27 14:44:13', 1, '2017-05-27 14:44:13', 1, 0);
INSERT INTO `k_quartz` VALUES (2, '每周一0点执行一次', '0 0 0 ? * 2', '2017-05-27 14:56:38', 1, '2017-05-27 14:56:38', 1, 0);
INSERT INTO `k_quartz` VALUES (3, '每月1日0点执行一次', '0 0 0 1 * ?', '2017-05-27 14:56:38', 1, '2017-05-27 14:56:38', 1, 0);
INSERT INTO `k_quartz` VALUES (4, '每日0点执行一次', '0 0 0 * * ?', '2017-05-27 14:44:13', 1, '2017-05-27 14:44:15', 1, 0);

-- ----------------------------
-- Table structure for k_repository
-- ----------------------------
DROP TABLE IF EXISTS `k_repository`;
CREATE TABLE `k_repository`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `rep_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库名称',
  `rep_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库类型: fileRep-文件, dbRep-数据库',
  `rep_username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `rep_password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `rep_base_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件资源库路径: rep_type=fileRep生效',
  `db_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库类型（MYSQL、ORACLE）',
  `db_access` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库访问模式（\"Native\", \"ODBC\", \"OCI\", \"Plugin\", \"JNDI\")',
  `db_host` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库主机名或者IP地址',
  `db_port` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库端口号',
  `db_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库名称',
  `db_username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库登录账号',
  `db_password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库登录密码',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `rep_name_unique_index`(`rep_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_trans
-- ----------------------------
DROP TABLE IF EXISTS `k_trans`;
CREATE TABLE `k_trans`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '转换ID',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '分类ID',
  `trans_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换名称',
  `trans_description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换描述',
  `trans_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行类型（rep：资源库；file：文件）',
  `trans_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）',
  `trans_repository_id` int(11) NULL DEFAULT NULL COMMENT '转换的资源库ID',
  `trans_quartz` int(11) NULL DEFAULT 1 COMMENT '定时策略（外键ID）',
  `sync_strategy` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步策略(T+n)',
  `trans_log_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志级别(Basic，Detailed，Error，Debug，Minimal，Rowlevel）',
  `trans_status` int(11) NULL DEFAULT NULL COMMENT '状态（1：正在运行；2：已停止）',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `trans_name_unique_index`(`trans_name`) USING BTREE COMMENT '转换名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '转换表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_trans_monitor
-- ----------------------------
DROP TABLE IF EXISTS `k_trans_monitor`;
CREATE TABLE `k_trans_monitor`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '监控转换ID',
  `monitor_trans_id` int(11) NULL DEFAULT NULL COMMENT '监控的转换的ID',
  `monitor_success` int(11) NULL DEFAULT NULL COMMENT '成功次数',
  `monitor_fail` int(11) NULL DEFAULT NULL COMMENT '失败次数',
  `monitor_status` int(11) NULL DEFAULT NULL COMMENT '监控状态（是否启动，1:启动；2:停止）',
  `run_status` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '运行状态（起始时间-结束时间,起始时间-结束时间……）',
  `last_execute_time` datetime(0) NULL DEFAULT NULL COMMENT '上一次执行时间',
  `next_execute_time` datetime(0) NULL DEFAULT NULL COMMENT '下一次执行时间',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '转换监控表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_trans_record
-- ----------------------------
DROP TABLE IF EXISTS `k_trans_record`;
CREATE TABLE `k_trans_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '转换记录ID',
  `record_trans_id` int(11) NULL DEFAULT NULL COMMENT '转换ID',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '启动时间',
  `stop_time` datetime(0) NULL DEFAULT NULL COMMENT '停止时间',
  `record_status` int(11) NULL DEFAULT NULL COMMENT '任务执行结果（1：成功；0：失败）',
  `log_file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换日志记录文件保存位置',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '转换执行记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for k_user
-- ----------------------------
DROP TABLE IF EXISTS `k_user`;
CREATE TABLE `k_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用于电话',
  `account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `password` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：存在；1：删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_index_account`(`account`) USING BTREE COMMENT '账号唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_user
-- ----------------------------
INSERT INTO `k_user` VALUES (1, '管理员', NULL, NULL, 'admin', 'f11cc9b89ecbe02bd6a0906101b1e5688aef39b0d42875ad80ae271d0efa7a16', '2019-11-20 20:11:53', 1, '2019-12-06 11:13:38', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
