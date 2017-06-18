from usb import core


class UsbDescriptor:
    def __init__(self):
        self.device = core.find(idVendor=0x04D9, idProduct=0xA052)
        self.interface = 0

    def get_serial_number(self):
        return self.device.idProduct
