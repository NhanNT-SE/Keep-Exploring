import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    email: {
      type: String,
      required: [true, `{PATH} is required`],
      index: true,
      unique: true,
      match: [
        /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
        `{PATH}: {VALUE} is not a valid email address`,
      ],
    },
    username: {
      type: String,
      index: true,
      unique: true,
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
    displayName: {
      type: String,
      minLength: [6, "fullName password should contain atleast 6 characters"],
    },
    role: {
      type: String,
      enum: ["admin", "user"],
      default: "user",
      message: "{VALUE} is not supported",
    },
    avatar: {
      type: String,
    },

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
    userInfo: {
      type: Schema.Types.ObjectId,
      ref: "userInfo",
    },
  },
  { collection: "user" }
);
export const User = mongoose.model("user", schema);
