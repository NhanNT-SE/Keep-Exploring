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
    onlineStatus: {
      type: String,
      enum: ["online", "offline","idle"],
      default: "idle",
      message: "{VALUE} is not supported",
    },
    stars: {
      type: Number,
      min: 100,
      default: 100,
    },
    rank: {
      type: String,
      enum: ["diamond", "platinum", "gold", "sliver", "bronze"],
      default: "bronze",
      message: "{VALUE} is not supported",
    },
    accountStatus: {
      type: String,
      enum: ["active", "inactive", "blocked"],
      default: "active",
      message: "{VALUE} is not supported",
    },
    periodBlocked: {
      type: Object,
      dayBlocked: { type: Number, min: 0 },
      dateStart: { type: Date },
      dateEnd: { type: Date },
      required: function () {
        return this.accountStatus === "blocked";
      },
    },
    follower: [{ type: Schema.Types.ObjectId, ref: "user" }],
    following: [{ type: Schema.Types.ObjectId, ref: "user" }],
    blockedList: [{ type: Schema.Types.ObjectId, ref: "user" }],
    postList: [{ type: Schema.Types.ObjectId, ref: "post" }],
  },
  { collection: "userInfo" }
);

export const UserInfo = mongoose.model("userInfo", schema);
