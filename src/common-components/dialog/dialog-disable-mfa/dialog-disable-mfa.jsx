import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog } from "redux/slices/dialog.slice";
import { actionDisableMFA } from "redux/slices/mfa.slice";
import { DIALOG } from "utils/global_variable";
import "./dialog-disable-mfa.scss";
function DialogDisableMFA() {
  const iShowDialog = useSelector((state) => state.dialog.dialogDisableMFA);
  const dispatch = useDispatch();
  const [password, setPassword] = useState("KeepExploring@");
  const [otp, setOTP] = useState("");
  const [isDisable, setIsDisable] = useState(true);
  const hideDialog = () => {
    dispatch(actionHideDialog(DIALOG.DIALOG_DISABLE_MFA));
  };

  useEffect(() => {
    if (password.length >= 8 && otp.length === 6) {
      setIsDisable(false);
    } else {
      setIsDisable(true);
    }
  }, [password, otp]);
  const disableMFA = () => {
    dispatch(actionDisableMFA({ password, otp }));
  };
  return (
    <Dialog
      header="Disable MFA"
      visible={iShowDialog}
      style={{ width: "40vw" }}
      onHide={hideDialog}
    >
      <span>
        Your account is more secure when you need a password and a verification
        code to sign in. If you remove this extra layer of security, you will
        only be asked for a password when you sign in. It might be easier for
        someone to break into your account.
      </span>
      <div className="dialog-disable-mfa-container">
        <div className="input-container">
          <span className="label">Your password</span>
          <span className="p-input-icon-left">
            <i className="pi pi-lock" />
            <InputText
              placeholder="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </span>
        </div>
        <div className="input-container">
          <span className="label">OTP code</span>

          <span className="p-input-icon-left">
            <i className="pi pi-lock" />
            <InputText
              placeholder="6-digit code"
              type="text"
              value={otp}
              maxLength="6"
              onChange={(e) => setOTP(e.target.value)}
            />
          </span>
        </div>
        <div className="button-container">
          <Button label="Cancel" className="back" onClick={hideDialog} />
          <Button
            disabled={isDisable}
            label="Verify"
            className="verify"
            onClick={disableMFA}
          />
        </div>
      </div>
    </Dialog>
  );
}

export default DialogDisableMFA;
