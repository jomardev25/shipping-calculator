CREATE TABLE IF NOT EXISTS `shipping_rates` (
  `id` int NOT NULL AUTO_INCREMENT,
  `condition` varchar(255) DEFAULT NULL,
  `rate` double(7,2) NOT NULL DEFAULT '0.00',
  `rule_name` enum('HEAVY_PARCEL','LARGE_PARCEL','MEDIUM_PARCEL','REJECT','SMALL_PARCEL') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaf9jiu5p0cb9o8mxm9ihsl6dg` (`rule_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;