const AlertService = require('../../services/alert');

class OxygenController {
    constructor() {
        this.alertService = new AlertService();
        this.log = (req, res) => {
            console.log(req.body);
            let quality = this.assess(req.body.carbon);
            if (quality >= 4) {
                this.alertService.alert(req.body.device_id);
            }
            models.state.create({
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
            models.user.find({where: {access_token: access_token}}).then((user) => {
                console.log(user.room);
                models.device.find({where: {room: user.room}}).then((device) => {
                    console.log(device.id);
                    models.state.find({where: {device_id: device.id}, order: [['time', 'DESC']]}).then((result) => {
                        console.log(result.id);
                        res.status(200).json({message: 'Found', success: true, body: {room: user.room, state: result}});
                    });
                });
            });
        };

        this.stat = (req, res) => {
            let access_token = req.get("Authorization");
            models.user.find({where: {access_token: access_token}}).then((user) => {
                console.log(user.room);
                models.device.find({where: {room: user.room}}).then((device) => {
                    console.log(device.id);
                    models.state.findAll({where: {device_id: device.id}, limit: Number(req.query.number),
                        order: [['time', 'DESC']]}).then((result) => {
                        console.log(result.id);
                        res.status(200).json({message: 'Found', success: true, body: {room: user.room, states: result}});
                    });
                });
            });
        };
    }
}

module.exports = OxygenController;