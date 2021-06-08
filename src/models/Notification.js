import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    owner: {
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
    type: {
      type: String,
      enum: ["like", "comment", "done", "pending", "need_update", "system"],
    },
    content: {
      type: String,
    },
    created_on: {
      type: Date,
      default: Date.now,
    },
  },
  { collection: "notifications" }
);
export const Notification = mongoose.model("notification", schema);
