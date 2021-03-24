const express = require("express");
const router = express.Router();
const passport = require("passport");
const statisticsController = require("../Controllers/StatisticsController");
require("../middleware/passport");
router.get(
  "/",
  passport.authenticate("jwt"),
  statisticsController.statisticsPost
);
module.exports = router;