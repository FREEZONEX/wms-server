-- warehouse definition
CREATE TABLE IF NOT EXISTS `wms_warehouse` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `warehouse_id` VARCHAR(100) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(100) NOT NULL,
    `manager` VARCHAR(100) DEFAULT NULL,
    `department` VARCHAR(100) DEFAULT NULL,
    `email` VARCHAR(200) DEFAULT NULL,
    `project_group` VARCHAR(100) DEFAULT NULL,
    `note` VARCHAR(200) DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `name` (`name`),
    UNIQUE KEY `warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- warehouse shelf's, 1 location can only store 1 kind os material
CREATE TABLE IF NOT EXISTS `wms_storage_location` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `warehouse_id` BIGINT UNSIGNED NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `material_name` VARCHAR(100) DEFAULT NULL,
    `quantity` INT DEFAULT NULL,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_storage_location_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `wms_warehouse` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- material predefined
CREATE TABLE IF NOT EXISTS `wms_material` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `material_code` VARCHAR(100) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `material_type` VARCHAR(100) DEFAULT NULL,
    `unit` VARCHAR(100) DEFAULT NULL,
    `note` VARCHAR(200) DEFAULT NULL,
    `specification` VARCHAR(100) DEFAULT NULL,
    `max` BIGINT DEFAULT NULL,
    `min` BIGINT DEFAULT NULL,
    `status` VARCHAR(100) DEFAULT NULL,
    `expect_wh_id` BIGINT DEFAULT NULL,
    `expect_storage_locations` VARCHAR(1000) DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `material_code` (`material_code`),
    UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- material sub table, store all expected locations for material type
CREATE TABLE IF NOT EXISTS `wms_material_expected_location` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `material_id` BIGINT UNSIGNED NOT NULL,
    `location_id` BIGINT UNSIGNED NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_material_expected_location_material_id` FOREIGN KEY (`material_id`) REFERENCES `wms_material` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_material_expected_location_location_id` FOREIGN KEY (`location_id`) REFERENCES `wms_storage_location` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- rfid and single material mapping definition
CREATE TABLE IF NOT EXISTS `wms_rfid_material` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `rf_id` VARCHAR(100) NOT NULL,
    `material_id` BIGINT UNSIGNED NOT NULL,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_rfid_material_material_id` FOREIGN KEY (`material_id`) REFERENCES `wms_material` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- material storage location detail, 1 material can storage at N locations
CREATE TABLE IF NOT EXISTS `wms_material_storage_location` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `material_id` BIGINT UNSIGNED NOT NULL,
    `location_id` BIGINT UNSIGNED NOT NULL,
    `quantity` INT NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_material_storage_location_material_id` FOREIGN KEY (`material_id`) REFERENCES `wms_material` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_material_storage_location_id` FOREIGN KEY (`location_id`) REFERENCES `wms_storage_location` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- inbound record
CREATE TABLE IF NOT EXISTS `wms_inbound` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(100) DEFAULT NULL,
    `source` VARCHAR(100) DEFAULT NULL,                     -- PDA or Manual
    `note` VARCHAR(200) DEFAULT NULL,
    `creator` VARCHAR(100) DEFAULT NULL,
    `operator` VARCHAR(100) DEFAULT NULL,
    `status` VARCHAR(100) DEFAULT NULL,
    `purchase_order_no` VARCHAR(100) DEFAULT NULL,
    `supplier` VARCHAR(100) DEFAULT NULL,
    `delivery_date` TIMESTAMP NULL DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- inbound detail
CREATE TABLE IF NOT EXISTS `wms_inbound_detail` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `operation_id` BIGINT UNSIGNED NOT NULL,
    `location_id` BIGINT UNSIGNED NOT NULL,
    `material_id` BIGINT UNSIGNED NOT NULL,
    `quantity` INT NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_inbound_detail_operation_id` FOREIGN KEY (`operation_id`) REFERENCES `wms_inbound` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- outbound record
CREATE TABLE IF NOT EXISTS `wms_outbound` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(100) DEFAULT NULL,
    `source` VARCHAR(100) DEFAULT NULL,                     -- PDA or Manual
    `note` VARCHAR(200) DEFAULT NULL,
    `creator` VARCHAR(100) DEFAULT NULL,
    `operator` VARCHAR(100) DEFAULT NULL,
    `status` VARCHAR(100) DEFAULT NULL,                     -- pending | done
    `purchase_order_no` VARCHAR(100) DEFAULT NULL,
    `supplier` VARCHAR(100) DEFAULT NULL,
    `delivery_date` TIMESTAMP NULL DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- outbound detail
CREATE TABLE IF NOT EXISTS wms_outbound_detail (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `operation_id` BIGINT UNSIGNED NOT NULL,
    `location_id` BIGINT UNSIGNED NOT NULL,
    `material_id` BIGINT UNSIGNED NOT NULL,
    `quantity` INT NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_outbound_detail_operation_id` FOREIGN KEY (`operation_id`) REFERENCES `wms_outbound` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- stocktaking record
