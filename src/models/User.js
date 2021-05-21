import mongoose from "mongoose";
const Schema = mongoose.Schema;

const UserSchema = new Schema(
  {
    email: {
      type: String,
      index: true,
      required: [true, "email is required"],
      unique: true,
      match: [/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/, "invalid email"],
    },
    username: {
      type: String,
      index: true,
      unique: true,
      required: [true, "username is required"],
      minLength: [6, "min username"],
    },
    password: {
      type: String,
      required: [true, "password  is required"],
      match: [
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/,
        "invalid password",
      ],
    },
    fullName: {
      type: String,
      minLength: [6, "min fullName"],
    },
    role: {
      type: String,
      enum: ["admin", "user"],
      default: "user",
    },
    enableMFA: { type: Boolean, default: false },
    secretMFA: { type: String },
    imgUser: {
      type: String,
    },
    post: [
      {
        type: Schema.Types.ObjectId,
        ref: "Post",
      },
    ],
    blog: [
      {
        type: Schema.Types.ObjectId,
        ref: "Blog",
      },
    ],
    address: { type: String },
    bod: {
      type: Date,
    },
    gender: {
      type: String,
      enum: ["male", "female"],
      default: "male",
    },
    created_on: {
      type: Date,
      default: Date.now,
    },
  },
  { collection: "User" }
);

export default mongoose.model("User", UserSchema);
