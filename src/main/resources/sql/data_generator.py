import random

# Function to generate Excel-style rows
def generate_excel_style_rows(n):
    result = []
    for i in range(1, n+1):
        name = ''
        while i > 0:
            i -= 1
            name = chr(i % 26 + ord('A')) + name
            i //= 26
        result.append(name)
    return result

# Generate storage location names
rows = generate_excel_style_rows(40)
levels = [f"{i:02d}" for i in range(1, 6)]
sections = [f"{chr(group)}{number}" for group in range(ord('A'), ord('F')+1) for number in range(1, 6)]
full_names = [f"{row}-{level}-{section}" for row in rows for level in levels for section in sections]

# Material names and probabilities
materials = ['SR20VET', 'VQ35DE', 'VG30DETT', 'RB26DETT', 'SR16VE', 'CA18DET', 'SR20DE']
probabilities = [0.1 if mat else 0.7 for mat in materials] + [0.7]  # Last entry for NULL probability

# Prepare SQL insert statements
location_statements = []
warehouse_statements = []

location_id = 1
warehouse_id_start = 1
warehouse_id_end = 11
update_time = "2024-04-19 16:22:27"
create_time = "2024-04-19 16:22:27"
# del_flag = "NULL"

for warehouse_id in range(warehouse_id_start, warehouse_id_end + 1):
    for name in full_names:
        # Location table
        material_name = random.choices(materials + [None], weights=probabilities, k=1)[0]
        material_name_sql = 'NULL' if material_name is None else f"'{material_name}'"
        location_statement = f"INSERT INTO wms_storage_location (id, warehouse_id, name, material_name, update_time, create_time) VALUES ({location_id}, {warehouse_id}, '{name}', {material_name_sql}, '{update_time}', '{create_time}');"
        location_statements.append(location_statement)

        # wms_threed_warehouse table
        # warehouse_statement = f"INSERT INTO wms_threed_warehouse (id, location_id, location_name, material_name, del_flag, update_time, create_time) VALUES ({warehouse_id}, {location_id}, '{name}', {material_name_sql}, {del_flag}, '{update_time}', '{create_time}');"
        # warehouse_statements.append(warehouse_statement)

        location_id += 1

# Write SQL statements to a file
with open('wms_storage_location.sql', 'w') as file:
    for statement in location_statements + warehouse_statements:
        file.write(statement + "\n")

print("SQL file has been generated.")
