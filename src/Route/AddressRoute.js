const express = require('express');
const addressController = require('../Controllers/AddressController');
const passport = require('passport');

require('../middleware/passport');
const router = express.Router();

//GET Method
router.get('/', addressController.getAddressList);
router.get('/province', addressController.getPostbyAddress);

//POST Method
router.post('/', addressController.createAdress);

//Delete Method
router.delete('/:idPost', addressController.deleteAddress);

module.exports = router;