CREATE TABLE IF NOT EXISTS `wms_stocktaking` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type` VARCHAR(100) DEFAULT NULL,
    `source` VARCHAR(100) DEFAULT NULL,                     -- PDA or Manual
    `note` VARCHAR(200) DEFAULT NULL,
    `creator` VARCHAR(100) DEFAULT NULL,
    `operator` VARCHAR(100) DEFAULT NULL,
    `status` VARCHAR(100) DEFAULT NULL,                     -- pending | done
    `purchase_order_no` VARCHAR(100) DEFAULT NULL,
    `supplier` VARCHAR(100) DEFAULT NULL,
    `delivery_date` TIMESTAMP NULL DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- stocktaking detail
CREATE TABLE IF NOT EXISTS wms_stocktaking_detail (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `operation_id` BIGINT UNSIGNED NOT NULL,
    `location_id` BIGINT UNSIGNED NOT NULL,
    `material_id` BIGINT UNSIGNED NOT NULL,
    `quantity` INT NOT NULL,
    `stock_quantity` INT DEFAULT NULL,
    `discrepancy` INT DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_stocktaking_detail_operation_id` FOREIGN KEY (`operation_id`) REFERENCES `wms_stocktaking` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 3D model table for BI, not use anymore
/*CREATE TABLE `wms_threed_warehouse` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `location_id` BIGINT UNSIGNED DEFAULT NULL,
    `location_name` VARCHAR(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `material_name` VARCHAR(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1382 DEFAULT CHARSET=utf8mb4;*/

-- resource definition, include Forklift and pane
CREATE TABLE IF NOT EXISTS `wms_resource` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(100) NOT NULL,                   -- forklift | pane
    `note` VARCHAR(200) DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- resource occupy log, update_time - create_time is total time for this section
CREATE TABLE IF NOT EXISTS `wms_resource_occupy_log` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `task_id` BIGINT UNSIGNED NOT NULL,
    `resource_id` BIGINT UNSIGNED NOT NULL,
    `is_occupy` tinyINT(1) DEFAULT 1,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_resource_id` FOREIGN KEY (`resource_id`) REFERENCES `wms_resource` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*CREATE TABLE `wms_people` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(100) NOT NULL,
    `note` VARCHAR(200) DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4;*/

-- task definition, putaway task for inbound, picking task for outbound
CREATE TABLE IF NOT EXISTS `wms_task` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `operation_id` BIGINT UNSIGNED NOT NULL,   -- operation_id is inbound id for putaway task, and is outbound id for picking task
    `type` VARCHAR(100) NOT NULL,              -- putaway | pickup
    `note` VARCHAR(200) DEFAULT NULL,
    `status` VARCHAR(100) NOT NULL,            -- pending | done
    `people_name` VARCHAR(200) DEFAULT NULL,   -- assigned people name, 1 task can only assign to 1 people
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- task resource allocate, 1 task can be assigned many resources
CREATE TABLE IF NOT EXISTS `wms_task_resource` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `task_id` BIGINT UNSIGNED NOT NULL,
    `resource_id` BIGINT UNSIGNED NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_task_resource_task_id` FOREIGN KEY (`task_id`) REFERENCES `wms_task` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `uc_task_id_resource_id` UNIQUE (`task_id`, `resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- rule for task assignment, include resource assignment and people assignment
CREATE TABLE IF NOT EXISTS `wms_rule` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `task_type` VARCHAR(100) NOT NULL,                  -- putaway | pickup
    `warehouse_id` BIGINT UNSIGNED NOT NULL,
    `location_expression` VARCHAR(100) NOT NULL,        -- location name match expression: "A-%", "AC-%", "%-01-%"
    `resource_id_list` VARCHAR(500) NOT NULL,           -- resource id array, split by comma ",", for example: "1,2,3"
    `people_name` VARCHAR(200) NOT NULL,                -- assigned people name, 1 task can only assign to 1 people
    `note` VARCHAR(200) DEFAULT NULL,
    `enabled` tinyINT(1) DEFAULT 1,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_rule_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `wms_warehouse` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `supos_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(100) NOT NULL,
  `accountType` INT DEFAULT NULL,
  `lockStatus` INT DEFAULT NULL,
  `valid` TINYINT DEFAULT NULL,
  `personCode` VARCHAR(100) NOT NULL,
  `personName` VARCHAR(100) NOT NULL,
  `delFlag` TINYINT DEFAULT NULL,
  `createTime` TIMESTAMP NOT NULL,
  `modifyTime` TIMESTAMP NOT NULL,
  `syncTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `personCode` (`personCode`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- time series data prediction data
CREATE TABLE IF NOT EXISTS `wms_prediction` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `material_id` BIGINT UNSIGNED NOT NULL,
    `material_name` VARCHAR(100) NOT NULL,
    `prediction_date` TIMESTAMP NOT NULL,
    `prediction_count` INT DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;