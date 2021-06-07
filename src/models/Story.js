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
    content: [
      {
        type: Object,
        img: String,
        desc: String,
        file_name: String,
      },
    ],
  },
  { collection: "story" }
);
export const Story = mongoose.model("story", schema);
