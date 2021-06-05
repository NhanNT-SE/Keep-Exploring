import mfaAPI from "api/mfa.api";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Steps } from "primereact/steps";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog } from "redux/slices/dialog.slice";
import { actionEnableMFA } from "redux/slices/mfa.slice";
import { DIALOG } from "utils/global_variable";
import localStorageService from "utils/localStorageService";
import "./dialog-enable-mfa.scss";
function DialogEnableMFA() {
  const iShowDialog = useSelector((state) => state.dialog.dialogEnableMFA);
  const user = JSON.parse(localStorageService.getUser());
  const dispatch = useDispatch();

  const [password, setPassword] = useState("KeepExploring@");
  const [activeStep, setActiveStep] = useState(0);
  const [qrCode, setQRCode] = useState("");
  const [otp, setOTP] = useState("");
  const [err, setErr] = useState("");
  const [isDisable, setIsDisable] = useState(true);
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
    try {
      const response = await mfaAPI.getQRCode(password);
      setQRCode(response.data);
      setActiveStep(1);
    } catch (error) {
      setErr(error.message);
    }
  };
  const stepQRCode = () => {
    setActiveStep(2);
  };
  const stepVerifyOTP = async () => {
    try {
      dispatch(actionEnableMFA({ userId: user._id, otp }));
    } catch (error) {
      console.log(error);
    }
  };
  const openTab = () => {
    window.open(
      "https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=vi&gl=US",
      "_blank"
    );
  };
  useEffect(() => {
    const length = otp.length;
    if (length === 6) {
      setIsDisable(false);
    } else {
      setIsDisable(true);
    }
  }, [otp]);
  const back = () => {
    setActiveStep(1);
  };
  const hideDialog = () => {
    dispatch(actionHideDialog(DIALOG.DIALOG_ENABLE_MFA));
  };

  return (
    <Dialog
      header="Enable MFA"
      visible={iShowDialog}
      style={{ width: "50vw" }}
      onHide={hideDialog}
    >
      <Steps model={items} activeIndex={activeStep} />
      <div className="dialog-enable-mfa-container">
        {activeStep === 0 && (
          <div className="controller">
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
            {err && <div className="err-message">{err}</div>}
          </div>
        )}
        {activeStep === 1 && (
          <div className="scan-qr">
            <span className="note">
              Use <b>Google Authenticator</b> to scan this code below. If you
              don't have this app
              <span>
                <b onClick={openTab} className="open-tab">
                  {" "}
                  Click here
                </b>{" "}
                to download and install
              </span>
            </span>
            <img src={qrCode} alt="qrcode" />
            <Button label="Next" onClick={stepQRCode} />
          </div>
        )}

        {activeStep === 2 && (
          <div className="controller">
            <div className="verify-otp">
              <span>
                Open the <b>Google Authenticator</b> app on your device to view
                your authentication code and verify your identity.
              </span>
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
              <div className="button-container">
                <Button label="Back" className="back" onClick={back} />

                <Button
                  disabled={isDisable}
                  label="Verify"
                  className="verify"
                  onClick={stepVerifyOTP}
                />
              </div>
            </div>
            {err && <div className="err-message">{err}</div>}
          </div>
        )}
      </div>
    </Dialog>
  );
}

export default DialogEnableMFA;
