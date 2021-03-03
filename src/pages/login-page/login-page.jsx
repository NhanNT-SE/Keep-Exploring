import { yupResolver } from "@hookform/resolvers/yup";
import Button from "@material-ui/core/Button";
import Divider from "@material-ui/core/Divider";
import CheckBoxField from "custom-fields/checkbox-field";
import InputField from "custom-fields/input-field";
import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useSelector, useDispatch } from "react-redux";
import { actionLogin, actionLogout } from "redux/slices/userSlice";
import * as yup from "yup";
import "./styles/login-page.scss";
const schema = yup.object().shape({
  email: yup
    .string()
    .required("Email address is a required field")
    .email("Invalid email address"),
  password: yup
    .string()
    .matches(
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/,
      "Password must contain at least 8 characters, one uppercase, one number and one special case character"
    ),
});
function LoginPage() {
  const user = useSelector((state) => state.user.user);
  const dispatch = useDispatch();
  const { handleSubmit, control, errors, register } = useForm({
    defaultValues: {
      email: "",
      password: "",
      remember: false,
    },
    resolver: yupResolver(schema),
    mode: "all",
  });
  const onSubmit = (data) => console.log(data);
  const login = () => {
    const userLogin = {
      id: "PS10674",
      name: "Nguyen Trong Nhan",
      email: "nhannt.se1905@gmail.com",
    };
    dispatch(actionLogin(userLogin));
  };

  useEffect(() => {
    console.log(user);
  }, [user]);

  return (
    <div className="login-page">
      <div className="content">
        <p className="content-title">Keep Exploring Admin</p>
        <form onSubmit={handleSubmit(onSubmit)}>
          <InputField
            name="email"
            control={control}
            placeholder="Email"
            label="Email address"
            message={errors.email ? errors.email.message : ""}
          />
          <InputField
            name="password"
            control={control}
            placeholder="Password"
            label="Password"
            message={errors.password ? errors.password.message : ""}
            type="password"
          />
          <CheckBoxField
            name="remember"
            control={control}
            label="remember me?"
            defaultValue={false}
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
            onClick={login}
          >
            Sign in with facebook
          </Button>
          <Button
            variant="contained"
            color="primary"
            style={{ backgroundColor: "#dd4b39" }}
          >
            Sign in with google
          </Button>
        </form>
      </div>
    </div>
  );
}
export default LoginPage;
