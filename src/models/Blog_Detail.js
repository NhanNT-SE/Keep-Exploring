import mongoose from "mongoose";
const Schema = mongoose.Schema;

const ContentBlog = new Schema(
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

export default mongoose.model("content_blog", ContentBlog);
