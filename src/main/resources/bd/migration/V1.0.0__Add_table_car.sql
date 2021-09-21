SET MODE MYSQL;

CREATE TABLE IF NOT EXISTS `car` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `color` varchar(255),
    `model` varchar(255),
    `order_data` date

)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

