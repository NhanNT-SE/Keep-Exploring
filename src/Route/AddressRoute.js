const express = require("express");
const addressController = require("../Controllers/AddressController");
const router = express.Router();

//POST Method
router.post("/", addressController.createAddress);

//Delete Method
router.delete("/:idPost", addressController.deleteAddress);

//Patch Method
router.patch("/:idAddress", addressController.updateAddress);
router.patch("/", addressController.updateDistrict);

module.exports = router;
