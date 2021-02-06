const express = require('express');
const addressController = require('../Controllers/AddressController');

const router = express.Router();

//GET Method
router.get('/list', addressController.getAddressList);

//POST Method
router.post('/add', addressController.createAdress);
router.post('/delete', addressController.deleteAddress);

module.exports = router;
