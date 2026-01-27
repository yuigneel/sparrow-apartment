/*
 Navicat Premium Data Transfer
 精简版：保留完整表结构，仅保留核心测试数据（每个表10条内）
 调整：create_time/update_time 字段设置自动生成/自动更新
*/
-- ----------------------------
-- 数据库初始化
-- ----------------------------
-- 如果有的话删除数据库
DROP DATABASE IF EXISTS sparrow_rental;
-- 创建数据库（指定字符集和排序规则，避免中文乱码）
CREATE DATABASE sparrow_rental
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE sparrow_rental;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apartment_facility（公寓&配套关联表）
-- ----------------------------
DROP TABLE IF EXISTS `apartment_facility`;
CREATE TABLE `apartment_facility`  (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `apartment_id` bigint NULL DEFAULT NULL COMMENT '公寓id',
                                       `facility_id` bigint NULL DEFAULT NULL COMMENT '设施id',
                                       `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除', -- 明确NOT NULL + 默认0
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 231 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公寓&配套关联表' ROW_FORMAT = DYNAMIC;

INSERT INTO `apartment_facility` VALUES (1, 1, 1, '2023-06-19 23:20:06', '2023-06-19 23:20:06', 0);
INSERT INTO `apartment_facility` VALUES (2, 1, 2, '2023-06-19 23:20:06', '2023-06-19 23:20:06', 0);
INSERT INTO `apartment_facility` VALUES (3, 1, 3, '2023-06-19 23:20:06', '2023-06-19 23:20:06', 0);
INSERT INTO `apartment_facility` VALUES (4, 2, 1, '2023-06-19 23:21:42', '2023-06-19 23:21:42', 0);
INSERT INTO `apartment_facility` VALUES (5, 2, 4, '2023-06-19 23:21:42', '2023-06-19 23:21:42', 0);

-- ----------------------------
-- Table structure for apartment_fee_value（公寓&杂费关联表）
-- ----------------------------
DROP TABLE IF EXISTS `apartment_fee_value`;
CREATE TABLE `apartment_fee_value`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `apartment_id` bigint NULL DEFAULT NULL COMMENT '公寓id',
                                        `fee_value_id` bigint NULL DEFAULT NULL COMMENT '收费项value_id',
                                        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公寓&杂费关联表' ROW_FORMAT = DYNAMIC;

INSERT INTO `apartment_fee_value` VALUES (1, 1, 1, '2023-06-20 10:15:43', '2023-06-20 10:15:43', 0);
INSERT INTO `apartment_fee_value` VALUES (2, 1, 4, '2023-06-20 10:15:43', '2023-06-20 10:15:43', 0);
INSERT INTO `apartment_fee_value` VALUES (3, 2, 2, '2023-06-20 10:17:44', '2023-06-20 10:17:44', 0);
INSERT INTO `apartment_fee_value` VALUES (4, 2, 7, '2023-06-20 10:17:44', '2023-06-20 10:17:44', 0);

-- ----------------------------
-- Table structure for apartment_info（公寓信息表）
-- ----------------------------
DROP TABLE IF EXISTS `apartment_info`;
CREATE TABLE `apartment_info`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公寓id',
                                   `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公寓名称',
                                   `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公寓介绍',
                                   `district_id` bigint NULL DEFAULT NULL COMMENT '所处区域id',
                                   `district_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
                                   `city_id` bigint NULL DEFAULT NULL COMMENT '所处城市id',
                                   `city_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市名称',
                                   `province_id` bigint NULL DEFAULT NULL COMMENT '所处省份id',
                                   `province_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份名称',
                                   `address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
                                   `latitude` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
                                   `longitude` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
                                   `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公寓前台电话',
                                   `is_release` tinyint NULL DEFAULT NULL COMMENT '是否发布（1:发布，0:未发布）',
                                   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公寓信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `apartment_info` VALUES (1, '温都水城社区', '现代化公寓，近地铁，配套健身房/停车位', 110114, '昌平区', 1101, '市辖区', 11, '北京市', '北京市昌平区温都水城北七家镇王府街55号', '40.103976', '116.370825', '1234567788', 1, '2023-06-20 09:13:52', '2023-08-19 15:44:26', 0);
INSERT INTO `apartment_info` VALUES (2, '朝阳公园公寓', '轻奢公寓，朝南户型，独立卫浴，近公交', 110105, '朝阳区', 1101, '市辖区', 11, '北京市', '北京市朝阳区朝阳公园路19号', '40.081628', '116.363725', '1234567890', 1, '2023-06-21 10:17:59', '2023-08-19 15:44:50', 0);

-- ----------------------------
-- Table structure for apartment_label（公寓标签关联表）
-- ----------------------------
DROP TABLE IF EXISTS `apartment_label`;
CREATE TABLE `apartment_label`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `apartment_id` bigint NULL DEFAULT NULL COMMENT '公寓id',
                                    `label_id` bigint NULL DEFAULT NULL COMMENT '标签id',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公寓标签关联表' ROW_FORMAT = DYNAMIC;

INSERT INTO `apartment_label` VALUES (1, 1, 1, '2023-06-19 23:20:06', '2023-06-19 23:20:06', 0);
INSERT INTO `apartment_label` VALUES (2, 1, 3, '2023-06-19 23:20:06', '2023-06-19 23:20:06', 0);
INSERT INTO `apartment_label` VALUES (3, 2, 1, '2023-06-19 23:21:42', '2023-06-19 23:21:42', 0);
INSERT INTO `apartment_label` VALUES (4, 2, 2, '2023-06-19 23:21:42', '2023-06-19 23:21:42', 0);

-- ----------------------------
-- Table structure for attr_key（房间基本属性表）
-- ----------------------------
DROP TABLE IF EXISTS `attr_key`;
CREATE TABLE `attr_key`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性key',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间基本属性表' ROW_FORMAT = DYNAMIC;

INSERT INTO `attr_key` VALUES (1, '面积', '2023-06-19 01:43:37', '2023-06-19 02:20:01', 0);
INSERT INTO `attr_key` VALUES (2, '朝向', '2023-06-19 02:06:12', '2023-06-21 10:10:57', 0);
INSERT INTO `attr_key` VALUES (3, '户型', '2023-06-19 02:20:53', '2023-06-19 02:31:14', 0);
INSERT INTO `attr_key` VALUES (4, '卫所', '2023-08-10 18:47:36', '2023-08-14 00:11:57', 0);

-- ----------------------------
-- Table structure for attr_value（房间基本属性值表）
-- ----------------------------
DROP TABLE IF EXISTS `attr_value`;
CREATE TABLE `attr_value`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性value',
                               `attr_key_id` bigint NULL DEFAULT NULL COMMENT '对应的属性key_id',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                               `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间基本属性值表' ROW_FORMAT = DYNAMIC;

INSERT INTO `attr_value` VALUES (1, '25平', 1, '2023-06-19 01:44:23', '2023-06-19 01:44:23', 0);
INSERT INTO `attr_value` VALUES (2, '35平', 1, '2023-07-22 11:59:04', '2023-07-22 11:59:04', 0);
INSERT INTO `attr_value` VALUES (3, '朝南', 2, '2023-08-10 15:22:20', '2023-08-10 15:22:20', 0);
INSERT INTO `attr_value` VALUES (4, '朝北', 2, '2023-08-10 15:22:34', '2023-08-10 15:22:34', 0);
INSERT INTO `attr_value` VALUES (5, '一室一厅', 3, '2023-06-21 10:09:50', '2023-06-21 10:09:50', 0);
INSERT INTO `attr_value` VALUES (6, '两室一厅', 3, '2023-06-21 10:09:56', '2023-06-21 10:09:56', 0);
INSERT INTO `attr_value` VALUES (7, '独卫', 4, '2023-08-10 18:47:46', '2023-08-10 18:47:46', 0);
INSERT INTO `attr_value` VALUES (8, '公共', 4, '2023-08-10 18:47:51', '2023-08-10 18:47:51', 0);

-- ----------------------------
-- Table structure for browsing_history（浏览历史）
-- ----------------------------
DROP TABLE IF EXISTS `browsing_history`;
CREATE TABLE `browsing_history`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
                                     `room_id` bigint NULL DEFAULT NULL COMMENT '浏览房间id',
                                     `browse_time` timestamp NULL DEFAULT NULL,
                                     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '浏览历史' ROW_FORMAT = DYNAMIC;

INSERT INTO `browsing_history` VALUES (1, 1, 1, '2023-08-11 10:45:48', '2023-08-11 10:35:31', '2023-08-11 10:45:48', 0);
INSERT INTO `browsing_history` VALUES (2, 1, 2, '2023-08-11 10:45:47', '2023-08-11 10:35:32', '2023-08-11 10:45:47', 0);
INSERT INTO `browsing_history` VALUES (3, 2, 1, '2023-08-19 15:51:22', '2023-08-13 23:22:38', '2023-08-19 15:51:22', 0);

-- ----------------------------
-- Table structure for city_info（城市表）
-- ----------------------------
DROP TABLE IF EXISTS `city_info`;
CREATE TABLE `city_info`  (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT '城市id',
                              `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市名称',
                              `province_id` int NULL DEFAULT NULL COMMENT '所属省份id',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6591 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

INSERT INTO `city_info` VALUES (1101, '市辖区', 11, '2023-06-25 13:48:30', '2023-06-25 13:48:30', 0);
INSERT INTO `city_info` VALUES (3101, '市辖区', 31, '2023-06-25 13:48:30', '2023-06-25 13:48:30', 0);

-- ----------------------------
-- Table structure for district_info（区域表）
-- ----------------------------
DROP TABLE IF EXISTS `district_info`;
CREATE TABLE `district_info`  (
                                  `id` int NOT NULL AUTO_INCREMENT COMMENT '区域id',
                                  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
                                  `city_id` int NULL DEFAULT NULL COMMENT '所属城市id',
                                  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 659012 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

INSERT INTO `district_info` VALUES (110105, '朝阳区', 1101, '2023-06-25 13:48:34', '2023-06-25 13:48:34', 0);
INSERT INTO `district_info` VALUES (110114, '昌平区', 1101, '2023-06-25 13:48:34', '2023-06-25 13:48:34', 0);

-- ----------------------------
-- Table structure for facility_info（配套信息表）
-- ----------------------------
DROP TABLE IF EXISTS `facility_info`;
CREATE TABLE `facility_info`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                  `type` tinyint NULL DEFAULT NULL COMMENT '类型（1:公寓图片,2:房间图片）',
                                  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '配套信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `facility_info` VALUES (1, 1, '健身房', '健身房', '2023-06-17 06:21:24', '2023-06-17 06:21:24', 0);
INSERT INTO `facility_info` VALUES (2, 1, '停车位', '停车场', '2023-06-17 06:21:42', '2023-06-17 06:21:42', 0);
INSERT INTO `facility_info` VALUES (3, 1, '电梯', '电梯', '2023-06-17 06:21:47', '2023-06-17 06:21:47', 0);
INSERT INTO `facility_info` VALUES (4, 2, '空调', '空调', '2023-06-17 06:22:06', '2023-06-17 06:22:06', 0);
INSERT INTO `facility_info` VALUES (5, 2, '洗衣机', '洗衣机', '2023-06-17 06:22:11', '2023-06-17 06:22:11', 0);
INSERT INTO `facility_info` VALUES (6, 2, '冰箱', '冰箱', '2023-06-17 06:22:15', '2023-06-17 06:22:15', 0);

-- ----------------------------
-- Table structure for fee_key（杂项费用名称表）
-- ----------------------------
DROP TABLE IF EXISTS `fee_key`;
CREATE TABLE `fee_key`  (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款项key',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '杂项费用名称表' ROW_FORMAT = DYNAMIC;

INSERT INTO `fee_key` VALUES (1, '停车费', '2023-06-19 03:03:55', '2023-06-19 03:03:55', 0);
INSERT INTO `fee_key` VALUES (2, '网费', '2023-06-19 03:06:49', '2023-06-19 03:06:49', 0);
INSERT INTO `fee_key` VALUES (3, '电费', '2023-08-10 18:45:12', '2023-08-10 18:45:12', 0);

-- ----------------------------
-- Table structure for fee_value（杂项费用值表）
-- ----------------------------
DROP TABLE IF EXISTS `fee_value`;
CREATE TABLE `fee_value`  (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用value',
                              `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收费单位',
                              `fee_key_id` bigint NULL DEFAULT NULL COMMENT '费用所对的fee_key',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '杂项费用值表' ROW_FORMAT = DYNAMIC;

INSERT INTO `fee_value` VALUES (1, '200', '元/月', 1, '2023-06-19 03:04:35', '2023-06-19 03:04:35', 0);
INSERT INTO `fee_value` VALUES (2, '300', '元/月', 1, '2023-06-19 03:04:40', '2023-06-19 03:04:40', 0);
INSERT INTO `fee_value` VALUES (3, '50', '元/月', 2, '2023-06-19 03:07:00', '2023-06-19 03:07:00', 0);
INSERT INTO `fee_value` VALUES (4, '60', '元/月', 2, '2023-06-19 03:07:07', '2023-06-19 03:07:07', 0);
INSERT INTO `fee_value` VALUES (5, '1.5', '元/度', 3, '2023-08-10 18:49:01', '2023-08-10 18:49:01', 0);
INSERT INTO `fee_value` VALUES (6, '1', '元/度', 3, '2023-08-10 18:49:34', '2023-08-10 18:49:34', 0);

-- ----------------------------
-- Table structure for graph_info（图片信息表）
-- ----------------------------
DROP TABLE IF EXISTS `graph_info`;
CREATE TABLE `graph_info`  (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图片id',
                               `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片名称',
                               `item_type` tinyint NULL DEFAULT NULL COMMENT '图片所属对象类型（1:apartment,2:room）',
                               `item_id` bigint NULL DEFAULT NULL COMMENT '图片所有对象id',
                               `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 172 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '图片信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `graph_info` VALUES (1, '公寓-健身房.jpg', 1, 1, 'http://demo/lease/apartment_gym.jpg', '2023-08-19 15:44:26', '2023-08-19 15:44:26', 0);
INSERT INTO `graph_info` VALUES (2, '公寓-外观.jpg', 1, 2, 'http://demo/lease/apartment_outside.jpg', '2023-08-19 15:44:50', '2023-08-19 15:44:50', 0);
INSERT INTO `graph_info` VALUES (3, '房间-卧室.jpg', 2, 1, 'http://demo/lease/room_bed.jpg', '2023-08-19 15:46:10', '2023-08-19 15:46:10', 0);
INSERT INTO `graph_info` VALUES (4, '房间-客厅.jpg', 2, 2, 'http://demo/lease/room_living.jpg', '2023-08-19 15:46:32', '2023-08-19 15:46:32', 0);

-- ----------------------------
-- Table structure for label_info（标签信息表）
-- ----------------------------
DROP TABLE IF EXISTS `label_info`;
CREATE TABLE `label_info`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `type` tinyint NULL DEFAULT NULL COMMENT '类型（1:公寓标签,2:房间标签）',
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `label_info` VALUES (1, 1, '近地铁', '2023-06-19 00:49:17', '2023-06-19 00:49:17', 0);
INSERT INTO `label_info` VALUES (2, 1, '近公交', '2023-06-19 00:49:23', '2023-06-19 00:49:23', 0);
INSERT INTO `label_info` VALUES (3, 1, '有电梯', '2023-06-19 00:49:28', '2023-06-19 00:49:28', 0);
INSERT INTO `label_info` VALUES (4, 2, '朝南', '2023-06-19 00:50:24', '2023-06-19 00:50:24', 0);
INSERT INTO `label_info` VALUES (5, 2, '独卫', '2023-08-11 08:40:51', '2023-08-11 08:40:51', 0);

-- ----------------------------
-- Table structure for lease_agreement（租约信息表）
-- ----------------------------
DROP TABLE IF EXISTS `lease_agreement`;
CREATE TABLE `lease_agreement`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '租约id',
                                    `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '承租人手机号码',
                                    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '承租人姓名',
                                    `identification_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '承租人身份证号码',
                                    `apartment_id` bigint NULL DEFAULT NULL COMMENT '签约公寓id',
                                    `room_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '签约房间id',
                                    `lease_start_date` date NULL DEFAULT NULL COMMENT '租约开始日期',
                                    `lease_end_date` date NULL DEFAULT NULL COMMENT '租约结束日期',
                                    `lease_term_id` bigint NULL DEFAULT NULL COMMENT '租期id',
                                    `rent` decimal(16, 2) NULL DEFAULT NULL COMMENT '租金（元/月）',
                                    `deposit` decimal(16, 2) NULL DEFAULT NULL COMMENT '押金（元）',
                                    `payment_type_id` bigint NULL DEFAULT NULL COMMENT '支付类型id',
                                    `status` tinyint NULL DEFAULT NULL COMMENT '租约状态（1:签约待确认，2:已签约，3:已取消，4:已到期，5:退租待确认，6:已退租，7:续约待确认）',
                                    `source_type` tinyint NULL DEFAULT NULL COMMENT '租约来源（1:新签，2:续约）',
                                    `additional_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租约信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `lease_agreement` VALUES (1, '13888888888', '张三', '110101199001011234', 1, 1, '2023-01-01', '2023-12-31', 3, 2500.00, 2500.00, 1, 2, 1, '无', '2023-06-21 15:36:08', '2023-08-14 15:03:03', 0);
INSERT INTO `lease_agreement` VALUES (2, '13666666666', '李四', '110105199505056789', 2, 2, '2023-02-01', '2023-08-31', 2, 3000.00, 3000.00, 2, 2, 1, '无', '2023-06-21 15:36:08', '2023-08-14 15:03:03', 0);

-- ----------------------------
-- Table structure for lease_term（租期）
-- ----------------------------
DROP TABLE IF EXISTS `lease_term`;
CREATE TABLE `lease_term`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `month_count` int NULL DEFAULT NULL COMMENT '租期',
                               `unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租期单位',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租期' ROW_FORMAT = DYNAMIC;

INSERT INTO `lease_term` VALUES (1, 3, '月', '2023-06-30 10:58:17', '2023-06-30 10:58:17', 0);
INSERT INTO `lease_term` VALUES (2, 6, '月', '2023-06-30 10:58:21', '2023-06-30 10:58:21', 0);
INSERT INTO `lease_term` VALUES (3, 12, '月', '2023-08-01 18:01:20', '2023-08-01 18:01:20', 0);

-- ----------------------------
-- Table structure for payment_type（支付方式表）
-- ----------------------------
DROP TABLE IF EXISTS `payment_type`;
CREATE TABLE `payment_type`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款方式名称',
                                 `pay_month_count` int NULL DEFAULT NULL COMMENT '每次支付租期数',
                                 `additional_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付费说明',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除', -- 修正：原默认值为NULL，改为0且NOT NULL
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付方式表' ROW_FORMAT = DYNAMIC;

INSERT INTO `payment_type` VALUES (1, '月付', 1, '押一付一', '2023-06-21 11:26:08', '2023-06-21 11:26:08', 0);
INSERT INTO `payment_type` VALUES (2, '季付', 3, '押一付三', '2023-06-21 11:26:21', '2023-06-21 11:26:21', 0);
INSERT INTO `payment_type` VALUES (3, '半年付', 6, '押一付六', '2023-06-21 11:26:35', '2023-06-21 11:26:35', 0);

-- ----------------------------
-- Table structure for province_info（省份表）
-- ----------------------------
DROP TABLE IF EXISTS `province_info`;
CREATE TABLE `province_info`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '省份id',
                                  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份名称',
                                  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

INSERT INTO `province_info` VALUES (11, '北京市', '2023-06-25 13:48:39', '2023-06-25 13:48:39', 0);
INSERT INTO `province_info` VALUES (31, '上海市', '2023-06-25 13:48:39', '2023-06-25 13:48:39', 0);

-- ----------------------------
-- Table structure for room_attr_value（房间&基本属性值关联表）
-- ----------------------------
DROP TABLE IF EXISTS `room_attr_value`;
CREATE TABLE `room_attr_value`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `room_id` bigint NULL DEFAULT NULL COMMENT '房间id',
                                    `attr_value_id` bigint NULL DEFAULT NULL COMMENT '属性值id',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 217 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间&基本属性值关联表' ROW_FORMAT = DYNAMIC;

INSERT INTO `room_attr_value` VALUES (1, 1, 1, '2023-06-20 19:14:46', '2023-06-20 19:14:46', 0);
INSERT INTO `room_attr_value` VALUES (2, 1, 3, '2023-06-20 19:14:46', '2023-06-20 19:14:46', 0);
INSERT INTO `room_attr_value` VALUES (3, 1, 5, '2023-06-20 19:14:46', '2023-06-20 19:14:46', 0);
INSERT INTO `room_attr_value` VALUES (4, 2, 2, '2023-06-21 10:35:16', '2023-06-21 10:35:16', 0);
INSERT INTO `room_attr_value` VALUES (5, 2, 4, '2023-06-21 10:35:16', '2023-06-21 10:35:16', 0);

-- ----------------------------
-- Table structure for room_facility（房间&配套关联表）
-- ----------------------------
DROP TABLE IF EXISTS `room_facility`;
CREATE TABLE `room_facility`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `room_id` bigint NULL DEFAULT NULL COMMENT '房间id',
                                  `facility_id` bigint NULL DEFAULT NULL COMMENT '房间设施id',
                                  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 560 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间&配套关联表' ROW_FORMAT = DYNAMIC;

INSERT INTO `room_facility` VALUES (1, 1, 4, '2023-06-20 19:14:46', '2023-06-20 19:14:46', 0);
INSERT INTO `room_facility` VALUES (2, 1, 5, '2023-06-20 19:14:46', '2023-06-20 19:14:46', 0);
INSERT INTO `room_facility` VALUES (3, 2, 4, '2023-06-21 10:35:16', '2023-06-21 10:35:16', 0);
INSERT INTO `room_facility` VALUES (4, 2, 6, '2023-06-21 10:35:16', '2023-06-21 10:35:16', 0);

-- ----------------------------
-- Table structure for room_info（房间信息表）
-- ----------------------------
DROP TABLE IF EXISTS `room_info`;
CREATE TABLE `room_info`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '房间id',
                              `room_number` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房间号',
                              `rent` decimal(16, 2) NULL DEFAULT NULL COMMENT '租金（元/月）',
                              `apartment_id` bigint NULL DEFAULT NULL COMMENT '所属公寓id',
                              `is_release` tinyint NULL DEFAULT NULL COMMENT '是否发布',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `room_info` VALUES (1, '101', 2500.00, 1, 1, '2023-06-20 19:14:46', '2023-08-19 15:46:10', 0);
INSERT INTO `room_info` VALUES (2, '102', 3000.00, 2, 1, '2023-06-21 10:35:16', '2023-08-19 15:46:32', 0);
INSERT INTO `room_info` VALUES (3, '103', 2800.00, 1, 1, '2023-08-13 23:39:37', '2023-08-19 15:46:49', 0);
INSERT INTO `room_info` VALUES (4, '104', 3200.00, 2, 1, '2023-08-13 23:40:50', '2023-08-19 15:47:08', 0);

-- ----------------------------
-- Table structure for room_label（房间&标签关联表）
-- ----------------------------
DROP TABLE IF EXISTS `room_label`;
CREATE TABLE `room_label`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `room_id` bigint NULL DEFAULT NULL COMMENT '房间id',
                               `label_id` bigint NULL DEFAULT NULL COMMENT '标签id',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间&标签关联表' ROW_FORMAT = DYNAMIC;

INSERT INTO `room_label` VALUES (1, 1, 4, '2023-08-11 08:41:28', '2023-08-11 08:41:28', 0);
INSERT INTO `room_label` VALUES (2, 1, 5, '2023-08-11 08:41:28', '2023-08-11 08:41:28', 0);
INSERT INTO `room_label` VALUES (3, 2, 4, '2023-08-11 08:41:35', '2023-08-11 08:41:35', 0);
INSERT INTO `room_label` VALUES (4, 3, 5, '2023-08-13 23:39:37', '2023-08-13 23:39:37', 0);

-- ----------------------------
-- Table structure for room_lease_term（房间租期管理表）
-- ----------------------------
DROP TABLE IF EXISTS `room_lease_term`;
CREATE TABLE `room_lease_term`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `room_id` bigint NULL DEFAULT NULL COMMENT '房间id',
                                    `lease_term_id` bigint NULL DEFAULT NULL COMMENT '租期id',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 186 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间租期管理表' ROW_FORMAT = DYNAMIC;

INSERT INTO `room_lease_term` VALUES (1, 1, 1, '2023-07-18 16:07:09', '2023-07-18 16:07:09', 0);
INSERT INTO `room_lease_term` VALUES (2, 2, 2, '2023-07-18 16:07:09', '2023-07-18 16:07:09', 0);

-- ----------------------------
-- Table structure for room_payment_type（房间&支付方式关联表）
-- ----------------------------
DROP TABLE IF EXISTS `room_payment_type`;
CREATE TABLE `room_payment_type`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `room_id` bigint NULL DEFAULT NULL COMMENT '房间id',
                                      `payment_type_id` bigint NULL DEFAULT NULL COMMENT '支付类型id',
                                      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '房间&支付方式关联表' ROW_FORMAT = DYNAMIC;

INSERT INTO `room_payment_type` VALUES (1, 1, 1, '2023-08-19 15:49:38', '2023-08-19 15:49:38', 0);
INSERT INTO `room_payment_type` VALUES (2, 2, 2, '2023-08-19 15:49:38', '2023-08-19 15:49:38', 0);

-- ----------------------------
-- Table structure for system_post（岗位信息表）
-- ----------------------------
DROP TABLE IF EXISTS `system_post`;
CREATE TABLE `system_post`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
                                `code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '岗位编码',
                                `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '岗位名称',
                                `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '描述',
                                `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（1正常 0停用）',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `system_post` VALUES (1, 'admin', '管理员', '系统超级管理员', 1, '2023-08-10 09:01:56', '2023-08-10 17:01:57', 0);
INSERT INTO `system_post` VALUES (2, 'regional_manager', '区域经理', '负责区域公寓管理', 1, '2023-08-10 09:01:56', '2023-08-10 17:01:57', 0);
INSERT INTO `system_post` VALUES (3, 'apartment_manager', '店长', '公寓日常运营管理', 1, '2023-08-10 09:03:55', '2023-08-10 17:03:56', 0);

-- ----------------------------
-- Table structure for system_user（员工信息表）
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '员工id',
                                `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码（md5: 123456）',
                                `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
                                `type` tinyint NULL DEFAULT NULL COMMENT '用户类型',
                                `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
                                `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
                                `additional_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                                `post_id` bigint NULL DEFAULT NULL COMMENT '岗位id',
                                `status` tinyint NULL DEFAULT NULL COMMENT '账号状态',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `system_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 0, '18888888888', NULL, NULL, 1, 1, '2023-08-10 17:13:00', '2023-08-19 23:30:48', 0);
INSERT INTO `system_user` VALUES (2, 'shop_manager', 'e10adc3949ba59abbe56e057f20f883e', '张三店长', 1, '13666666666', NULL, NULL, 3, 1, '2023-08-19 16:53:53', '2023-08-19 16:53:53', 0);

-- ----------------------------
-- Table structure for user_info（用户信息表）
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                              `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码（用做登录用户名）',
                              `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
                              `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像url',
                              `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
                              `status` tinyint NULL DEFAULT 1 COMMENT '账号状态',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `user_info` VALUES (1, '13888888888', 'e10adc3949ba59abbe56e057f20f883e', NULL, '张三', 1, '2023-07-01 14:48:17', '2023-07-01 14:48:17', 0);
INSERT INTO `user_info` VALUES (2, '13666666666', 'e10adc3949ba59abbe56e057f20f883e', NULL, '李四', 1, '2023-07-01 14:48:17', '2023-07-01 14:48:17', 0);

-- ----------------------------
-- Table structure for view_appointment（预约看房信息表）
-- ----------------------------
DROP TABLE IF EXISTS `view_appointment`;
CREATE TABLE `view_appointment`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预约id',
                                     `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
                                     `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
                                     `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机号码',
                                     `apartment_id` int NULL DEFAULT NULL COMMENT '公寓id',
                                     `appointment_time` timestamp NULL DEFAULT NULL COMMENT '预约时间',
                                     `additional_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                                     `appointment_status` tinyint NULL DEFAULT NULL COMMENT '预约状态（1:待看房，2:已取消，3已看房）',
                                     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预约看房信息表' ROW_FORMAT = DYNAMIC;

INSERT INTO `view_appointment` VALUES (1, 1, '张三', '13888888888', 1, '2023-08-20 09:00:00', '无', 1, '2023-08-14 12:44:50', '2023-08-14 15:10:04', 0);
INSERT INTO `view_appointment` VALUES (2, 2, '李四', '13666666666', 2, '2023-08-21 10:00:00', '下午看房', 1, '2023-08-14 12:45:51', '2023-08-14 14:49:58', 0);

SET FOREIGN_KEY_CHECKS = 1;