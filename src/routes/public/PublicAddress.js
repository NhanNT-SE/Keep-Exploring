import express from "express";
import * as controller from "../../controllers/public/PublicAddress.js";
const router = express.Router();

router.route("").get(controller.getProvinceList).post(controller.getAddress);

export default router;
