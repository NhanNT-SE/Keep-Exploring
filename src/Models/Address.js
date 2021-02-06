const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const addressSchema = new Schema(
	{
		idPost: {
			type: Schema.Types.ObjectId,
            required: true,
            ref:"Post"
		},
		province: {
			type: String,
			required: true,
		},
		district: {
			type: String,
		},
		ward: {
			type: String,
		},
		additional: {
			type: String,
		},
	},
	{ collection: 'Address' }
);

module.exports = mongoose.model('Address', addressSchema);
