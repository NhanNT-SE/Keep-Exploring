import dotenv from "dotenv";
dotenv.config();
const ACCESS_TOKEN_SECRET = process.env.ACCESS_TOKEN_SECRET;
const REFRESH_TOKEN_SECRET = process.env.REFRESH_TOKEN_SECRET;
const ACCESS_TOKEN_LIFE = process.env.ACCESS_TOKEN_LIFE;
const REFRESH_TOKEN_LIFE = process.env.REFRESH_TOKEN_LIFE;
const SERVER_NAME = process.env.SERVER_NAME;
const PROJECT_ID = process.env.PROJECT_ID;
const BUCKET_NAME = process.env.BUCKET_NAME;
const BUCKET_STORAGE = process.env.BUCKET_STORAGE;
const DEFAULT_AVATAR =
  "https://storage.googleapis.com/keep-exploring/avatar/avatar-default.png";
export {
  ACCESS_TOKEN_SECRET,
  REFRESH_TOKEN_SECRET,
  ACCESS_TOKEN_LIFE,
  REFRESH_TOKEN_LIFE,
  SERVER_NAME,
  PROJECT_ID,
  BUCKET_NAME,
  BUCKET_STORAGE,
  DEFAULT_AVATAR,
};
