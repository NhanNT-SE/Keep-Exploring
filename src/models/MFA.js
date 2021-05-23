import mongoose from "mongoose";
const Schema = mongoose.Schema;

const MFASchema = new Schema(
  {
    owner: {
      type: Schema.Types.ObjectId,
      ref: "user",
      index: true,
      unique: true,
    },
    enableMFA: { type: Boolean, default: false },
    secretMFA: { type: String },
  },

  { collection: "mfa" }
);

export default mongoose.model("mfa", MFASchema);
