import React from "react";
import Button from "@material-ui/core/Button";
import "./styles/login-page.scss";
function LoginPage() {
  return (
    <div className="login-page">
      <Button variant="contained" color="primary">
        Hello World
      </Button>
      <img src='/images/logo.png'/>
    </div>
  );
}

export default LoginPage;
