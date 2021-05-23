import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "User",
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

    min_price: {
      type: Number,
      default: 0,
      required: true,
    },

    max_price: {
      type: Number,
      default: 0,
      required: true,
    },
    address: {
      type: String,
      lowercase: true,
      required: true,
    },
    status: {
      type: String,
      enum: ["pending", "done", "need_update"],
      default: "pending",
      required: true,
    },

    rating: {
      type: Number,
      min: 0,
      max: 5,
      required: true,
    },

    like_list: [
      {
        type: Schema.Types.ObjectId,
        ref: "user",
      },
    ],

    created_on: {
      type: Date,
      default: Date.now(),
    },

    comment: [
      {
        type: Schema.Types.ObjectId,
        ref: "comment",
      },
    ],
  },
  { collection: "post" }
);
export const Post = mongoose.model("post", schema);
