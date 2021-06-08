import _ from "lodash";
const customError = (message) => {
  const err = new Error();
  err.status = 400;
  err.message = message;
  throw err;
};
const errorAuth = (message, payload) => {
  const err = new Error();
  err.status = 401;
  err.message = message;
  err.payload = payload;
  throw err;
};
const mapErrorMessage = (error) => {
  const { name, errors, code } = error;
  let message = error.message;
  if (name === "MongoError" && code === 11000) {
    const path = Object.keys(error.keyValue)[0];
    const msg = error.keyValue[path];
    message = `${path}: ${msg} is already registered, please choose another one`;
  }
  if (errors) {
    const keys = Object.keys(errors);
    _.forEach(keys, (key) => {
      const props = errors[key].properties;
      message = props.message;
      return false;
    });
  }
  return message;
};
export { customError, errorAuth, mapErrorMessage };
