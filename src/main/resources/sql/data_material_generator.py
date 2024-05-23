import requests
import random
import string
import sys


goods = [
    "Aluminum Foil", "Baking Soda", "Bath Towels", "Batteries", "Bleach", "Canned Beans",
    "Canned Soup", "Coffee Beans", "Dish Soap", "Disposable Gloves", "Face Masks",
    "Flashlights", "Flour", "Hand Sanitizer", "Instant Noodles", "Laundry Detergent",
    "Light Bulbs", "Matches", "Microwave Popcorn", "Olive Oil", "Paper Plates",
    "Paper Towels", "Pasta", "Peanut Butter", "Plastic Cutlery", "Plastic Wrap",
    "Potato Chips", "Printer Paper", "Rubbing Alcohol", "Salt", "Shampoo", "Soap Bars",
    "Sponges", "Sugar", "Tea Bags", "Toilet Paper", "Toothpaste", "Trash Bags",
    "Vinegar", "Water Bottles", "Apple Juice", "Barbecue Sauce", "Black Pepper",
    "Body Lotion", "Bread", "Brown Rice", "Cereal", "Cheese", "Chocolate Bars",
    "Cleaning Wipes", "Coffee Filters", "Cooking Oil", "Corn Flakes", "Crackers",
    "Deodorant", "Diapers", "Dish Towels", "Dishwashing Tablets", "Fabric Softener",
    "Facial Tissues", "Frozen Pizza", "Frozen Vegetables", "Garbage Bags", "Granola Bars",
    "Green Tea", "Hair Conditioner", "Honey", "Ice Cream", "Ketchup", "Laundry Baskets",
    "Laundry Pods", "Mayonnaise", "Mustard", "Napkins", "Oatmeal", "Oranges",
    "Paper Clips", "Pasta Sauce", "Peanut Butter Cups", "Pickles", "Plastic Cups",
    "Plastic Forks", "Plastic Spoons", "Popcorn", "Razors", "Rice", "Rubber Bands",
    "Sandwich Bags", "Scissors", "Scrub Brushes", "Regular Shampoo", "Shaving Cream",
    "Snack Bars", "Soda", "Soup Mix", "Soy Sauce", "Spaghetti", "Spaghetti Sauce",
    "Sponge Mop", "Cellulose Sponges", "Spray Cleaner", "Sugar Cubes", "Sunflower Oil",
    "Tea Towels", "Tissue Paper", "Tomato Paste", "Tomato Sauce", "Toothbrushes",
    "Toothpicks", "Vegetable Oil", "White Vinegar", "Water Filter", "Wet Wipes", "White Rice",
    "Whole Wheat Bread", "Aluminum Pans", "Apple Cider Vinegar", "Baking Powder",
    "Band-Aids", "Bar Soap", "BBQ Chips", "Black Tea", "Bleach Wipes", "Blue Cheese",
    "Bottle Openers", "Bread Crumbs", "Brownie Mix", "Butter", "Cake Mix", "Can Openers",
    "Canned Corn", "Canned Fish", "Canned Fruit", "Canned Meat", "Canned Peas",
    "Canned Tomatoes", "Caramel Sauce", "Carpet Cleaner", "Cat Food", "Chicken Broth",
    "Chicken Nuggets", "Chili Powder", "Chocolate Chips", "Cinnamon", "Clorox Wipes",
    "Cocoa Powder", "Coconut Oil", "Coffee Creamer", "Cooking Spray", "Cotton Balls",
    "Cotton Swabs", "Croutons", "Deli Meat", "Disinfectant Spray", "Dog Food",
    "Dried Fruit", "Energy Bars", "Envelopes", "Evaporated Milk", "Fabric Dye",
    "Face Wash", "Fish Sticks", "Foil Pans", "French Fries", "Frozen Berries",
    "Frozen Dinners", "Garlic Powder", "Gelatin", "Gift Wrap", "Grape Juice",
    "Gravy Mix", "Greek Yogurt", "Ground Coffee", "Hand Soap", "Hot Chocolate",
    "Hot Sauce", "Ice Packs", "Instant Coffee", "Jam", "Jelly", "Kitchen Towels",
    "Lemon Juice", "Lighter Fluid", "Lighters", "Margarine", "Marshmallows",
    "Meatballs", "Microwave Dinners", "Milk", "Molasses", "Muffin Mix", "Mushrooms",
    "Nail Clippers", "Non-Stick Spray", "Noodles", "Olive Oil Spray", "Onion Powder",
    "Orange Juice", "Oven Cleaner", "Pancake Mix", "Paper Napkins", "Parmesan Cheese",
    "Pasta Salad", "Peanut Oil", "Penne Pasta", "Pepperoni", "Pet Shampoo",
    "Pickle Relish", "Pineapple Juice", "Pop Tarts", "Popsicles", "Powdered Sugar",
    "Protein Bars", "Q-tips", "Raisins", "Red Wine Vinegar", "Relish", "Rubber Gloves",
    "Salad Dressing", "Salsa", "Sandwich Spread", "Saran Wrap", "Scouring Pads",
    "Sea Salt", "Sesame Oil", "Soy Milk", "Sports Drinks", "Spatulas", "Spray Bottles",
    "Sriracha Sauce", "Steak Sauce", "Steel Wool", "Sugar Substitute", "Sunflower Seeds",
    "Sweet Pickles", "Taco Shells", "Tartar Sauce", "Tissues", "Fluoride Toothpaste",
    "Tortilla Chips", "Trash Cans", "Turkey Bacon", "Vanilla Extract", "Balsamic Vinegar",
    "Vitamin Water", "Whipped Cream", "Whole Grain Bread", "Yogurt", "Zinc Tablets",
    "Absorbent Pads", "Aluminum Cans", "Antibacterial Soap", "Applesauce", "Apron",
    "Baking Trays", "Bandages", "Bath Mats", "Beef Jerky", "Bell Peppers", "Berry Mix",
    "Biscuit Mix", "Blender", "Body Scrub", "Bottle Brush", "Bottled Water",
    "Bread Rolls", "Broccoli", "Bubble Bath", "Cake Pan", "Candy Bars", "Cat Litter",
    "Cat Treats", "Cheese Slices", "Chewing Gum", "Chocolate Milk", "Cling Film",
    "Cocoa", "Coconut Milk", "Coffee Pods", "Conditioner", "Cookie Dough", "Corn Meal",
    "Cream Cheese", "Dental Floss", "Dog Biscuits", "Dog Treats", "Drain Cleaner",
    "Dressing", "Dry Beans", "Dry Soup Mix", "Ear Plugs", "Egg Noodles", "Eggs",
    "Epsom Salt", "Organic Evaporated Milk", "Eye Drops", "Facial Masks", "Lotion Facial Tissues",
    "Fish Food", "Floss Picks", "Food Coloring", "Garlic Salt", "Gel Pens",
    "Gingersnaps", "Glass Cleaner", "Graham Crackers", "Gravy", "Ground Beef",
    "Hand Lotion", "Hair Gel", "Hand Warmers", "Hot Dog Buns", "Instant Oatmeal",
    "Jello Mix", "Juice Boxes", "Kitchen Cleaner", "Lasagna Noodles", "Lentils",
    "Licorice", "Liquid Detergent", "Maple Syrup", "Meat Marinade", "Melba Toast",
    "Microwave Meals", "Mixed Nuts", "White Button Mushrooms", "Nut Mix", "Oregano", "Paper Straws",
    "Parmesan", "Party Plates", "Pasta Shells", "Pita Bread", "Pizza Crust",
    "Plastic Bags", "Potato Salad", "Pretzels", "Protein Powder", "Pudding Mix",
    "Raisin Bread", "Ranch Dressing", "Resealable Bags", "Ribbons", "Root Beer",
    "Salad Mix", "Salsa Dip", "Sardines", "Seasoning Salt", "Anti-Dandruff Shampoo", "Sheet Pans",
    "Slippers", "Soap", "Soy Protein", "Sparkling Water", "Spice Mix", "Sponge Brushes",
    "Sports Tape", "Steak Rub", "Stevia", "String Cheese", "Styrofoam Cups", "Sunblock",
    "Syrup", "Taco Sauce", "Tape", "Gluten-Free Tartar Sauce", "Tea", "Thermos", "Tissue",
    "Toast Bread", "Tomato Ketchup", "Herb-Infused Tomato Paste", "Tongs", "Tonic Water",
    "Toothbrush", "Tupperware", "Twine", "Vanilla Beans", "Vegetable Broth",
    "Soybean Oil", "White Wine Vinegar", "Vitamins", "Waffle Mix", "Watermelon Juice",
    "Wheat Bread", "Whisk", "Rice Vinegar", "Whole Wheat Pasta", "Wine Bottles",
    "Wipes", "Wood Cleaner", "Yeast", "Yogurt Drinks", "Zinc Cream", "Almond Butter",
    "Avocado Oil", "Baking Cups", "Barley", "Beets", "Black Beans", "Brown Sugar",
    "Butter Cookies", "Canola Oil", "Carrots", "Cashews", "Cauliflower", "Celery",
    "Chia Seeds", "Chicken Breast", "Chickpeas", "Chili Flakes", "Chocolate Syrup",
    "Coconut Flour", "Coconut Water", "Condensed Milk", "Corn", "Cornbread Mix",
    "Couscous", "Cranberry Sauce", "Crayons", "Cream of Mushroom Soup", "Crushed Tomatoes",
    "Cucumber", "Currants", "Curry Powder", "Dill Pickles", "Eggplant", "English Muffins",
    "Feta Cheese", "Frosting", "Fruit Cups", "Fruit Snacks", "Gelato", "Granulated Sugar",
    "Green Beans", "Ground Turkey", "Ham", "Honey Mustard", "Horseradish", "Iced Tea",
    "Jalapenos", "Kale", "Kombucha", "Lamb Chops", "Lasagna", "Linguine", "Lobster",
    "Lollipops", "Macaroni", "Mandarin Oranges", "Mango", "Marinara Sauce", "Meatloaf",
    "Mozzarella Sticks", "Muesli", "Naan Bread", "Navy Beans", "Oat Milk", "Oysters",
    "Paprika", "Peach Slices", "Pecans", "Peppermint", "Pesto", "Pickled Onions",
    "Pine Nuts", "Plantain Chips", "Plum Sauce", "Pork Chops", "Poultry Seasoning",
    "Pretzel Sticks", "Prosciutto", "Pumpkin", "Quinoa", "Radishes", "Ranch Dip",
    "Red Beans", "Red Cabbage", "Romaine Lettuce", "Rosemary", "Rye Bread", "Sage",
    "Salsa Verde", "Sardine Oil", "Sausages", "Scallions", "Shiitake Mushrooms",
    "Shredded Cheese", "Smoked Salmon", "Snow Peas", "Sour Cream", "Split Peas",
    "Squash", "Sriracha", "Stewed Tomatoes", "String Beans", "Sweet Corn", "Sweet Potatoes",
    "Tabasco Sauce", "Tahini", "Tilapia", "Tomato Juice", "Tootsie Rolls", "Trout",
    "Turkey", "Turmeric", "Unbleached Flour", "Vanilla Pudding", "Veal", "Walnuts",
    "Wheat Germ", "Wonton Wrappers"
]

