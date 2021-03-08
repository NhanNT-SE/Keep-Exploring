const express = require('express');
const addressController = require('../Controllers/AddressController');
<<<<<<< HEAD

const router = express.Router();

//GET Method
router.get('/list', addressController.getAddressList);

//POST Method
router.post('/add', addressController.createAdress);
router.post('/delete', addressController.deleteAddress);
=======
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
>>>>>>> c5140b92034d8033313ffeb8ffc757fd48b1a331

module.exports = router;
