const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
const multer = require("multer");
const forms = multer();
const app = express();
app.use(cors());
app.use(bodyParser.json());
app.use(forms.array());
app.use(bodyParser.urlencoded({ extended: true }));

app.get("/",async (req, res, next) => {
  try {
    //  Nếu muốn trả lỗi thì trả kiểu này
    let err = new Error(`tried to access /Forbidden`);
    err.status = 403;
    // next nó tương đương với return res.send()
    // next(err);

    //Còn ko thì trả về bình thường
    return res.send({ err: { message: "error custom", status: 300 } });
  } catch (error) {
    // Nếu muốn thì set status code cho nó ko thì thôi, nó sẽ tự lấy nếu có còn ko thì default sẽ là 500
    error.status = 444;
    next(error);
    // return res.send(error);
  }
});
// Handler 404 error page not found
app.use((req, res, next) => {
  const error = new Error("Not found");
  error.status = 404;
  next(error);
});
// Handler custom error page not found
app.use((error, req, res, next) => {
  res.status(error.status || 500);
  res.send({
    error: {
      status: error.status || 500,
      message: error.message,
    },
  });
});
app.listen(3000, () => console.log("app listen on port 3000"));
