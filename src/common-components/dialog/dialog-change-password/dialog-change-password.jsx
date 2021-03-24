import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { actionHideDialog } from "redux/slices/commonSlice";
import { actionChangePassword } from "redux/slices/userSlice";
import GLOBAL_VARIABLE from "utils/global_variable";
import { Checkbox, Input, TextField } from "@material-ui/core";
import "./dialog-change-password.scss";
import InputField from "custom-fields/input-field";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm, Controller } from "react-hook-form";
const schema = yup.object().shape({
  currentPass: yup.string().required(),
  newPass: yup.string().required(),
  // .matches(
  //   /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/,
  //   "Password must contain at least 8 characters, one uppercase, one number and one special case character"
  // ),
  confirmPass: yup
    .string()
    .oneOf(
      [yup.ref("newPass"), null],
      "Confirm passwords must match with new pass word"
    ),
});
function DialogChangePassword(props) {
  const [currentPass, setCurrentPass] = useState("");
  const [newPass, setNewPass] = useState("");
  const [confirmPass, setConfirmPass] = useState("");
  const { handleSubmit, control, errors, setValue } = useForm({
    defaultValues: {
      currentPass,
      newPass,
      confirmPass,
    },
    resolver: yupResolver(schema),
    mode: "all",
  });
  const { user } = props;
  const dispatch = useDispatch();
  const history = useHistory();

  const isShowDialog = useSelector(
    (state) => state.common.isShowDialogChangePassword
  );
  const hideDialog = () => {
    dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_CHANGE_PASSWORD));
  };

  const handlerOnChange = (e, name, callBack) => {
    const { value } = e.target;
    setValue(name, value);
    callBack(value);
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
          label="Change Password"
          icon="pi pi-lock"
          disabled={
            !currentPass || !newPass || newPass !== confirmPass ? true : false
          }
          className="p-button-warning"
          onClick={handleSubmit(() => {
            const data = {
              oldPass: currentPass,
              newPass,
            };
            dispatch(actionChangePassword({ data, history }));
          })}
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
      <form>
        <InputField
          name="currentPass"
          control={control}
          label="Your current password"
          type="password"
          message={errors.currentPass ? errors.currentPass.message : ""}
          onChange={(e) => handlerOnChange(e, "currentPass", setCurrentPass)}
        />
        <InputField
          name="newPass"
          control={control}
          label="New password"
          message={errors.newPass ? errors.newPass.message : ""}
          onChange={(e) => handlerOnChange(e, "newPass", setNewPass)}
          type="password"
        />
        <InputField
          name="confirmPass"
          control={control}
          label="Confirm new password"
          message={errors.confirmPass ? errors.confirmPass.message : ""}
          type="password"
          onChange={(e) => handlerOnChange(e, "confirmPass", setConfirmPass)}
        />
      </form>
    </Dialog>
  );
}

export default DialogChangePassword;
