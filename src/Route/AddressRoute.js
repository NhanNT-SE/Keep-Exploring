const express = require("express");
const addressController = require("../Controllers/AddressController");
const passport = require("passport");

require("../middleware/passport");
const router = express.Router();

//GET Method
router.get("/", addressController.getAddressList);
router.get("/province", addressController.getPostByAddress);

//POST Method
router.post(
  "/",
  passport.authenticate("jwt", { session: false }),
  addressController.createAddress
);

//Delete Method
router.delete(
  "/:idPost",
  passport.authenticate("jwt", { session: false }),
  addressController.deleteAddress
);

//Patch Method
router.patch(
  "/:idAddress",
  passport.authenticate("jwt", { session: false }),
  addressController.updateAddress
);

module.exports = router;
