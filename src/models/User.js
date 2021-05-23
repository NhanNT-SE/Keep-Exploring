import mongoose from "mongoose";
const Schema = mongoose.Schema;

const user = new Schema(
  {
    email: {
      type: String,
      required: [true, `{PATH} is required`],
      match: [
        /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
        `{PATH}: {VALUE} is not a valid email address`,
      ],
    },
    username: {
      type: String,
      required: [true, "{PATH} is required"],
      minLength: [6, "username should contain atleast 6 characters"],
    },
    password: {
      type: String,
      required: [true, "{PATH} is required"],
      match: [
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/,
        "password should contain atleast 8 characters, 1 number, 1 special character , 1 upper and 1 lowercase",
      ],
    },
    fullName: {
      type: String,
      minLength: [6, "fullName password should contain atleast 6 characters"],
    },
    role: {
      type: String,
      enum: ["admin", "user"],
      default: "user",
      message: "{VALUE} is not supported",
    },

    imgUser: {
      type: String,
    },
    post: [
      {
        type: Schema.Types.ObjectId,
        ref: "post",
      },
    ],
    blog: [
      {
        type: Schema.Types.ObjectId,
        ref: "blog",
      },
    ],
    address: { type: String },
    bod: {
      type: Date,
    },
    gender: {
      type: String,
      enum: ["male", "female", "unknown"],
      default: "male",
      message: "{VALUE} is not supported",
    },
    created_on: {
      type: Date,
      default: Date.now,
    },
  },
  { collection: "user" }
);
user.index({ email: 1, username: 1 }, { unique: true });
export default mongoose.model("user", user);
