class OxygenController {
    log(req, res) {
        console.log(req.body);
        global.models.state.create({
            device_id: req.body.device_id,
            oxygen: req.body.oxygen,
            temp: req.body.temp
        });
        res.status(200).json({message: 'Logged', success: true});
    }
}

module.exports = OxygenController;