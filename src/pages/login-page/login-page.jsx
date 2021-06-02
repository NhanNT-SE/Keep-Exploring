import { yupResolver } from "@hookform/resolvers/yup";
import Button from "@material-ui/core/Button";
import DialogMessage from "common-components/dialog/dialog-message/dialog-message";
import LoadingComponent from "common-components/loading/loading";
import InputField from "custom-fields/input-field";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { actionLogin } from "redux/slices/auth.slice";
import * as yup from "yup";
import "./login-page.scss";
const schema = yup.object().shape({
  username: yup
    .string()
    .required("Email address is a required field")
    .email("Invalid email address"),
  password: yup
    .string()
    .required()
    .matches(
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/,
      "Password must contain at least 8 characters, one uppercase, one number and one special case character"
    ),
});
function LoginPage(props) {
  const user = useSelector((state) => state.auth.user);
  const isRemember = useSelector((state) => state.common.isRemember);
  const loadingStore = useSelector((state) => state.common.isLoading);
  const [username, setUsername] = useState("nhan@keep-exploring.com");
  const [password, setPassword] = useState("Nhanvippro320377@");
  const history = useHistory();
  const dispatch = useDispatch();
  const { handleSubmit, control, errors, register, setValue } = useForm({
    defaultValues: {
      username,
      password,
    },
    resolver: yupResolver(schema),
    mode: "all",
  });

  const onSubmit = (data) => {
    const userLogin = { username, password };
    dispatch(actionLogin(userLogin));
  };

  const handlerOnChange = (e, name, callBack) => {
    const { value } = e.target;
    setValue(name, value);
    callBack(value);
  };

  useEffect(() => {
    if (user && user.role === "admin") {
      history.push("/home");
    }
  }, [user]);
  return (
    <div className="login-page">
      {loadingStore && <LoadingComponent />}
      <DialogMessage />
      <div className="content">
        <p className="content-title">Keep Exploring Admin</p>
        <form onSubmit={handleSubmit(onSubmit)}>
          <InputField
            name="username"
            control={control}
            placeholder="Username or Email address"
            label="Username/Email"
            message={errors.username ? errors.username.message : ""}
            onChange={(e) => handlerOnChange(e, "username", setUsername)}
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
          <Button variant="contained" color="primary" type="submit">
            Sign in
          </Button>
        </form>
      </div>
    </div>
  );
}
export default LoginPage;
