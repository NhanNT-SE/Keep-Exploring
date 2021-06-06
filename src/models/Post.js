import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "User",
      required: true,
    },
    album: {
      type: Schema.Types.ObjectId,
      ref: "album",
      required: function () {
        return this.type === "album";
      },
    },
    story: {
      type: Schema.Types.ObjectId,
      ref: "story",
      required: function () {
        return this.type === "story";
      },
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
    },
    view_mode: {
      type: String,
      enum: ["public", "hidden", "friends"],
      default: "public",
    },
  },
  { collection: "post" }
);
export const Post = mongoose.model("post", schema);
