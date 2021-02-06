const express = require('express');
const router = express.Router();

const swaggerJsdoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');
const swaggerDefinition = require('../docs/swaggerDef');

//Config Swagger document
const swaggerDoc = swaggerJsdoc({
	swaggerDefinition,
	apis: ['src/docs/*.yml', 'src/Route/*.js'],
});

router.use('/', swaggerUi.serve);
router.get(
	'/',
	swaggerUi.setup(swaggerDoc, {
		explorer: true,
	})
);

module.exports = router;
