const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const commentSchema = new Schema(
	{
		idPost: {
			type: Schema.Types.ObjectId,
			ref: 'Post',
			required: true,
		},
		idUser: {
			type: Schema.Types.ObjectId,
			ref: 'User',
			required: true,
		},
		content: {
			type: String,
			required: true,
		},
		date: {
			type: Date,
			default: Date.now(),
		},
		imgs: {
			type: String,
		},
	},
	{ collection: 'Comment' }
);

module.exports = mongoose.model('Comment', commentSchema);
