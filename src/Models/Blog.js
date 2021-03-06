const moongose = require('mongoose');
const Schema = moongose.Schema;

const BlogSchema = new Schema(
	{
		owner: {
			type: Schema.Types.ObjectId,
			ref: 'User',
		},
		title: {
			type: String,
			required: true,
		},
		img: {
			type: String,
		},
		
		status: {
			type: String,
			enum: ['pending', 'done', 'need_update'],
			default: 'pending',
		},
		like_list: [
			{
				type: Schema.Types.ObjectId,
				ref: 'User',
			},
		],
		created_on: {
			type: Date,
			default: Date.now,
		},
	},
	{ collection: 'Blog' }
);

module.exports = moongose.model('Blog', BlogSchema);