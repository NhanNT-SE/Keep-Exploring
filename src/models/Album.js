import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    post: {
      type: Schema.Types.ObjectId,
      ref: "post",
      required: true,
    },
    category: {
      type: String,
      required: true,
      enum: ["food", "hotel", "check_in"],
      default: "food",
    },
    title: {
      required: true,
      type: String,
    },
    desc: {
      required: true,
      type: String,
    },
    imgs: {
      type: [String],
    },
    address: {
      type: String,
      lowercase: true,
      required: true,
    },
    rating: {
      type: Number,
      min: 0,
      max: 5,
      required: true,
    },
    created_on: {
      type: Date,
      default: Date.now(),
    },
    last_modify: {
      type: Date,
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
  },
  { collection: "album" }
);
export const Album = mongoose.model("album", schema);
