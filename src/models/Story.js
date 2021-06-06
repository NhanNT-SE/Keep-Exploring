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
  },
  { collection: "story" }
);
export const Story = mongoose.model("story", schema);
