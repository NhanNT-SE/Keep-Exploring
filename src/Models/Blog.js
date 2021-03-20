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
		comment: [
			{
				type: Schema.Types.ObjectId,
				ref: 'Comment',
			},
		],
		blog_detail: [
			{
				type: Schema.Types.ObjectId,
				ref: 'Blog_Detail',
			},
		],
	},
	{ collection: 'Blog' }
);

module.exports = moongose.model('Blog', BlogSchema);
