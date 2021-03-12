const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const RefreshTokenSchema = new Schema(
	{
		_id: { type: String },
		accessToken: {
			type: String,
		},
		refreshToken: {
			type: String,
		},
	},

	{ collection: 'RefreshToken' }
);

module.exports = mongoose.model('RefreshhToken', RefreshTokenSchema);
