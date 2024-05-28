import random

def generate_insert_statements():
    insert_statements = []
    names = [
        "Agatha.Nair", "Arif.Hussain", "Arjun.Baidya.Ray", "Bhaskara.Rao.Ravva", "IMI_Rayan",
        "IMI_user", "IMI_Yara", "Jana.Shivarama.Krishna", "Majid.Husain.Choudhary",
        "Mohammed.Noorulla.Sheriff", "Nitin.Bali", "Pradipta.Mukherjee", "Prakash.M",
        "Rajasekaran.R", "Ravi.Kumar.Pasupuleti", "Ravi.Kumar.Pasupuleti_1", "Ravi.Soni",
        "Saubhagyalaxmi.Samantaray", "ShiMu", "Sravan.Murarishetti", "Umesh.Hegde",
        "Venu.Gopal.Valiveti_1", "Vijaya.Kumari.Miryala"
    ]

    location_expressions = [
        r"^(A-|B-|C-|D-|E-|F-|G-|H-|I-|J-).*",
        r"^(K-|L-|M-|N-|O-|P-|Q-|R-|S-|T-).*",
        r"^(U-|V-|W-|X-|Y-|Z-|AA-|AB-|AC-|AD-).*",
        r"^(AE-|AF-|AG-|AH-|AI-|AJ-|AK-|AL-|AM-|AN-).*"
    ]

    create_time = "2024-04-19 16:22:27"
    update_time = "2024-04-19 16:22:27"

    for i in range(1, 45):
        name = f"rule {i}"
        task_type = "putaway"
        warehouse_id = (i - 1) // 4 + 1
        location_expression = location_expressions[(i - 1) % 4]
        resource_id_list = f"{2 * i - 1},{2 * i}"
        people_name = random.choice(names)
        note = f"Note about {name}"
        enabled = 1

        insert_statement = (f"INSERT INTO `wms_rule` (`id`, `name`, `task_type`, `warehouse_id`, "
                            f"`location_expression`, `resource_id_list`, `people_name`, `note`, `enabled`, "
                            f"`create_time`, `update_time`) VALUES ({i}, '{name}', '{task_type}', {warehouse_id}, "
                            f"'{location_expression}', '{resource_id_list}', '{people_name}', '{note}', {enabled}, "
                            f"'{create_time}', '{update_time}');")
        insert_statements.append(insert_statement)

    # Generate insert statements for "pickup" type
    for i in range(1, 45):
        name = f"rule {i + 44}"
        task_type = "pickup"
        warehouse_id = (i - 1) // 4 + 1
        location_expression = location_expressions[(i - 1) % 4]
        resource_id_list = f"{2 * i - 1},{2 * i}"
        people_name = random.choice(names)
        note = f"Note about {name}"
        enabled = 1

        insert_statement = (f"INSERT INTO `wms_rule` (`id`, `name`, `task_type`, `warehouse_id`, "
                            f"`location_expression`, `resource_id_list`, `people_name`, `note`, `enabled`, "
                            f"`create_time`, `update_time`) VALUES ({i + 44}, '{name}', '{task_type}', {warehouse_id}, "
                            f"'{location_expression}', '{resource_id_list}', '{people_name}', '{note}', {enabled}, "
                            f"'{create_time}', '{update_time}');")
        insert_statements.append(insert_statement)

    return insert_statements

# Generate the insert statements
insert_statements = generate_insert_statements()

# Write the insert statements to 'wms_rule.sql'
with open('wms_rule.sql', 'w') as file:
    for statement in insert_statements:
        file.write(statement + '\n')

print("Insert statements written to 'wms_rule.sql'.")
