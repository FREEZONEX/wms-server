import requests
import random
import json
from datetime import datetime

# Constants
MATERIAL_API_URL = 'http://127.0.0.1:8085/wms/material/get'
OUTBOUND_API_URL = 'http://127.0.0.1:8085/wms/outbound/add'
HEADERS = {'Content-Type': 'application/json'}
CREATOR = 'Rajasekaran.R'
OPERATOR = 'Ravi.Soni'
SOURCE = 'manual'
SUPPLIER = 'SupplierOut'
OUTBOUND_TYPES = [
    'Customer Shipment', 'Internal Transfer', 'Production Supply', 'Return to Vendor (RTV)',
    'Sample Shipment', 'Consignment Shipment', 'Distribution', 'Scrap Disposal', 'Donation', 'Exhibition or Event Shipment'
]
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

# Function to generate outbound data
def generate_outbound_data(order_number, material_id, stock):
    type_ = random.choice(OUTBOUND_TYPES)
    note = f"note of {type_.lower()}"
    status = random.choice(STATUS)
    purchase_order_no = f"PO_OUT_{order_number:04d}"

    # Select 2/3 of the stock locations
    selected_stock = random.sample(stock, k=int(len(stock) * 2 / 3))
    details = [
        {
            "location_id": int(item['location_id']),
            "material_id": material_id,
            "quantity": int(item['quantity'])
        }
        for item in selected_stock
    ]

    data = {
        "type": type_,
        "source": SOURCE,
        "note": note,
        "creator": CREATOR,
        "operator": OPERATOR,
        "status": status,
        "purchase_order_no": purchase_order_no,
        "supplier": SUPPLIER,
        "delivery_date": DELIVERY_DATE,
        "create_time": CREATE_TIME,
        "update_time": UPDATE_TIME,
        "details": details
    }

    return data

# Main script to call the API for each material_id
order_number = 1
for material_id in range(1, 441):
    stock = fetch_material_storage(material_id)
    if stock:
        outbound_data = generate_outbound_data(order_number, material_id, stock)
        response = requests.post(OUTBOUND_API_URL, headers=HEADERS, data=json.dumps(outbound_data))

        if response.status_code == 200:
            print(f"Outbound request for material_id {material_id} succeeded.")
        else:
            print(f"Outbound request for material_id {material_id} failed with status code {response.status_code}.")

        order_number += 1

print("Completed all requests.")
