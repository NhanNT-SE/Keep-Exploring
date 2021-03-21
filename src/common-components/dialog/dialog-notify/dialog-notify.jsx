import { Dialog } from "primereact/dialog";
import { InputTextarea } from "primereact/inputtextarea";
import GLOBAL_VARIABLE from "utils/global_variable";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Button } from "primereact/button";
import { actionHideDialog } from "redux/slices/commonSlice";
import "./dialog-notify.scss";
import { actionSendNotify } from "redux/slices/userSlice";
function DialogNotify(props) {
  const { user } = props;
  const dispatch = useDispatch();
  const [notify, setNotify] = useState("");
  const isShowDialog = useSelector((state) => state.common.isShowDialogNotify);
  const hideDialog = () => {
    dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
  };
  const sendNotify = () => {
    const payload = {
      idUser: user._id,
      contentAdmin: notify,
    };
    dispatch(actionSendNotify(payload));
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
          label="Clear"
          icon="pi pi-ban"
          className="p-button-success"
          disabled={notify.length > 0 ? false : true}
          onClick={() => setNotify("")}
        />
        <Button
          label="Send"
          icon="pi pi-send"
          disabled={notify ? false : true}
          className="p-button-success"
          onClick={sendNotify}
        />
      </div>
    );
  };
  return (
    <Dialog
      header="Edit Post"
      visible={isShowDialog}
      style={{ width: "50vw" }}
      footer={renderFooter()}
      onHide={hideDialog}
      className="dialog-notify"
    >
      <h5>Send notification to user: "{user.displayName}"</h5>
      <InputTextarea
        rows={5}
        cols={30}
        value={notify}
        onChange={(e) => setNotify(e.target.value)}
        placeholder="Type message you want to send here"
        required
      />
    </Dialog>
  );
}

export default DialogNotify;
