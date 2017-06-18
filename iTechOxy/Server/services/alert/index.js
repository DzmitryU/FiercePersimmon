var request = require('request');

class AlertService {
    constructor() {
        this.alert = (device_id) => {
            models.device.find({where: {id: device_id}}).then((device) => {
                console.log(device.room);
                models.user.findAll({where: {room: device.room, refresh_token: {$ne: null}}}).then((users) => {
                    if (users) {
                        users.forEach((user) => {
                            console.log(user.refresh_token);
                            // Set the headers
                            let headers = {
                                'User-Agent':       'Super Agent/0.0.1',
                                'Content-Type':     'application/x-www-form-urlencoded'
                            };

                            // Configure the request
                            let options = {
                                url: 'http://ec2-52-23-254-168.compute-1.amazonaws.com:5000/notify',
                                method: 'POST',
                                headers: headers,
                                form: {'ids': user.refresh_token, 'message': 'High Carbon dioxide level!'}
                            };

                            // Start the request
                            request(options, function (error, response, body) {
                            })
                        });
                    }
                });
            });
        }
    }
}

module.exports = AlertService;