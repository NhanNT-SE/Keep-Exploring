const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const BlogDetail_Schema = new Schema(
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
  { collection: "Blog_Detail" }
);

module.exports = mongoose.model("Blog_Detail", BlogDetail_Schema);
