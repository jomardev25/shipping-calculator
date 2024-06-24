INSERT INTO `shipping_rates` (`id`, `condition`, `rate`, `rule_name`) VALUES
	(1, 'Weight exceeds 50kg', 0.00, 'REJECT'),
	(2, 'Weight exceeds 10kg', 20.00, 'HEAVY_PARCEL'),
	(3, 'Volume is less than 1500 cm3', 0.03, 'SMALL_PARCEL'),
	(4, 'Volume is less than 2500 cm3', 0.04, 'MEDIUM_PARCEL'),
	(5, NULL, 0.05, 'LARGE_PARCEL');