/*
 Navicat Premium Dump SQL

 Source Server         : 123
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : 47.76.61.206:3306
 Source Schema         : meiweixuan

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 23/05/2025 23:33:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_time` datetime NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sort` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------

-- ----------------------------
-- Table structure for caipin
-- ----------------------------
DROP TABLE IF EXISTS `caipin`;
CREATE TABLE `caipin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜品名称',
  `category_id` int NOT NULL COMMENT '分类ID',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `sales` int NULL DEFAULT 0 COMMENT '销量',
  `is_featured` tinyint NULL DEFAULT 0 COMMENT '是否推荐：1-是，0-否',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-下架',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `caipin_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `caipinfenlei` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of caipin
-- ----------------------------
INSERT INTO `caipin` VALUES (1, '宫保鸡丁', 1, 48.00, '选用优质鸡胸肉，配以花生、辣椒等食材炒制而成，口感麻辣鲜香', '/api/images/dishes/gongbaojiding.jpg', 0, 1, '2025-05-21 18:06:21', NULL, 1);
INSERT INTO `caipin` VALUES (2, '水煮鱼', 2, 58.00, '选用新鲜草鱼，配以豆芽、辣椒等食材煮制而成，口感麻辣鲜香', '/api/images/dishes/shuizhuyu.jpg', 0, 1, '2025-05-21 18:06:21', NULL, 1);
INSERT INTO `caipin` VALUES (3, '北京烤鸭', 1, 128.00, '精选优质鸭胚，采用传统挂炉技法烤制，皮酥肉嫩，搭配薄饼、甜面酱和葱丝食用。', '/api/images/dishes/beijingkaoya.jpg', 0, 1, '2025-05-22 15:47:15', NULL, 1);
INSERT INTO `caipin` VALUES (6, '麻婆豆腐', 2, 28.00, '正宗川味豆腐料理，豆腐嫩滑，麻辣鲜香，配以肉末和特制豆瓣酱，下饭绝配。', '/api/images/dishes/mapodoufu.jpg', 0, 1, '2025-05-22 15:47:15', NULL, 1);
INSERT INTO `caipin` VALUES (7, '红烧肉', 1, 58.00, '精选五花肉，经过焯水、煎制、慢炖等多道工序，肥而不腻，入口即化。', '/api/images/dishes/hongshaorou.jpg', 0, 1, '2025-05-22 15:47:15', NULL, 1);
INSERT INTO `caipin` VALUES (8, '水晶虾饺', 4, 32.00, '广式经典点心，外皮晶莹剔透，内馅选用新鲜虾仁，鲜美多汁，口感Q弹。', '/api/images/dishes/shuijingxiajiao.jpg', 0, 1, '2025-05-22 15:47:15', NULL, 1);
INSERT INTO `caipin` VALUES (9, '杨枝甘露', 5, 22.00, '经典港式甜品，芒果、西柚、西米和椰奶的完美组合，清凉解腻，甜而不腻。', '/api/images/dishes/yangzhiganlu.jpg', 0, 1, '2025-05-22 15:47:15', NULL, 1);
INSERT INTO `caipin` VALUES (10, '黄金虾球', 1, 68.00, '精选新鲜大虾，外裹金黄面包糠炸至酥脆，内里虾肉鲜嫩多汁，配特制蘸酱', '/api/images/dishes/huangjinxiaqiu.jpg', 0, 1, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (11, '蒜香排骨', 1, 78.00, '精选猪肋排，蒜香腌制后烤制，外焦里嫩，蒜香浓郁，回味无穷', '/api/images/dishes/suanxiangpaigu.jpg', 0, 1, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (12, '清炒时蔬', 2, 28.00, '当季新鲜蔬菜清炒，保留原汁原味，健康低脂，清爽可口', '/api/images/dishes/qingchaoshishu.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (13, '西湖牛肉羹', 3, 32.00, '传统杭州风味，牛肉末与蛋花完美结合，口感滑嫩，鲜美爽口', '/api/images/dishes/xihuniurougeng.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (14, '菌菇鸡汤', 3, 45.00, '老母鸡与多种野生菌菇慢炖，汤色清澈，菌香浓郁，滋补养生', '/api/images/dishes/jungujitang.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (15, '扬州炒饭', 1, 28.00, '正宗扬州做法，米饭粒粒分明，配以虾仁、火腿、鸡蛋等食材，色香味俱全', '/api/images/dishes/yangzhouchaofan.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (16, '鲜榨西瓜汁', 5, 18.00, '100%新鲜西瓜现榨，不添加任何防腐剂，清凉解暑，夏日必备', '/api/images/dishes/xianzhaxiguazhi.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (17, '蜂蜜柠檬水', 5, 12.00, '新鲜柠檬切片，加入纯天然蜂蜜，酸甜适中，美容养颜', '/api/images/dishes/fengmimingmengshui.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (18, '冰镇酸梅汤', 5, 10.00, '古法熬制乌梅汤，加入山楂、甘草等原料，冰镇后口感更佳，消暑解腻', '/api/images/dishes/bingzhensuanmeitang.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (19, '西瓜冻奶西米露', 5, 22.00, '新鲜西瓜汁与椰奶完美融合，加入Q弹西米，清凉爽口，夏日特饮', '/api/images/dishes/xiguadongnaiximilu.jpg', 0, 1, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (20, '可口可乐', 5, 5.00, '经典可口可乐，冰镇后口感更佳，气泡十足', '/api/images/dishes/kekoukele.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (21, '排骨汤', 3, 38.00, '精选猪小排与玉米、胡萝卜等食材慢炖，汤鲜味美，营养丰富', '/api/images/dishes/paigutang.jpg', 0, 0, '2025-05-22 21:23:42', NULL, 1);
INSERT INTO `caipin` VALUES (22, '米饭', 4, 2.00, '优质大米蒸制而成，香软可口', '/api/images/dishes/mifan.jpg', 0, 0, '2025-05-21 18:06:21', NULL, 1);
INSERT INTO `caipin` VALUES (23, '番茄鸡蛋汤', 3, 18.00, '选用新鲜番茄和鸡蛋，营养丰富，开胃解腻', '/api/images/dishes/fanqiejidantang.jpg', 0, 0, '2025-05-21 18:06:21', NULL, 1);

-- ----------------------------
-- Table structure for caipinfenlei
-- ----------------------------
DROP TABLE IF EXISTS `caipinfenlei`;
CREATE TABLE `caipinfenlei`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of caipinfenlei
-- ----------------------------
INSERT INTO `caipinfenlei` VALUES (1, '热门推荐', '本店热门推荐菜品', 1, '2025-05-21 18:06:21', NULL, 1);
INSERT INTO `caipinfenlei` VALUES (2, '特色炒菜', '本店特色炒菜', 2, '2025-05-21 18:06:21', NULL, 1);
INSERT INTO `caipinfenlei` VALUES (3, '汤类', '各种营养汤品', 3, '2025-05-21 18:06:21', NULL, 1);
INSERT INTO `caipinfenlei` VALUES (4, '主食', '米饭面食等', 4, '2025-05-21 18:06:21', NULL, 1);
INSERT INTO `caipinfenlei` VALUES (5, '饮品', '各种饮品', 5, '2025-05-21 18:06:21', NULL, 1);

-- ----------------------------
-- Table structure for dingdanxiangqing
-- ----------------------------
DROP TABLE IF EXISTS `dingdanxiangqing`;
CREATE TABLE `dingdanxiangqing`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `dish_id` int NOT NULL COMMENT '菜品ID',
  `dish_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜品名称',
  `dish_price` decimal(10, 2) NOT NULL COMMENT '菜品价格',
  `quantity` int NOT NULL COMMENT '数量',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '小计',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `dingdanxiangqing_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `dingdanxiangqing_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `caipin` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dingdanxiangqing
-- ----------------------------
INSERT INTO `dingdanxiangqing` VALUES (1, 1, 1, '宫保鸡丁', 38.00, 2, 76.00);
INSERT INTO `dingdanxiangqing` VALUES (4, 2, 2, '水煮鱼', 58.00, 1, 58.00);

-- ----------------------------
-- Table structure for gouwuche
-- ----------------------------
DROP TABLE IF EXISTS `gouwuche`;
CREATE TABLE `gouwuche`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户ID',
  `dish_id` int NOT NULL COMMENT '菜品ID',
  `quantity` int NOT NULL COMMENT '数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `gouwuche_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `yonghu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `gouwuche_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `caipin` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gouwuche
-- ----------------------------
INSERT INTO `gouwuche` VALUES (4, 2, 2, 1, '2025-05-21 18:06:21', NULL);

-- ----------------------------
-- Table structure for guanliyuan
-- ----------------------------
DROP TABLE IF EXISTS `guanliyuan`;
CREATE TABLE `guanliyuan`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guanliyuan
-- ----------------------------
INSERT INTO `guanliyuan` VALUES (1, 'admin', '123456', '系统管理员', '13800138000', '2025-05-21 18:06:21', '2025-05-23 20:13:44', 1);

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `dish_id` int NOT NULL COMMENT '菜品ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `quantity` int NOT NULL COMMENT '数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '小计',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `caipin` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_items
-- ----------------------------
INSERT INTO `order_items` VALUES (1, 1, 1, '宫保鸡丁', 38.00, 2, 76.00, '/images/dishes/gongbaojiding.jpg');
INSERT INTO `order_items` VALUES (4, 2, 2, '水煮鱼', 58.00, 1, 58.00, '/images/dishes/shuizhuyu.jpg');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单号',
  `user_id` int NOT NULL COMMENT '用户ID',
  `order_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `amount` decimal(10, 2) NOT NULL COMMENT '总金额',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配送地址',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系电话',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '待支付' COMMENT '订单状态：待支付，已支付待配送，配送中，已完成，已取消',
  `delivery_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '配送费',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '未支付' COMMENT '支付状态：未支付，已支付',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '配送时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `yonghu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 1, 'DD20230501001', 98.00, '北京市朝阳区阳光大道123号', '13912345678', '已完成', 0.00, NULL, '已支付', '2025-05-19 18:06:21', '2025-05-18 18:06:21', NULL, '2025-05-20 18:06:21', '2025-05-21 18:06:21');
INSERT INTO `orders` VALUES (2, 1, 'DD20230502002', 76.00, '北京市朝阳区阳光大道123号', '13912345678', 'COMPLETED', 0.00, NULL, '已支付', '2025-05-20 18:06:21', '2025-05-20 18:06:21', '2025-05-23 20:10:41', '2025-05-21 18:06:21', NULL);
INSERT INTO `orders` VALUES (3, 2, 'DD20230503001', 116.00, '北京市海淀区学院路456号', '13987654321', '已支付待配送', 0.00, NULL, '已支付', '2025-05-21 18:06:21', '2025-05-21 18:06:21', NULL, NULL, NULL);
INSERT INTO `orders` VALUES (4, 2, 'DD20230504002', 38.00, '北京市海淀区学院路456号', '13987654321', '待支付', 0.00, NULL, '未支付', NULL, '2025-05-21 18:06:21', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for pingjia
-- ----------------------------
DROP TABLE IF EXISTS `pingjia`;
CREATE TABLE `pingjia`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `dish_id` int NOT NULL COMMENT '菜品ID',
  `rating` tinyint NOT NULL COMMENT '评分：1-5',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `pingjia_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `pingjia_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `yonghu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `pingjia_ibfk_3` FOREIGN KEY (`dish_id`) REFERENCES `caipin` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pingjia
-- ----------------------------

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (1, 'user1', '123456', '张三', '13912345678', '北京市朝阳区阳光大道123号', '2025-05-21 18:06:21', '2025-05-23 20:13:31', 1, NULL);
INSERT INTO `yonghu` VALUES (2, 'user2', '123456', '李四', '13987654321', '北京市海淀区学院路456号', '2025-05-21 18:06:21', NULL, 1, NULL);

-- ----------------------------
-- Table structure for youhuihuodong
-- ----------------------------
DROP TABLE IF EXISTS `youhuihuodong`;
CREATE TABLE `youhuihuodong`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '活动标题',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动描述',
  `discount_type` tinyint NOT NULL COMMENT '优惠类型：1-满减，2-折扣',
  `discount_value` decimal(10, 2) NOT NULL COMMENT '优惠值：满减为减免金额，折扣为折扣率',
  `threshold` decimal(10, 2) NULL DEFAULT NULL COMMENT '门槛金额',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of youhuihuodong
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
