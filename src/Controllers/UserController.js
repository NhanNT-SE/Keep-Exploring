const User = require("../Models/User");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");

const { JWT_SECRET } = require("../config/index");
var userRole;

const checkRoleAdmin = async (req, res) => {
  try {
    const { token } = req.cookies;

    jwt.verify(token, JWT_SECRET, (err, data) => {
      if (err) {
        return res.status(500).send(err.message);
      }
      checkRole(data.id).then(() => {
        if (userRole == "admin") {
          return res.json("thanh cong vao admin page");
        }
        return res.json("ban khong co quyen admin");
      });
    });
  } catch (error) {
    return res.status(500).send(error.message);
  }
};

const signIn = async (req, res) => {
  try {
    console.log("sign-in");
    const { email, pass } = req.body;
    const user = await User.findOne({ email });

    if (user) {
      const checkPass = await bcrypt.compare(pass, user.pass);
      if (checkPass) {
        // Synchronous Sign with default (HMAC SHA256)
        var token = await jwt.sign({ id: user._id }, JWT_SECRET, {
          expiresIn: "1h",
        });

        //Set Header authorization
        res.setHeader("Authorization", "Bearer " + token);
        return res.status(200).send({
          status: 200,
          data: `Bearer ${token}`,
          message: "Login successfully",
          error: null,
        });
      }
      return res.send({
        status: 202,
        data: null,
        message: "Your password incorrect",
        error: null,
      });
    }
    return res.send({
      status: 202,
      data: null,
      msg: "This user not exists",
    });
  } catch (e) {
    console.log(e);
    return res.status(500).send("loi server:" + e.message);
  }
};

const signUp = async (req, res) => {
  try {
    const file = req.file;
    var avatar;
    if (file) {
      avatar = file.filename;
    } else {
      avatar = "avatar-default.png";
    }

    const user = req.body;
    //Validate by joi
    // const uservalidate = new User(user)
    // var err = uservalidate.joiValidate(user);
    // if (err)
    // {
    // 	return res.json(err);
    // }

    const userFound = await User.findOne({ email: user.email });
    if (userFound) {
      return res.status(201).send("Tai khoan da ton tai");
    }

    const salt = await bcrypt.genSalt(10);
    const passHashed = await bcrypt.hash(user.pass, salt);

    var newUser = {
      ...req.body,
      pass: passHashed,
    };

    // console.log(newUser);
    await new User(newUser).save();
    return res.status(200).send("Tao tai khoan thanh cong");
  } catch (e) {
    return res.status(202).send(e.message);
  }
};

const checkRole = async (idUser) => {
  try {
    const user = await User.findById(idUser);
    return (userRole = user.role);
  } catch (error) {
    return error.message;
  }
};

module.exports = {
  signIn,
  signUp,
  checkRoleAdmin,
};
