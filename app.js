require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
const passport = require("passport");
const cors = require("cors");
const userRouter = require("./src/Route/UserRoute");
const postRouter = require("./src/Route/PostRoute");
const addressRouter = require("./src/Route/AddressRoute");
const commentRouter = require("./src/Route/CommentRoute");
const apiDocRouter = require("./src/Route/APIDocsRoute");
const blogRouter = require("./src/Route/BlogRoute");
const AuthController = require("./src/Controllers/AuthController");
const jwtHelper = require("./src/middleware/jwtHelper");
// const mongoString = 'mongodb+srv://admin:doanhnhangroup@cluster0.jqsm5.mongodb.net/user?retryWrites=true&w=majority';
const mongoString =
  "mongodb://nhannt:nhannt1905@13.58.149.178:27017/keep-exploring?authSource=admin&w=1";
const port = process.env.PORT || 3000;
const app = express();
app.use(express.static("src/public"));
app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
);
app.use(passport.initialize());
app.use(cors());
//Routers

app.post("/login", AuthController.login);
app.post("/refresh-token", AuthController.refreshToken);
app.post("/logout", AuthController.logout);
app.use(jwtHelper.isAuth);
app.get("/testAuth", (req, res, next) => {
  try {
    const friends = [
      {
        name: "Cat: Russian Blue",
      },
      {
        name: "Cat: Maine Coon",
      },
      {
        name: "Cat: Balinese",
      },
    ];
    return res.status(200).send({ data: friends });
  } catch (error) {
    next(error);
  }
});
//
app.use("/api-doc", apiDocRouter);
app.use("/user", userRouter);
app.use("/post", postRouter);
app.use("/address", addressRouter);
app.use("/comment", commentRouter);
app.use("/blog", blogRouter);

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

app.use((req, res, next) => {
  const error = new Error("Not found");
  error.status = 404;
  next(error);
});
app.use((error, req, res, next) => {
  res.status(error.status || 500);
  res.send({
    error: {
      status: error.status || 500,
      message: error.message,
    },
  });
});

app.listen(port, console.log(`start on port ${port}`));
