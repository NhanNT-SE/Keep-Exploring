require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const passport = require('passport');
const cors = require('cors');
const multer = require('multer');

const userRouter = require('./src/Route/UserRoute');
const postRouter = require('./src/Route/PostRoute');
const addressRouter = require('./src/Route/AddressRoute');
const commentRouter = require('./src/Route/CommentRoute');
const apiDocRouter = require('./src/Route/APIDocsRoute');
const blogRouter = require('./src/Route/BlogRoute');

// const mongoString = 'mongodb+srv://admin:doanhnhangroup@cluster0.jqsm5.mongodb.net/user?retryWrites=true&w=majority';
const mongoString = 'mongodb://nhannt:nhannt1905@13.58.149.178:27017/keep-exploring?authSource=admin&w=1';
const port = process.env.PORT || 3000;
const app = express();
const forms = multer();

app.use(express.static('src/public'));
app.use(bodyParser.json());
// app.use(forms.array());
app.use(
	bodyParser.urlencoded({
		extended: true,
	})
);
app.use(passport.initialize());
app.use(cors());

//Routers
app.use('/api-doc', apiDocRouter);
app.use('/user', userRouter);
app.use('/post', postRouter);
app.use('/address', addressRouter);
app.use('/comment', commentRouter);
app.use('/blog', blogRouter);

//Config next() function

//Kết nối với mongo database
mongoose
	.connect(mongoString, {
		useUnifiedTopology: true,
		useNewUrlParser: true,
		useCreateIndex: true,
		useFindAndModify: false,
	})
	.then(() => {
		console.log('Connected to database ');
	})
	.catch((err) => {
		console.error(`Error connecting to the database. \n${err}`);
	});

app.listen(port, console.log(`start on port ${port}`));