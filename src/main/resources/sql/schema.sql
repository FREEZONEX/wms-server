-- warehouse definition
CREATE TABLE `wms_warehouse` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `warehouse_id` varchar(100) NOT NULL,
    `name` varchar(100) NOT NULL,
    `type` varchar(100) NOT NULL,
    `manager` varchar(100) DEFAULT NULL,
    `department` varchar(100) DEFAULT NULL,
    `email` varchar(200) DEFAULT NULL,
    `project_group` varchar(100) DEFAULT NULL,
    `note` varchar(200) DEFAULT NULL,
    `del_flag` tinyint(1) DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
    UNIQUE KEY `warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 ;

-- warehouse shelf's
CREATE TABLE `wms_storage_location` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `warehouse_id` bigint unsigned NOT NULL,
    `name` varchar(100) NOT NULL,
    `material_name` varchar(100) DEFAULT NULL,
    -- `occupied` tinyint(1) DEFAULT '0',
    `del_flag` tinyint(1) DEFAULT NULL,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1481 DEFAULT CHARSET=utf8mb4 ;

-- material predefined
CREATE TABLE `wms_material` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `product_code` varchar(100) NOT NULL,
    `name` varchar(100) NOT NULL,
    `product_type` varchar(100) DEFAULT NULL,
    `unit` varchar(100) DEFAULT NULL,
    `note` varchar(200) DEFAULT NULL,
    `specification` varchar(100) DEFAULT NULL,
    `max` bigint DEFAULT NULL,
    `min` bigint DEFAULT NULL,
    `status` varchar(100) DEFAULT NULL,
    `expect_wh_id` bigint DEFAULT NULL,
    `expact_storage_locations` varchar(1000) DEFAULT NULL,
    `del_flag` tinyint(1) DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8mb4 ;

-- material sub table, store all expected locations for material type
CREATE TABLE `wms_material_expected_location` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `material_id` bigint unsigned NOT NULL,
    `location_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_material_id` FOREIGN KEY (`material_id`) REFERENCES `wms_material` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1481 DEFAULT CHARSET=utf8mb4 ;

-- combine inbound, outbound,stocktaking together
CREATE TABLE `wms_material_transaction` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `material_code` varchar(100) CHARACTER SET utf8mb4  NOT NULL,
    `type` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `source` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `inbound_id` bigint unsigned DEFAULT NULL,
    `stocktaking_id` bigint unsigned DEFAULT NULL,
    `outbound_id` bigint DEFAULT NULL,
    `warehouse_id` bigint DEFAULT NULL,
    `stock_location_id` bigint DEFAULT NULL,
    `rf_id` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `operator` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `inbound_status` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `outbound_status` varchar(100) DEFAULT NULL,
    `note` varchar(200) DEFAULT NULL,
    `inbound_creator` varchar(100) DEFAULT NULL,
    `outbound_creator` varchar(100) DEFAULT NULL,
    `inbound_purchase_order_no` varchar(100) DEFAULT NULL,
    `outbound_purchase_order_no` varchar(100) DEFAULT NULL,
    `inbound_supplier` varchar(100) DEFAULT NULL,
    `outbound_supplier` varchar(100) DEFAULT NULL,
    `inbound_delivery_date` timestamp NULL DEFAULT NULL,
    `outbound_delivery_date` timestamp NULL DEFAULT NULL,
    `del_flag` tinyint(1) DEFAULT NULL,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3648 DEFAULT CHARSET=utf8mb4 ;

-- rfid and single material mapping definition
CREATE TABLE `wms_rfid_material` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `rf_id` varchar(100) NOT NULL,
    `material_code` varchar(100) NOT NULL,
    `del_flag` tinyint(1) DEFAULT NULL,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 ;

-- 3D model table for BI, not use anymore
CREATE TABLE `wms_threed_warehouse` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `location_id` bigint unsigned DEFAULT NULL,
    `location_name` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `material_name` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL,
    `del_flag` tinyint(1) DEFAULT NULL,
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1382 DEFAULT CHARSET=utf8mb4 ;

-- resource definition, include Forklift and pane
CREATE TABLE `wms_resource` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `type` varchar(100) NOT NULL,
    `note` varchar(200) DEFAULT NULL,
    `del_flag` tinyint(1) DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 ;

/*CREATE TABLE `wms_people` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `type` varchar(100) NOT NULL,
    `note` varchar(200) DEFAULT NULL,
    `del_flag` tinyint(1) DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 ;*/

-- task definition, putaway task for inbound, picking task for outbound
CREATE TABLE `wms_task` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `operation_id` bigint unsigned NOT NULL,   -- operation_id is inbound id for putaway task, and is outbound id for picking task
    `type` varchar(100) NOT NULL,              -- putaway | picking
    `note` varchar(200) DEFAULT NULL,
    `status` varchar(100) NOT NULL,            -- pending | assigned | completed
    `people_name` varchar(200) DEFAULT NULL,   -- assigned people name, 1 task can only assign to 1 people
    `del_flag` tinyint(1) DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 ;

-- task resource allocate, 1 task can be assigned many resources
CREATE TABLE `wms_task_resource` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `task_id` bigint unsigned NOT NULL,
    `resource_id` bigint unsigned NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_task_id` FOREIGN KEY (`task_id`) REFERENCES `wms_task` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4;

-- rule for task assignment, include resource assignment and people assignment
CREATE TABLE `wms_rule` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `task_type` varchar(100) NOT NULL,                  -- putaway | picking | all
    `warehouse_id` bigint unsigned NOT NULL,
    `location_expression` varchar(100) NOT NULL,        -- location name match expression: "A-%", "AC-%", "%-01-%"
    `resource_id_list` varchar(500) NOT NULL,           -- resource id array, split by comma ","
    `people_name` varchar(200) NOT NULL,                -- assigned people name, 1 task can only assign to 1 people
    `note` varchar(200) DEFAULT NULL,
    `enabled` tinyint(1) DEFAULT 1,
    `del_flag` tinyint(1) DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 ;