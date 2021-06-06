import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    post: {
      type: Schema.Types.ObjectId,
      ref: "post",
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
    content: [
      {
        type: Object,
        img: String,
        content: String,
        file_name: String,
      },
    ],
    created_on: {
      type: Date,
      default: Date.now,
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
  { collection: "story" }
);
export const Story = mongoose.model("story", schema);
