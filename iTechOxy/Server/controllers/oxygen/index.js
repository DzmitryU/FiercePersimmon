class OxygenController {
    constructor(models) {
        this.moduls = models;
    }
    log(req, res) {
        console.log(req.body);
        res.status(200).json({message: 'Logged', success: true});
    }
}

module.exports = OxygenController;