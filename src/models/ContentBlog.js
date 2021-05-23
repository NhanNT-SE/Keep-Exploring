import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
  {
    _id: {
      type: String,
    },
    detail_list: [
      {
        type: Object,
        img: String,
        content: String,
        file_name: String,
      },
    ],
  },
  { collection: "content_blog" }
);
export const ContentBlog = mongoose.model("content_blog", schema);
