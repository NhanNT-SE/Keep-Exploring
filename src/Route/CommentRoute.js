const express = require('express');
const commentController = require('../Controllers/CommentController');

const router = express.Router();

// GET Method
router.get('/getByPost', commentController.getCommentbyPost);

//POST Method
router.post('/add', commentController.createComment);
router.post('/deleteByPost', commentController.deleteCommentbyPost);
router.post('/deletebyId/:idComment', commentController.deleteCommentbyID);

module.exports = router;
