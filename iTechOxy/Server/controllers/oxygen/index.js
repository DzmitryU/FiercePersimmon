class OxygenController {
    constructor() {
        this.log = (req, res) => {
            console.log(req.body);
            global.models.state.create({
                device_id: req.body.device_id,
                carbon: req.body.carbon,
                temp: req.body.temp,
                quality: this.assess(req.body.carbon)
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
                        res.status(200).json({message: 'Found', success: true, body: result});
                    });
                });
            })
        }
    }
}

module.exports = OxygenController;