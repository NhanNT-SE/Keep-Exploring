import mongoose from "mongoose";
const Schema = mongoose.Schema;

const schema = new Schema(
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
  { collection: "address" }
);

export const Address = mongoose.model("address", schema);
