import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { actionHideDialog } from "redux/slices/commonSlice";
import { actionChangePassword } from "redux/slices/userSlice";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./dialog-change-password.scss";
function DialogChangePassword(props) {
  const { user } = props;
  const dispatch = useDispatch();
  const history = useHistory();
  const [currentPass, setCurrentPass] = useState("");
  const [newPass, setNewPass] = useState("");
  const [confirmPass, setConfirmPass] = useState("");
  const isShowDialog = useSelector(
    (state) => state.common.isShowDialogChangePassword
  );
  const hideDialog = () => {
    dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_CHANGE_PASSWORD));
  };
  const changePassword = () => {
    const payload = {
      userId: user._id,
      history,
    };
    dispatch(actionChangePassword(payload));
  };
  const renderFooter = () => {
    return (
      <div>
        <Button
          label="Cancel"
          icon="pi pi-times"
          className="p-button-warning"
          onClick={hideDialog}
        />
        <Button
          label="Delete"
          icon="pi pi-lock"
          // disabled={
          //   confirm.toLocaleLowerCase() === "confirm-delete" ? false : true
          // }
          className="p-button-warning"
          onClick={changePassword}
        />
      </div>
    );
  };
  return (
    <Dialog
      header="Change Password"
      visible={isShowDialog}
      style={{ width: "50vw" }}
      footer={renderFooter()}
      onHide={hideDialog}
      className="dialog-change-password"
    >
          <InputText
            id="currentPass"
            value={currentPass}
            onChange={(e) => setCurrentPass(e.target.value)}
          />
    
          <InputText
            id="currentPass"
            value={newPass}
            onChange={(e) => setNewPass(e.target.value)}
          />
    
          <InputText
            id="currentPass"
            value={confirmPass}
            onChange={(e) => setConfirmPass(e.target.value)}
          />
    </Dialog>
  );
}

export default DialogChangePassword;
