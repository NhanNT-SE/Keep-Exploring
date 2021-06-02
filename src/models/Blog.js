import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "user",
    },
    title: {
      type: String,
      required: true,
    },
    img: {
      type: String,
    },
    folder_storage: {
      type: String,
    },
    view_mode: {
      type: String,
      enum: ["public", "hidden", "friend"],
      default: "public",
    },
    status: {
      type: String,
      enum: ["pending", "done", "need_update"],
      default: "pending",
    },
    likes: [
      {
        type: Schema.Types.ObjectId,
        ref: "user",
      },
    ],
    created_on: {
      type: Date,
      default: Date.now,
    },
    last_modify: {
      type: Date,
    },
    comments: [
      {
        type: Schema.Types.ObjectId,
        ref: "comment",
      },
    ],
    content: {
      type: Schema.Types.ObjectId,
      ref: "content_blog",
    },
  },
  { collection: "blog" }
);
export const Blog = mongoose.model("blog", schema);
