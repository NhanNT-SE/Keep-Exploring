import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "user",
      required: true,
    },
    album: {
      type: Schema.Types.ObjectId,
      ref: "album",
    },
    story: {
      type: Schema.Types.ObjectId,
      ref: "story",
    },
    type: {
      type: String,
      enum: ["album", "story"],
      default: "album",
    },
    status: {
      type: String,
      enum: ["pending", "done", "need_update"],
      default: "pending",
      message: "{VALUE} is not supported",
    },
    view_mode: {
      type: String,
      enum: ["public", "hidden", "friends"],
      default: "public",
      message: "{VALUE} is not supported",
    },
    likes: [
      {
        type: Schema.Types.ObjectId,
        ref: "user",
      },
    ],
    comments: [
      {
        type: Schema.Types.ObjectId,
        ref: "comment",
      },
    ],
    created_on: {
      type: Date,
      default: Date.now(),
    },
    last_modify: {
      type: Date,
      default: Date.now(),
    },
  },
  { collection: "posts" }
);
export const Post = mongoose.model("post", schema);
