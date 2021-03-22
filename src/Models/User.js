const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const UserSchema = new Schema(
	{
		email: {
			type: String,
			required: true,
		},
		pass: {
			type: String,
			required: true,
		},
		displayName: {
			type: String,
			required: true,
		},
		role: {
			type: String,
			enum: ['admin', 'user'],
			default: 'user',
		},
		imgUser: {
			type: String,
		},
		post: [
			{
				type: Schema.Types.ObjectId,
				ref: 'Post',
			},
		],
		blog: [
			{
				type: Schema.Types.ObjectId,
				ref: 'Blog',
			},
		],
	},
	{ collection: 'User' }
);

// UserSchema.methods.joiValidate = function (obj) {
//     const Joi = require('@hapi/joi');
//     	const schema = {
// 		email: Joi.string().email().required(),
// 		pass: Joi.string().min(6).required(),
// 	};
// 	return Joi.validate(obj, schema);
// };

module.exports = mongoose.model('User', UserSchema);
