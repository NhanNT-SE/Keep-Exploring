import mongoose from "mongoose";
const Schema = mongoose.Schema;

const token = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "user",
      index: true,
      unique: true,
    },
    accessToken: {
      type: String,
    },
    refreshToken: {
      type: String,
    },
  },

  { collection: "token" }
);

export default mongoose.model("token", token);
