class OxygenController {
    log(req, res) {
        consle.log(req.body);
        res.status(200).json({message: 'Logged', success: true});
    }
}

module.exports = OxygenController;