const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const BlogDetail_Schema = new Schema(
	{
		id_Post: {
			type: Schema.Types.ObjectId,
			ref: 'Blog',
		},
		img: {
			type: String,
		},
		content: {
			type: String,
		},
	},
	{ collection: 'Blog_Detail' }
);

module.exports = mongoose.model('Blog_Detail', BlogDetail_Schema);
