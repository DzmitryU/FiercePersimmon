class UserController {
    constructor(models) {
        this.models = models;
    }
    login(req, res) {
        console.log(req.body);

        res.status(200).json({message: 'Signed in!', success: true, body: {access_token: "sosi lalka"}});
    }
    updateToken(req, res) {
        console.log(req.body);

        res.status(200).json({message: 'Saved', success: true});
    }
}

module.exports = UserController;