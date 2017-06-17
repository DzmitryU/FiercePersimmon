var request = require('request');

class OxygenController {
    constructor() {
        this.log = (req, res) => {
            console.log(req.body);
            let quality = this.assess(req.body.carbon);
            if (quality >= 4) {
                this.alert(req.body.device_id);
            }
            global.models.state.create({
                device_id: req.body.device_id,
                carbon: req.body.carbon,
                temp: req.body.temp,
                quality: quality
            }).then((state) => {
                res.status(200).json({message: 'Logged', success: true});
            });
        };

        this.assess = (carbon) => {
            let value = 1;
            if (carbon >= 700) {
                value = 2;
            }
            if (carbon >= 800) {
                value = 3;
            }
            if (carbon >= 900) {
                value = 4;
            }
            if (carbon >= 1100) {
                value = 5;
            }

            return value;
        };

        this.get = (req, res) => {
            let access_token = req.get("Authorization");
            global.models.user.find({where: {access_token: access_token}}).then((user) => {
                console.log(user.room);
                global.models.device.find({where: {room: user.room}}).then((device) => {
                    console.log(device.id);
                    global.models.state.find({where: {device_id: device.id}, order: [['time', 'DESC']]}).then((result) => {
                        console.log(result.id);
                        res.status(200).json({message: 'Found', success: true, body: {room: user.room, state: result}});
                    });
                });
            });
        };

        this.stat = (req, res) => {
            let access_token = req.get("Authorization");
            global.models.user.find({where: {access_token: access_token}}).then((user) => {
                console.log(user.room);
                global.models.device.find({where: {room: user.room}}).then((device) => {
                    console.log(device.id);
                    global.models.state.find({where: {device_id: device.id}, order: [['time', 'DESC']]}).then((result) => {
                        console.log(result.id);
                        res.status(200).json({message: 'Found', success: true, body: {room: user.room, state: result}});
                    });
                });
            });
        };

        this.alert = (device_id) => {
            global.models.device.find({where: {id: device_id}}).then((device) => {
                console.log(device.room);
                global.models.user.findAll({where: {room: device.room, refresh_token: {$ne: null}}}).then((users) => {
                    if (users) {
                        users.forEach((user) => {
                            console.log(user.refresh_token);
                            // Set the headers
                            var headers = {
                                'User-Agent':       'Super Agent/0.0.1',
                                'Content-Type':     'application/x-www-form-urlencoded'
                            };

                            // Configure the request
                            var options = {
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

module.exports = OxygenController;