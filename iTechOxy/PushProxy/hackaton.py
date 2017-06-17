from flask import Flask, jsonify, request
import os

from push_service.PushService import get_push_service

app = Flask(__name__)


@app.route('/notify', methods=['POST'])
def notify():
    ids_set = request.form['ids'].split(',')
    message = request.form['message']
    is_success = False

    module_dir = os.path.dirname(__file__)
    push_service = get_push_service(os.path.join(module_dir, 'firebase_config'))

    if push_service is not None:
        is_success, response = push_service.notify(reg_ids=ids_set, message=message)
    else:
        response = 'Internal server error.'

    if not is_success:
        return jsonify({'message': response})

    return jsonify(response)

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=False)
