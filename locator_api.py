from flask import Flask, request, jsonify
import mysql.connector

app = Flask(__name__)

def get_db_connection():
    return mysql.connector.connect(
        host="localhost",
        user="Arnav",
        password="13975",
        database="bank_locator"
    )

@app.route('/getBanks', methods=['GET'])
def get_banks():
    city = request.args.get('city')
    if not city:
        return jsonify({"error": "City parameter is required"}), 400

    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)
    query = "SELECT address FROM bank_locations WHERE city = %s"
    cursor.execute(query, (city,))
    results = cursor.fetchall()
    cursor.close()
    connection.close()

    if not results:
        return jsonify({"message": "No banks found in this city"}), 404

    return jsonify(results)

if __name__ == '__main__':
    app.run(debug=True)

