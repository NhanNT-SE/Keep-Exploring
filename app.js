require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
const http = require("http");
const passport = require("passport");
const cors = require("cors");
const { Server } = require("socket.io");
const { isAuth, isAdmin } = require("./src/middleware/jwtHelper");
// ----------DEFINE ROUTER----------
const addressRouter = require("./src/Route/AddressRoute");
const adminRouter = require("./src/Route/AdminRoute");
const apiDocRouter = require("./src/Route/APIDocsRoute");
const authRouter = require("./src/Route/AuthRouter");
const blogRouter = require("./src/Route/BlogRoute");
const commentRouter = require("./src/Route/CommentRoute");
const postRouter = require("./src/Route/PostRoute");
const notifyRouter = require("./src/Route/NotificationRoute");
const publicRouter = require("./src/Route/PublicRoute");
const userRouter = require("./src/Route/UserRoute");

// ----------ROUTER----------
const mongoString =
  "mongodb://keepExploringUser:keepExploringUser@13.58.149.178:27017/keep-exploring?authSource=keep-exploring&w=1";
const port = process.env.PORT || 3000;
const app = express();
const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: "*",
  },
});

app.use(express.static("src/public"));
app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
);
app.use(passport.initialize());
app.use(cors());
// ----------CONNECT MONGO DB----------
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
io.on("connection", (socket) => {
  console.log("User connected");
  socket.on("disconnect", () => console.log("User has disconnected"));
});
app.use((req, res, next) => {
  req.io = io;
  next();
});
// ----------PUBLIC ROUTER----------
app.use("/api-doc", apiDocRouter);
app.use("/auth", authRouter);
app.use("/public", publicRouter);
// ----------AUTH ROUTER----------
app.use(isAuth);
app.use("/blog", blogRouter);
app.use("/comment", commentRouter);
app.use("/notification", notifyRouter);
app.use("/post", postRouter);
app.use("/user", userRouter);
// ----------ADMIN ROUTER----------
app.use(isAdmin);
app.use("/admin", adminRouter);
app.use("/address", addressRouter);
// ----------HANDLER ERROR----------
app.use((req, res, next) => {
  const error = new Error("Not found");
  error.status = 404;
  next(error);
});
app.use((error, req, res, next) => {
  res.status(error.status || 500);
  if (error.payload) {
    return res.send({
      error: {
        status: error.status || 500,
        message: error.message,
        payload: error.payload ? error.payload : "",
      },
    });
  }
  res.send({
    error: {
      status: error.status || 500,
      message: error.message,
    },
  });
});
server.listen(port, console.log(`start on port ${port}`));
// Comment ci cd
