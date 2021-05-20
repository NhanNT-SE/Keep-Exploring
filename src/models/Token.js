import mongoose from "mongoose";
const Schema = mongoose.Schema;

const TokenSchema = new Schema(
  {
    _id: { type: String },
    accessToken: {
      type: String,
    },
    refreshToken: {
      type: String,
    },
  },

  { collection: "Token" }
);

export default mongoose.model("Token", TokenSchema);
