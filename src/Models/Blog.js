const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const BlogSchema = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "User",
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
        ref: "User",
      },
    ],
    created_on: {
      type: Date,
      default: Date.now,
    },
    comments: [
      {
        type: Schema.Types.ObjectId,
        ref: "Comment",
      },
    ],
    blog_detail: {
      type: Schema.Types.ObjectId,
      ref: "Blog_Detail",
    },
  },
  { collection: "Blog" }
);

module.exports = mongoose.model("Blog", BlogSchema);
