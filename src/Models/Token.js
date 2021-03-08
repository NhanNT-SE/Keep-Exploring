const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const TokenSchema = new Schema(
  {
    _id: String,
    accessToken: String,
    refreshToken: String,
  },
  { collection: "Token", _id: false }
);
module.exports = mongoose.model("Token", TokenSchema);
