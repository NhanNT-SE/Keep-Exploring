import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog } from "redux/slices/dialog.slice";
import { DIALOG } from "utils/global_variable";
import "./dialog-verify-otp.scss";
function DialogVerifyOTP() {
  const iShowDialog = useSelector((state) => state.dialog.dialogVerifyOTP);
  const dispatch = useDispatch();
  const [otp, setOTP] = useState("");
  const [isDisable, setIsDisable] = useState(true);
  const hideDialog = () => {
    dispatch(actionHideDialog(DIALOG.DIALOG_VERIFY_OTP));
  };

  useEffect(() => {
    if (otp.length === 6) {
      setIsDisable(false);
    } else {
      setIsDisable(true);
    }
  }, [otp]);
  const verifyOTP = () => {};
  return (
    <Dialog
      header="Verify OTP token"
      visible={iShowDialog}
      style={{ width: "40vw" }}
      onHide={hideDialog}
    >
      <div className="dialog-verify-mfa-container">
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
        <Button
          disabled={isDisable}
          label="Verify"
          className="verify"
          onClick={verifyOTP}
        />
      </div>
    </Dialog>
  );
}

export default DialogVerifyOTP;
