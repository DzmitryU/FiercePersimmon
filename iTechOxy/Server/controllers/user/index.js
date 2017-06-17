class UserController {
    login(req, res) {
        console.log(req.body);
        global.models.user.find({where: {name: req.body.login}}).then((user) => {
            res.status(200).json({message: 'Signed in!', success: true, body: {access_token: user.access_token}});
        });
    }
    updateToken(req, res) {
        let access_token = req.get("Authorization");
        console.log(access_token);

        global.models.user.find({where: {access_token: access_token}}).then((user) => {
            user.update({refresh_token: req.body.refresh_token})
                .then(() => res.status(200).json({message: 'Token Updated', success: true}));
        });
    }
}

module.exports = UserController;