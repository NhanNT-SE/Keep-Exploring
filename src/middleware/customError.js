const handlerCustomError = (status, message, payload) => {
  const err = new Error();
  err.status = status || 500;
  err.message = message;
  err.payload = payload ;
  throw err;
};
module.exports = handlerCustomError;
