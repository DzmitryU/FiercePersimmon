class UserController {
    login(req, res) {
        console.log(req.body);
        global.models.user.find({where: {name: req.body.login}}).then((user) => {
            res.status(200).json({message: 'Signed in!', success: true, body: {access_token: user.access_token}});
        });
    }
    updateToken(req, res) {
        console.log(req.body);

        res.status(200).json({message: 'Saved', success: true});
    }
}

module.exports = UserController;