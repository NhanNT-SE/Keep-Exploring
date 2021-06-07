import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "user",
      index: true,
      unique: true,
      immutable: true,
    },
    avatar: {
      type: String,
    },
    address: { type: String },
    birthday: {
      type: Date,
    },
    fullName: {
      type: String,
      minLength: [6, "fullName password should contain atleast 6 characters"],
    },
    gender: {
      type: String,
      enum: ["male", "female", "unknown"],
      default: "male",
      message: "{VALUE} is not supported",
    },
    phoneNumber: {
      type: String,
    },
  },
  { collection: "basicInfo" }
);

export const BasicInfo = mongoose.model("basicInfo", schema);
