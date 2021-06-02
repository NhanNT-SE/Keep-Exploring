import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { actionHideDialog } from "redux/slices/common.slice";
import {
    actionDeleteUser
} from "redux/slices/user.slice";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./dialog-delete-user.scss";
function DialogDeleteUser(props) {
  const { user } = props;
  const dispatch = useDispatch();
  const history = useHistory();
  const [confirm, setConfirm] = useState("");
  const isShowDialog = useSelector(
    (state) => state.common.isShowDialogDeleteUser
  );
  const hideDialog = () => {
    dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_DELETE_USER));
  };
  const deleteUser = () => {
    const payload = {
      userId: user._id,
      history,
    };
    dispatch(actionDeleteUser(payload));
  };
  const renderFooter = () => {
    return (
      <div>
        <Button
          label="Cancel"
          icon="pi pi-times"
          className="p-button-success"
          onClick={hideDialog}
        />
        <Button
          label="Delete"
          icon="pi pi-trash"
          disabled={
            confirm.toLocaleLowerCase() === "confirm-delete" ? false : true
          }
          className="p-button-danger"
          onClick={deleteUser}
        />
      </div>
    );
  };
  return (
    <Dialog
      header="Delete User"
      visible={isShowDialog}
      style={{ width: "50vw" }}
      footer={renderFooter()}
      onHide={hideDialog}
      className="dialog-delete-user"
    >
      <div className="message">
        This action cannot be undone. This will permanently remove the user with
        id <span className="bold">{user._id}</span> from your system. <br />{" "}
        Please type
        <span className="bold"> confirm-delete</span> to confirm.
      </div>
      <InputText value={confirm} onChange={(e) => setConfirm(e.target.value)} />
    </Dialog>
  );
}

export default DialogDeleteUser;
