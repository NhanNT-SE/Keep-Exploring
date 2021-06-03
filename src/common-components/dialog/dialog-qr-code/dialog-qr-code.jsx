import { Dialog } from "primereact/dialog";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./dialog-qr-code.scss";
import { Steps } from "primereact/steps";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { actionHideDialog } from "redux/slices/dialog.slice";
import GLOBAL_VARIABLE from "utils/global_variable";
import mfaAPI from "api/mfa.api";
import localStorageService from "utils/localStorageService";
function DialogQRCode(props) {
  const iShowDialog = useSelector((state) => state.dialog.isShowDialogQRCode);
  const user = JSON.parse(localStorageService.getUser());
  const dispatch = useDispatch();

  const [password, setPassword] = useState("KeepExploring@");
  const [activeStep, setActiveStep] = useState(0);
  const [qrCode, setQRCode] = useState(0);
  const [otp, setOTP] = useState(0);
  const items = [
    {
      label: "Confirm password",
    },
    {
      label: "Scan QR code",
    },
    {
      label: "Verity OTP",
    },
  ];
  const stepConfirmPassword = async () => {
    const response = await mfaAPI.enableMFA({ password });
    console.log(password);
    // console.log(user);
    // setActiveStep(1);
  };
  const stepQRCode = (e) => {
    setActiveStep(2);
  };
  const stepVerifyOTP = () => {};
  const openTab = () => {
    window.open(
      "https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=vi&gl=US",
      "_blank"
    );
  };
  const hideDialog = () => {
    dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_QR_CODE));
  };

  return (
    <Dialog
      header="Enable MFA for first time"
      visible={iShowDialog}
      style={{ width: "50vw" }}
      onHide={hideDialog}
      className="dialog-notify"
    >
      <Steps model={items} activeIndex={activeStep} />
      <div className="dialog-qr-container">
        {activeStep === 0 && (
          <div className="confirm-password">
            <span className="p-input-icon-left">
              <i className="pi pi-lock" />
              <InputText
                placeholder="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </span>
            <Button label="Submit" onClick={stepConfirmPassword} />
          </div>
        )}
        {activeStep === 1 && (
          <div className="scan-qr">
            <span className="note">
              Use <b>Google Authenticator</b> to scan this code below, if you
              don't this app
              <span>
                <b onClick={openTab} className="open-tab">
                  {" "}
                  Click here
                </b>{" "}
                to download
              </span>
            </span>
            <img src={qrCode} alt="qrcode" />
            <Button label="Next" onClick={stepQRCode} />
          </div>
        )}

        {activeStep === 2 && (
          <div className="verify-otp">
            <span className="p-input-icon-left">
              <i className="pi pi-lock" />
              <InputText
                placeholder="OTP code"
                type="text"
                value={otp}
                maxLength="6"
                onChange={(e) => setOTP(e.target.value)}
              />
            </span>
            <Button label="Verify" onClick={stepVerifyOTP} />
          </div>
        )}
      </div>
    </Dialog>
  );
}

export default DialogQRCode;
