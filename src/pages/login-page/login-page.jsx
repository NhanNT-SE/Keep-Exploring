import Button from "@material-ui/core/Button";
import React from "react";
import TextField from "@material-ui/core/TextField";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Divider from "@material-ui/core/Divider";
import "./styles/login-page.scss";
import { useForm } from "react-hook-form";
import InputField from "custom-fields/input-field";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
const schema = yup.object().shape({
  email: yup.string().min(8),
  password: yup
    .string()
    .matches(
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/,
      "Password must contain at least 8 characters, one uppercase, one number and one special case character"
    ),
});
function LoginPage() {
  const { handleSubmit, control, errors } = useForm({
    defaultValues: {
      email: "",
      password: "",
    },
    resolver: yupResolver(schema),
    mode: "all",
  });
  const onSubmit = (data) => console.log(data);
  return (
    <div className="login-page">
      <div className="content">
        <img src="/images/logo-text.png" />
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
          <FormControlLabel
            control={<Checkbox name="remember me" color="primary" />}
            label="remember me  ?"
          />
          <Button variant="contained" color="primary" type="submit">
            Sign in
          </Button>
          <Divider />
          <Button
            variant="contained"
            color="primary"
            style={{ backgroundColor: "#3B5998" }}
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
