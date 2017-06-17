from pyfcm import FCMNotification
from configparser import ConfigParser


class PushService:
    def __init__(self, api_key, default_message_title):
        self.gcm = FCMNotification(api_key=api_key)
        self.default_message_title = default_message_title

    def notify(self, reg_ids, message):
        try:
            response = self.gcm.notify_multiple_devices(registration_ids=reg_ids,
                                                        message_body=message,
                                                        message_title=self.default_message_title)

            return True, response
        except BaseException as e:
            # TODO: Log an exception
            return False, str(e)


def get_push_service(path_to_config='firebase_config'):
    config = ConfigParser()

    if len(config.read(path_to_config)) > 0:
        api_key = config.get('main', 'api_key')
        default_message_title = config.get('main', 'default_message_title')

        return PushService(api_key, default_message_title)