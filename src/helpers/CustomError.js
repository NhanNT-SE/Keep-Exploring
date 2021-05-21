const customError = (status, message, payload) => {
  const err = new Error();
  err.status = status || 500;
  err.message = message;
  err.payload = payload;
  throw err;
};
const mapErrorMessage = (message) => {
  const resultMsg = message.split(": ").slice(2, 3).toString();
  switch (resultMsg) {
    case "duplicate email":
      return "This email is already registered, please choose another one.";
    case "duplicate username":
      return "This username is already registered, please choose another one.";
    case "invalid email":
      return "invalid format of email address";
    case "invalid password":
      return "password should contain atleast 8 characters, 1 number, 1 special character , 1 upper and 1 lowercase";
    case "min username":
      return "username should contain atleast 6 Characters";
    case "min fullName":
      return "fullName should contain atleast 6 Characters";
    default:
      return resultMsg;
  }
};
export { customError, mapErrorMessage };
