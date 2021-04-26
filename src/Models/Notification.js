const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const NotificationSchema = new Schema(
  {
    idUser: {
      type: Schema.Types.ObjectId,
      ref: "User",
    },
    status: {
      type: String,
      enum: ["new", "seen"],
      default: "new",
    },
    idPost: {
      type: Schema.Types.ObjectId,
      ref: "Post",
    },
    idBlog: {
      type: Schema.Types.ObjectId,
      ref: "Blog",
    },
    statusPost:{type: String},
    statusBlog:{type: String},
    content: {
      type: String,
      enum: ["like", "comment", "moderated", "unmoderated"],
    },
    contentAdmin: {
      type: String,
    },
    created_on: {
      type: Date,
      default: Date.now,
    },
  },
  { collection: "Notification" }
);

module.exports = mongoose.model("Notification", NotificationSchema);
