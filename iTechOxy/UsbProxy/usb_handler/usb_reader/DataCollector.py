import requests
from requests.exceptions import RequestException


class DataCollector:
    def __init__(self, endpoint_url):
        self.endpoint_url = endpoint_url

    def send_devices_values(self, payload):
        try:
            requests.put(self.endpoint_url, data=payload)
        except RequestException:
            pass
            # TODO: Log the error.
        else:
            print(payload)

