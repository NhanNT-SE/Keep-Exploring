import { fileURLToPath } from "url";
import { Storage } from "@google-cloud/storage";
import path from "path";
import { BUCKET_NAME, PROJECT_ID, BUCKET_STORAGE } from "../../src/config/index.js";
import multer from "multer";
const __dirname = path.dirname(fileURLToPath(import.meta.url));
const gc = new Storage({
  keyFilename: path.join(__dirname, "../../admin-storage.json"),
  projectId: PROJECT_ID,
});
const bucket = gc.bucket(BUCKET_NAME);
const storage = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: 5 * 1024 * 1024, // no larger than 5mb, you can change as needed.
  },
});
const urlImage = (path) => {
  return `${BUCKET_STORAGE}/${path}`;
};
const pathImage = (dir, file) => {
  const uniqueSuffix = Date.now() + "-";
  const path = uniqueSuffix + file.originalname.split(" ").join("-");
  return `${dir}${path}`;
};

export { urlImage, pathImage, storage, bucket };
