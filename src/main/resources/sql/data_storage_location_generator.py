def generate_insert_statements():
    insert_statements = []
    location_names = []
    create_time = "2025-03-11 16:22:27"
    update_time = "2025-03-11 16:22:27"

# INSERT INTO wms_storage_location (id, warehouse_id, name, material_name, quantity, update_time, create_time) VALUES (1, 1, 'A-01-A1', NULL, NULL, '2024-04-19 16:22:27', '2024-04-19 16:22:27');
    id = 1
    # for main storage area
    for i in range(1, 81):
        location_names.append(f"{i}")
        for j in "ABC":
            location_names.append(f"{i}{j}")
    
    location_names.append("Ground Floor")
    location_names.append("1St Floor")

    for location_name in location_names:
        sql = (f"INSERT INTO `wms_storage_location` (`id`, `warehouse_id`, `name`, `material_name`, `quantity`, `create_time`, `update_time`) "
                f"VALUES ({id}, 1, '{location_name}', NULL, NULL, '{create_time}', '{update_time}');")
        id += 1
        insert_statements.append(sql)

    # for inventory room 1
    for i in range(1, 10):
        location_names.append(f"{i}")
        for j in "ABC":
            location_names.append(f"{i}{j}")

    for location_name in location_names:
        sql = (f"INSERT INTO `wms_storage_location` (`id`, `warehouse_id`, `name`, `material_name`, `quantity`, `create_time`, `update_time`) "
                f"VALUES ({id}, 2, '{location_name}', NULL, NULL, '{create_time}', '{update_time}');")
        id += 1
        insert_statements.append(sql)

    # for inventory room 2
    for i in range(1, 10):
        location_names.append(f"{i}")
        for j in "ABC":
            location_names.append(f"{i}{j}")

    for location_name in location_names:
        sql = (f"INSERT INTO `wms_storage_location` (`id`, `warehouse_id`, `name`, `material_name`, `quantity`, `create_time`, `update_time`) "
                f"VALUES ({id}, 3, '{location_name}', NULL, NULL, '{create_time}', '{update_time}');")
        id += 1
        insert_statements.append(sql)

    # for inventory room 3
    for i in range(1, 10):
        location_names.append(f"{i}")
        for j in "ABC":
            location_names.append(f"{i}{j}")

    for location_name in location_names:
        sql = (f"INSERT INTO `wms_storage_location` (`id`, `warehouse_id`, `name`, `material_name`, `quantity`, `create_time`, `update_time`) "
                f"VALUES ({id}, 4, '{location_name}', NULL, NULL, '{create_time}', '{update_time}');")
        id += 1
        insert_statements.append(sql)

    return insert_statements

# Generate the insert statements
insert_statements = generate_insert_statements()

# Write the insert statements to 'wms_resource.sql'
with open('wms_storage_location_india.sql', 'w') as file:
    for statement in insert_statements:
        file.write(statement + '\n')

print("Insert statements written to 'wms_storage_location_india.sql'.")
