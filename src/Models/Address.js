const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const addressSchema = new Schema(
  {
    province: {
      type: String,
      lowercase: true,
      required: true,
      unique: true,
      index: true,
    },
    district: [
      {
        type: Object,
        name: { type: String, lowercase: true, unique: true },
        ward: [{ type: String, lowercase: true, unique: true }],
      },
    ],
  },
  { collection: "Address" }
);

module.exports = mongoose.model("Address", addressSchema);
