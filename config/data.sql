-- 建表脚本（已修正语法）
CREATE TABLE `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `department` VARCHAR(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `role` ENUM('ADMIN','USER','DOCTER'),
  `open_id` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信openId',
  `avatar` VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建child表（儿童信息表）
CREATE TABLE `child` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` enum('男','女') NOT NULL,
  `birthdate` date NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建growth_record表（生长数据记录表）
CREATE TABLE `growth_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `child_id` bigint(20) NOT NULL,
  `height` decimal(5,2) DEFAULT NULL,
  `weight` decimal(5,2) DEFAULT NULL,
  `bmi` decimal(5,2) DEFAULT NULL,
  `bone_age` decimal(5,2) DEFAULT NULL,
  `test_date` date NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_child_id` (`child_id`),
  KEY `idx_test_date` (`test_date`),
  CONSTRAINT `fk_growth_record_child` FOREIGN KEY (`child_id`) REFERENCES `child` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `prediction_result` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `child_id` BIGINT,
  `risk_level` ENUM('LOW','MID','HIGH') NOT NULL,
  `prediction_date` DATE NOT NULL,
  `model_version` VARCHAR(50) NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`child_id`) REFERENCES `child`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `appointment` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `parent_id` BIGINT,
  `doctor_id` BIGINT,
  `appointment_time` DATETIME NOT NULL,
  `status` ENUM('UN_CONFIRM','CONFIRMED','CONCELED') NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `health_article` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `author` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `publish_date` DATE NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `feedback` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `parent_id` BIGINT,
  `content` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `reply` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `log` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `action` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` BIGINT NOT NULL,
  `action_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `details` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `message` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `receiver_id` BIGINT NOT NULL,
  `receiver_role` ENUM('USER','DOCTER','ADMIN') NOT NULL,
  `message` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` ENUM('UNREAD','READED') DEFAULT 'UNREAD',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `notification` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `receiver_id` BIGINT NOT NULL,
  `receiver_role` ENUM('USER','DOCTER','ADMIN') NOT NULL,
  `message` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` ENUM('UNREAD','READED') DEFAULT 'UNREAD',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建diet_record表（饮食记录表）
CREATE TABLE `diet_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `child_id` bigint(20) NOT NULL,
  `meal_type` enum('早餐','午餐','晚餐','加餐','零食') NOT NULL COMMENT '餐次类型',
  `food_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '食物名称',
  `food_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '食物分类',
  `quantity` decimal(8,2) NOT NULL COMMENT '食物分量',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单位（克、毫升、个等）',
  `calories` decimal(8,2) DEFAULT NULL COMMENT '热量（卡路里）',
  `protein` decimal(8,2) DEFAULT NULL COMMENT '蛋白质（克）',
  `carbohydrate` decimal(8,2) DEFAULT NULL COMMENT '碳水化合物（克）',
  `fat` decimal(8,2) DEFAULT NULL COMMENT '脂肪（克）',
  `fiber` decimal(8,2) DEFAULT NULL COMMENT '膳食纤维（克）',
  `record_date` date NOT NULL COMMENT '记录日期',
  `record_time` time DEFAULT NULL COMMENT '记录时间',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `image_list` json DEFAULT NULL COMMENT '图片URL列表（JSON格式）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_child_id` (`child_id`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_meal_type` (`meal_type`),
  CONSTRAINT `fk_diet_record_child` FOREIGN KEY (`child_id`) REFERENCES `child` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='儿童饮食记录表';

-- 创建exercise_record表（运动记录表）
CREATE TABLE `exercise_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `child_id` bigint(20) NOT NULL,
  `exercise_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '运动类型',
  `exercise_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '运动分类（有氧、力量、柔韧性等）',
  `duration` int(11) NOT NULL COMMENT '运动时长（分钟）',
  `intensity` enum('低','中','高') DEFAULT '中' COMMENT '运动强度',
  `calories_burned` decimal(8,2) DEFAULT NULL COMMENT '消耗热量（卡路里）',
  `distance` decimal(8,2) DEFAULT NULL COMMENT '运动距离（公里）',
  `steps` int(11) DEFAULT NULL COMMENT '步数',
  `heart_rate_avg` int(11) DEFAULT NULL COMMENT '平均心率',
  `heart_rate_max` int(11) DEFAULT NULL COMMENT '最大心率',
  `record_date` date NOT NULL COMMENT '记录日期',
  `start_time` time DEFAULT NULL COMMENT '开始时间',
  `end_time` time DEFAULT NULL COMMENT '结束时间',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_child_id` (`child_id`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_exercise_type` (`exercise_type`),
  CONSTRAINT `fk_exercise_record_child` FOREIGN KEY (`child_id`) REFERENCES `child` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='儿童运动记录表';