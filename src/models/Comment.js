import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    idPost: {
      type: Schema.Types.ObjectId,
      ref: "post",
    },
    idBlog: {
      type: Schema.Types.ObjectId,
      ref: "blog",
    },
    idUser: {
      type: Schema.Types.ObjectId,
      ref: "user",
      required: true,
    },
    content: {
      type: String,
      required: true,
    },
    date: {
      type: Date,
      default: Date.now(),
    },
    img: {
      type: String,
    },
  },
  { collection: "comment" }
);
export const Comment = mongoose.model("comment", schema);