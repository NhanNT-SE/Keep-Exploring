import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
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

  { collection: "tokens" }
);
export const Token = mongoose.model("token", schema);
