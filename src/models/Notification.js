import mongoose from "mongoose";
const Schema = mongoose.Schema;

const notification = new Schema(
  {
    idUser: {
      type: Schema.Types.ObjectId,
      ref: "user",
    },
    status: {
      type: String,
      enum: ["new", "seen"],
      default: "new",
    },
    idPost: {
      type: Schema.Types.ObjectId,
      ref: "post",
    },
    idBlog: {
      type: Schema.Types.ObjectId,
      ref: "blog",
    },
    statusPost: { type: String },
    statusBlog: { type: String },
    content: {
      type: String,
      enum: ["like", "comment", "moderated", "unmoderated"],
    },
    contentAdmin: {
      type: String,
    },
    created_on: {
      type: Date,
      default: Date.now,
    },
  },
  { collection: "notification" }
);

export default mongoose.model("notification", notification);
