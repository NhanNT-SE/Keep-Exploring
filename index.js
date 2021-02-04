require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const passport = require("passport");

// const userRouter = require('./src/Route/UserRoute');
// const postRouter = require('./src/Route/PostRoute');
// const addressRouter = require('./src/Route/AddressRoute');
// const commentRouter = require('./src/Route/CommentRoute');

// const mongoString = 'mongodb+srv://admin:doanhnhangroup@cluster0.jqsm5.mongodb.net/user?retryWrites=true&w=majority';
const mongoString = "mongodb://nhannt:nhannt1905@13.58.149.178:27017/";
const port = process.env.PORT || 3000;
const app = express();

app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
);
app.use(cookieParser());
app.use(passport.initialize());

//Routers
// app.use('/user', userRouter);
// app.use('/post', postRouter);
// app.use('/address', addressRouter);
// app.use('/comment', commentRouter);

//Kết nối với mongo database
mongoose
  .connect(mongoString, {
    useUnifiedTopology: true,
    useNewUrlParser: true,
    useCreateIndex: true,
    useFindAndModify: false,
  })
  .then(() => {
    console.log("Connected to database ");
  })
  .catch((err) => {
    console.error(`Error connecting to the database. \n${err}`);
  });
//   Test deploy
app.get("/", (req, res) => {
  return res.send({
    "app-name": "Keep Exploring",
    "server-db": "13.58.149.178:27017",
    "server-nodejs":"http://keep-exploring-backend.eba-naub3qt7.us-east-2.elasticbeanstalk.com/",
    "server-react":"",
  });
});
//   Test deploy
app.listen(port, console.log(`start on port ${port}`));
