import express from "express";
import mongoose from "mongoose";
import http from "http";
import cors from "cors";
import { Server } from "socket.io";
import { isAuth, isAdmin } from "./src/helpers/JWTHelper.js";

// ----------DEFINE ROUTER----------
import apiDocs from "./src/docs/APIDocs.js";
import auth from "./src/routes/auth/AuthRouter.js";
import adminAddress from "./src/routes/admin/AdminAddress.js";
import adminPost from "./src/routes/admin/AdminPost.js";
import adminUser from "./src/routes/admin/AdminUser.js";
import adminStatistic from "./src/routes/admin/AdminStatistic.js";
import mfa from "./src/routes/auth/MFARouter.js";
import publicAddress from "./src/routes/public/PublicAddress.js";
import publicPost from "./src/routes/public/PublicPost.js";
import userComment from "./src/routes/user/UserComment.js";
import userPost from "./src/routes/user/UserPost.js";
import userProfile from "./src/routes/user/UserProfile.js";
import userNotify from "./src/routes/user/UserNotify.js";
import { mapErrorMessage } from "./src/helpers/CustomError.js";

// ----------ROUTER----------
// const mongoString =
//   "mongodb://keepExploringUser:keepExploringUser@13.58.149.178:27017/keep-exploring?authSource=keep-exploring&w=1";
const mongoString =
  "mongodb://localhost:27017,localhost:27018,localhost:27019/keep-exploring";
const port = process.env.PORT || 3000;
const app = express();
const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: "*",
  },
});

app.use(express.static("src/public"));
app.use(express.json());
app.use(
  express.urlencoded({
    extended: true,
  })
);
app.use(cors());
// ----------CONNECT MONGO DB----------
(async function () {
  try {
    await mongoose.connect(mongoString, {
      useUnifiedTopology: true,
      useNewUrlParser: true,
      useCreateIndex: true,
      useFindAndModify: false,
    });
    console.log("Connected to database ");
  } catch (error) {
    console.log(`Error connecting to the database. \n${error}`);
  }
})();
io.on("connection", (socket) => {
  console.log("User connected");
  socket.on("disconnect", () => console.log("User has disconnected"));
});
app.use(async (req, res, next) => {
  try {
    const session = await mongoose.startSession();
    const opts = { session, returnOriginal: false };
    req.io = io;
    req.session = session;
    req.opts = opts;
    next();
  } catch (error) {
    next(error);
  }
});
// ----------PUBLIC ROUTER----------
app.use("/api-doc", apiDocs);
app.use("/auth", auth);
app.use("/public/address", publicAddress);
app.use("/public/post", publicPost);
// // ----------PROTECT ROUTER----------
app.use(isAuth);
app.use("/mfa", mfa);
app.use("/user/comment", userComment);
app.use("/user/post", userPost);
app.use("/user/profile", userProfile);
app.use("/user/notify", userNotify);
// ----------ADMIN ROUTER----------
app.use(isAdmin);
app.use("/admin/address", adminAddress);
app.use("/admin/post", adminPost);
app.use("/admin/user", adminUser);
app.use("/admin/statistics", adminStatistic);
// ----------HANDLER ERROR----------
app.use((req, res, next) => {
  const error = new Error("Not found");
  error.status = 404;
  next(error);
});
app.use((error, req, res, next) => {
  const message = mapErrorMessage(error);
  // const message = error.message;
  const status = error.status || 500;
  res.status(status);
  let err = {
    status,
    message,
  };
  if (error.payload) {
    err.payload = error.payload;
  }
  res.send({
    error: {
      ...err,
    },
  });
});
server.listen(port, console.log(`start on port ${port}`));
