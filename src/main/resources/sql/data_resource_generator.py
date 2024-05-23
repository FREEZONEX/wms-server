def generate_insert_statements():
    insert_statements = []
    create_time = "2024-04-19 16:22:27"
    update_time = "2024-04-19 16:22:27"

    for i in range(1, 45):
        # Generate Forklift insert statement
        forklift_name = f"Forklift {i}"
        forklift_type = "forklift"
        forklift_note = f"Note about {forklift_name}"
        forklift_insert = (f"INSERT INTO `wms_resource` (`id`, `name`, `type`, `note`, `create_time`, `update_time`) "
                           f"VALUES ({2 * i - 1}, '{forklift_name}', '{forklift_type}', '{forklift_note}', "
                           f"'{create_time}', '{update_time}');")
        insert_statements.append(forklift_insert)

        # Generate Pallet insert statement
        pallet_name = f"Pallet {i}"
        pallet_type = "pallet"
        pallet_note = f"Note about {pallet_name}"
        pallet_insert = (f"INSERT INTO `wms_resource` (`id`, `name`, `type`, `note`, `create_time`, `update_time`) "
                         f"VALUES ({2 * i}, '{pallet_name}', '{pallet_type}', '{pallet_note}', "
                         f"'{create_time}', '{update_time}');")
        insert_statements.append(pallet_insert)

    return insert_statements

# Generate the insert statements
insert_statements = generate_insert_statements()

# Write the insert statements to 'wms_resource.sql'
with open('wms_resource.sql', 'w') as file:
    for statement in insert_statements:
        file.write(statement + '\n')

print("Insert statements written to 'wms_resource.sql'.")
