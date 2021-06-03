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
    status: {
      type: String,
      enum: ["idle", "enable", "disable"],
      default: "idle",
    },
    secretMFA: { type: String },
  },

  { collection: "mfa" }
);
export const MFA = mongoose.model("mfa", schema);
