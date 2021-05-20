import express from "express";
import * as controller from "../../controllers/admin/AdminAddress.js";
const router = express.Router();
router
  .route("")
  .post(controller.createAddress)
  .patch(controller.updateDistrict)
  .delete(controller.deleteAddress);

export default router;
