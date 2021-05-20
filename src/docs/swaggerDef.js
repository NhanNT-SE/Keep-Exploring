const port = 3000;
const swaggerDef = {
	openapi: '3.0.3',
	info: {
		title: 'Keep Explored API documentation',
		version: '1.0.0',
		license: {
			name: 'MIT',
			url: 'https://github.com/hagopj13/node-express-boilerplate/blob/master/LICENSE',
		},
	},
	servers: [
		{
			url: `http://localhost:${port}`,
		},
	],
};

export default  swaggerDef;
