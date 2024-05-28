import requests
import json
import random

# Define the URL and headers
# url = 'http://localhost:8085/wms/task/update'
url = 'http://121.7.36.93:30086/wms/task/update'
headers = {
    'Content-Type': 'application/json'
}

# Loop through the IDs and update the status randomly
for task_id in range(582, 1462):  # The range function is inclusive of the first number and exclusive of the second
    status = random.choice(['done', 'pending'])
    payload = {
        'id': task_id,
        'status': status
    }

    # Convert the payload to JSON format
    data = json.dumps(payload)

    # Make the POST request
    response = requests.post(url, headers=headers, data=data)

    # Print the response for debugging
    print(f'Updated task ID {task_id} to status {status}. Response: {response.status_code}, {response.text}')
