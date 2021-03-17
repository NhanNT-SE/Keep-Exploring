const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const NotificaionSchema = new Schema(
	{
		idUser: {
			type: Schema.Types.ObjectId,
			ref: 'User',
		},
		status: {
			type: String,
			enum: ['new', 'seen'],
			default: 'new',
		},
		idPost: {
			type: Schema.Types.ObjectId,
			ref: 'Post',
		},
		idBlog: {
			type: Schema.Types.ObjectId,
			ref: 'Blog',
		},
		content: {
			type: String,
			enum: ['like', 'comment', 'moderated', 'unmoderated'],
		},
	},
	{ collection: 'Notification' }
);

module.exports = mongoose.model('Notification', NotificaionSchema);
