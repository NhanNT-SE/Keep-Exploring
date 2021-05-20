import express from "express";
import * as controller from "../../controllers/admin/AdminStatistic.js";
const router = express.Router();
router.get("/number", controller.statisticsNumber);
router.get("/time-line", controller.statisticsTimeLine);

export default router;
