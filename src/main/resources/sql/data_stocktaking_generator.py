import requests
import random
import json
from datetime import datetime

# Constants
MATERIAL_API_URL = 'http://127.0.0.1:8085/wms/material/get'
STOCKTAKING_API_URL = 'http://127.0.0.1:8085/wms/stocktaking/add'
HEADERS = {'Content-Type': 'application/json'}
CREATOR = 'Saubhagyalaxmi.Samantaray'
OPERATOR = 'Nitin.Bali'
SOURCE = 'manual'
STOCKTAKING_TYPES = ['Dynamic Stocktaking', 'Static Stocktaking']
STATUS = ['pending', 'done']
DELIVERY_DATE = '2024-05-27T08:39:35.000'
CREATE_TIME = '2024-04-19T16:22:27.000'
UPDATE_TIME = '2024-04-19T16:22:27.000'

# Function to fetch material storage situation
def fetch_material_storage(material_id):
    payload = {
        "id": material_id,
        "show_stock": True
    }
    response = requests.post(MATERIAL_API_URL, headers=HEADERS, data=json.dumps(payload))
    if response.status_code == 200:
        data = response.json()
        return data['data']['list'][0]['stock']
    else:
        print(f"Failed to fetch material storage for material_id {material_id} with status code {response.status_code}")
        return []

# Function to adjust quantity for stocktaking
def adjust_quantity(original_quantity):
    rand_value = random.random()
    if rand_value < 0.50:
        # 50% chance to keep the same
        return original_quantity
    elif rand_value < 0.70:
        # 20% chance to increase quantity
        return original_quantity + random.randint(1, 5)
    else:
        # 20% chance to decrease quantity
        return max(0, original_quantity - random.randint(1, 5))

# Function to generate stocktaking data
def generate_stocktaking_data(material_id, stock):
    type_ = random.choice(STOCKTAKING_TYPES)
    note = f"description of {type_.lower()}"
    status = random.choice(STATUS)

    details = [
        {
            "location_id": int(item['location_id']),
            "material_id": material_id,
            "quantity": adjust_quantity(int(item['quantity']))
        }
        for item in stock
    ]

    data = {
        "type": type_,
        "source": SOURCE,
        "note": note,
        "creator": CREATOR,
        "operator": OPERATOR,
        "status": status,
        "delivery_date": DELIVERY_DATE,
        "create_time": CREATE_TIME,
        "update_time": UPDATE_TIME,
        "details": details
    }

    return data

# Main script to call the API for each material_id
for material_id in range(1, 441):
    stock = fetch_material_storage(material_id)
    if stock:
        stocktaking_data = generate_stocktaking_data(material_id, stock)
        # print(stocktaking_data)
        response = requests.post(STOCKTAKING_API_URL, headers=HEADERS, data=json.dumps(stocktaking_data))

        if response.status_code == 200:
            print(f"Stocktaking request for material_id {material_id} succeeded.")
        else:
            print(f"Stocktaking request for material_id {material_id} failed with status code {response.status_code}.")

print("Completed all requests.")
