const mongoose = require("mongoose");
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

module.exports = mongoose.model("Token", TokenSchema);
