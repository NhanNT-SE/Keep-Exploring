import { yupResolver } from "@hookform/resolvers/yup";
import Button from "@material-ui/core/Button";
import Divider from "@material-ui/core/Divider";
import localStorageService from "api/localStorageService";
import postApi from "api/postApi";
import LoadingComponent from "common-components/loading/loading";
import CheckBoxField from "custom-fields/checkbox-field";
import InputField from "custom-fields/input-field";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { actionGetAllPost } from "redux/slices/postSlice";
import { actionLogin, actionLogout } from "redux/slices/userSlice";
import * as yup from "yup";
import "./styles/login-page.scss";
const schema = yup.object().shape({
  email: yup.string().required("Email address is a required field"),
  // .email("Invalid email address"),
  password: yup.string().required().min(6),
  // .matches(
  //   /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/,
  //   "Password must contain at least 8 characters, one uppercase, one number and one special case character"
  // ),
});
function LoginPage(props) {
  let userObj;
  const user = useSelector((state) => state.user.user);
  const loading = useSelector((state) => state.user.loading);
  const [email, setEmail] = useState("admin");
  const [password, setPassword] = useState("123456");
  const [remember, setRemember] = useState(() => {
    const userStorage = localStorageService.getUser();
    if (userStorage) {
      userObj = JSON.parse(userStorage);
      return userObj.remember;
    }
    return false;
  });
  const history = useHistory();
  const dispatch = useDispatch();

  const { handleSubmit, control, errors, register, setValue } = useForm({
    defaultValues: {
      email,
      password,
    },
    resolver: yupResolver(schema),
    mode: "all",
  });

  const onSubmit = (data) => {
    const userLogin = { email, pass: password };
    if (data.remember) {
      localStorageService.setUser(data);
    } else {
      localStorageService.clearUser();
    }
    dispatch(actionLogin(userLogin));
  };

  const handlerOnChange = (e, name, callBack) => {
    const { value, checked } = e.target;
    setValue(name, value || checked);
    callBack(value || checked);
  };
  const loginFacebook = () => {
    console.log("Facebook");
  };
  const loginGoogle = () => {
    console.log("Google");
  };
  useEffect(() => {
    if (userObj) {
      const userLogin = { email: userObj.email, pass: userObj.password };
      dispatch(actionLogin(userLogin));
    }
  }, []);
  useEffect(() => {
    if (user && user.role ==="admin") {
      history.push("/home");
    }
  }, [user]);
  return (
    <div className="login-page">
      {loading && <LoadingComponent />}

      <div className="content">
        <p className="content-title">Keep Exploring Admin</p>
        <form onSubmit={handleSubmit(onSubmit)}>
          <InputField
            name="email"
            control={control}
            placeholder="Email"
            label="Email address"
            message={errors.email ? errors.email.message : ""}
            onChange={(e) => handlerOnChange(e, "email", setEmail)}
          />
          <InputField
            name="password"
            control={control}
            placeholder="Password"
            label="Password"
            message={errors.password ? errors.password.message : ""}
            type="password"
            onChange={(e) => handlerOnChange(e, "password", setPassword)}
          />
          <CheckBoxField
            name="remember"
            control={control}
            label="remember me?"
            defaultChecked={remember}
            inputRef={register}
          />
          <Button variant="contained" color="primary" type="submit">
            Sign in
          </Button>
          <Divider />
          <Button
            variant="contained"
            color="primary"
            style={{ backgroundColor: "#3B5998" }}
            onClick={loginFacebook}
          >
            Sign in with facebook
          </Button>
          <Button
            variant="contained"
            color="primary"
            style={{ backgroundColor: "#dd4b39" }}
            onClick={loginGoogle}
          >
            Sign in with google
          </Button>
        </form>
      </div>
    </div>
  );
}
export default LoginPage;