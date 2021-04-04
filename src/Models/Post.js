const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const PostSchema = new Schema(
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
      // required: true,
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

    // address: {
    // 	type: Schema.Types.ObjectId,
    // 	ref: 'Address',
    // 	required: true,
    // },
	
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
        ref: "User",
      },
    ],

    created_on: {
      type: Date,
      default: Date.now(),
    },

    comment: [
      {
        type: Schema.Types.ObjectId,
        ref: "Comment",
      },
    ],
  },
  { collection: "Post" }
);

module.exports = mongoose.model("Post", PostSchema);