def find_duplicates(string_array):
    # Dictionary to store the count of each string
    string_count = {}
    duplicates = []

    # Iterate over the string array
    for string in string_array:
        # If the string is already in the dictionary, increment its count
        if string in string_count:
            string_count[string] += 1
        else:
            # If the string is not in the dictionary, add it with count 1
            string_count[string] = 1

    # Iterate over the dictionary to find duplicates
    for string, count in string_count.items():
        if count > 1:
            duplicates.append(string)

    # Print the duplicate strings
    if duplicates:
        print("Duplicate strings:", duplicates)
    else:
        print("No duplicate strings found.")

def generate_random_material_code():
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=6))

def generate_excel_column_names(n):
    letters = string.ascii_uppercase
    column_names = []

    # Generate single letter column names
    for letter in letters:
        column_names.append(letter)
        if len(column_names) == n:
            return column_names

    # Generate double letter column names
    for first_letter in letters:
        for second_letter in letters:
            column_names.append(first_letter + second_letter)
            if len(column_names) == n:
                return column_names

    return column_names

# Dummy data check
find_duplicates(goods)
# sys.exit(0)

# Generate 40 column names
column_names = generate_excel_column_names(40)

url = 'http://127.0.0.1:8085/wms/material/add'
headers = {
    'Content-Type': 'application/json'
}

idx = 1
for warehouse_id in range(1, 12):  # Loop through warehouse_id from 1 to 11
    for good_id in range (0, 40):
        name = goods[(warehouse_id - 1) * 40 + good_id]
        row = column_names[good_id]
        data = {
            "id": idx,
            "material_code": generate_random_material_code(),
            "name": name,
            "material_type": "stock material",  # Assuming all materials are of type "solid material"
            "unit": "box",
            "note": f"Note about {name}",
            "specification": f"Specification {idx}",
            "max": random.randint(100, 200),
            "min": random.randint(1, 10),
            "status": "Active",
            "expect_wh_id": warehouse_id,
            "expect_storage_locations": f"{row}-01,{row}-02,{row}-03,{row}-04,{row}-05"
        }
        idx = idx + 1
        print(data)
        response = requests.post(url, headers=headers, json=data)
        if response.status_code == 200:
            print(f"Successfully added {name}")
        else:
            print(f"Failed to add {name}, Status Code: {response.status_code}, Response: {response.text}")
