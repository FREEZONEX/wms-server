import requests
import random
import json
from datetime import datetime

# Constants
API_URL = 'http://127.0.0.1:8085/wms/inbound/add'
HEADERS = {'Content-Type': 'application/json'}
CREATOR = 'Pradipta.Mukherjee'
OPERATOR = 'Agatha.Nair'
SOURCE = 'manual'
SUPPLIER = 'SupplierIn'
TYPES = [
    'Purchase Inbound', 'Production Inbound', 'Return Inbound', 'Transfer Inbound',
    'Consignment Inbound', 'Vendor Managed Inventory (VMI) Inbound', 'Sample Inbound',
    'Donation Inbound', 'Intercompany Inbound'
]
STATUS = ['pending', 'done']
DELIVERY_DATE = '2024-05-27T08:39:35.000'
CREATE_TIME = '2024-04-19T16:22:27.000'
UPDATE_TIME = '2024-04-19T16:22:27.000'

# Function to generate dummy data
def generate_dummy_data(order_number, material_id):
    type_ = random.choice(TYPES)
    note = f"note of {type_.lower()}"
    status = random.choice(STATUS)
    purchase_order_no = f"PO_IN_{order_number:04d}"

    details = []
    for location_id in range((material_id - 1) * 200 + 1, material_id * 200 + 1):
        detail = {
            "location_id": location_id,
            "material_id": material_id,
            "quantity": random.randint(1, 20)
        }
        details.append(detail)

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

# Main script to call the API 440 times
for i in range(1, 441):
    data = generate_dummy_data(i, i)
    # print(data)
    response = requests.post(API_URL, headers=HEADERS, data=json.dumps(data))

    if response.status_code == 200:
        print(f"Request {i} succeeded.")
    else:
        print(f"Request {i} failed with status code {response.status_code}.")

print("Completed all requests.")
