import mongoose from "mongoose";
const Schema = mongoose.Schema;

const UserSchema = new Schema(
  {
    email: {
      type: String,
      required: true,
      unique: true,
    },
    pass: {
      type: String,
      required: true,
    },
    displayName: {
      type: String,
      required: true,
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

// UserSchema.methods.joiValidate = function (obj) {
//     const Joi = require('@hapi/joi');
//     	const schema = {
// 		email: Joi.string().email().required(),
// 		pass: Joi.string().min(6).required(),
// 	};
// 	return Joi.validate(obj, schema);
// };

export default mongoose.model("User", UserSchema);
