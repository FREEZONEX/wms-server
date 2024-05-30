import pandas as pd
import mysql.connector
from mysql.connector import Error
from datetime import datetime

# Database connection details
db_host = "127.0.0.1"
db_port = 6033
db_user = "root"
db_password = "root1234"
db_name = "wms"

# Read the CSV file
csv_file_path = 'modified_file.csv'
df = pd.read_csv(csv_file_path)

# Function to convert date format
def convert_date_format(date_str):
    try:
        # Convert from 'DD/MM/YYYY' to 'YYYY-MM-DD'
        return datetime.strptime(date_str, '%d/%m/%Y').strftime('%Y-%m-%d %H:%M:%S')
    except ValueError as e:
        print(f"Error converting date: {e}")
        return None

# Establish database connection
try:
    connection = mysql.connector.connect(
        host=db_host,
        port=db_port,
        user=db_user,
        password=db_password,
        database=db_name
    )

    if connection.is_connected():
        cursor = connection.cursor()

        # Insert data into table
        material_name_to_id = {}
        material_id = 1

        for index, row in df.iterrows():
            material_name = row['material_name']
            prediction_date = convert_date_format(row['prediction_date'])
            prediction_count = row['prediction_count']

            if not prediction_date:
                continue  # Skip rows with invalid dates

            if material_name not in material_name_to_id:
                material_name_to_id[material_name] = material_id
                material_id += 1

            insert_query = """
            INSERT INTO `wms_prediction` (material_id, material_name, prediction_date, prediction_count)
            VALUES (%s, %s, %s, %s)
            """
            record = (
                material_name_to_id[material_name],
                material_name,
                prediction_date,
                prediction_count
            )
            cursor.execute(insert_query, record)

        connection.commit()

        print("Data inserted successfully")

except Error as e:
    print(f"Error: {e}")
finally:
    if connection.is_connected():
        cursor.close()
        connection.close()
        print("MySQL connection is closed")
