class UserController {
    constructor() {
        this.login = (req, res) => {
            console.log(req.body);
            models.user.find({where: {name: req.body.login}}).then((user) => {
                if (user) {
                    res.status(200).json({message: 'Signed in!', success: true, body: {access_token: user.access_token}});
                } else {
                    res.status(404).json({message: 'User not found in SMG', success: false});
                }
            })
                .catch((ex) => {
                    console.log(ex);
                    res.status(500).json({message: ex, success: false});
                });
        };
        this.updateToken = (req, res) => {
            console.log(req.body);
            let access_token = req.get("Authorization");
            console.log(access_token);

            if (!req.body.refresh_token || !access_token) {
                res.status(200).json({message: 'Token Was not updated', success: false})
            } else {
                models.user.find({where: {access_token: access_token}}).then((user) => {
                    user.update({refresh_token: req.body.refresh_token})
                        .then(() => res.status(200).json({message: 'Token Updated', success: true}));
                });
            }
        }
    }
}

module.exports = UserController;