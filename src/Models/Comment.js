const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const commentSchema = new Schema(
	{
		idPost: {
			type: Schema.Types.ObjectId,
			ref: 'Post',
		},
		idBlog: {
			type: Schema.Types.ObjectId,
			ref: 'Blog',
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
		img: {
			type:String,
		},
	},
	{ collection: 'Comment' }
);

module.exports = mongoose.model('Comment', commentSchema);