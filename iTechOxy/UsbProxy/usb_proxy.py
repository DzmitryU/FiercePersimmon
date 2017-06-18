import subprocess
import time
from argparse import ArgumentParser

from usb_handler.usb_reader.DataCollector import DataCollector
from usb_handler.usb_reader.UsbDescriptor import UsbDescriptor


def get_value(raw_string):
    formatted_str = str(raw_string, 'utf-8').rstrip()
    result = formatted_str.split('\t')

    return tuple(result)

def run_loop():
    process = subprocess.Popen(["sudo", "./usb_handler/build/co2mond/co2mond"], stdout=subprocess.PIPE)
    collector = DataCollector('http://ec2-52-23-254-168.compute-1.amazonaws.com:3000/api/v1/oxygen')
    device_descriptor = UsbDescriptor()
    payload = {'device_id': device_descriptor.get_serial_number()}

    while True:
        time.sleep(5)

        key1, value1 = get_value(process.stdout.readline())
        key2, value2 = get_value(process.stdout.readline())

        if key2 == 'Tamb':
            value1, value2 = value2, value1

        payload['temp'] = value1
        payload['carbon'] = value2

        collector.send_devices_values(payload)

if __name__ == '__main__':
    run_loop()