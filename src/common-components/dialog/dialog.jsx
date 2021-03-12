import { Button, Dialog, DialogContent, Slide } from "@material-ui/core";
import { CheckCircleOutline, ErrorOutline } from "@material-ui/icons";
import { Alert } from "@material-ui/lab";
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog } from "redux/slices/commonSlice";
import "./dialog.scss";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});
function DialogComponent() {
  const commonState = useSelector((state) => state.common);
  const { isError, isSuccess, message, isShowDialog } = commonState;
  const dispatch = useDispatch();
  const handleClose = () => {
    dispatch(actionHideDialog());
  };

  return (
    <Dialog
      open={isShowDialog}
      TransitionComponent={Transition}
      keepMounted
      className="dialog-content"
    >
      {isSuccess && (
        <DialogContent className="dialog-header">
          <div className="dialog-success">
            <CheckCircleOutline className="icon icon-success" />
            <Alert severity="success">{message}</Alert>
          </div>
        </DialogContent>
      )}

      {isError && (
        <DialogContent className="dialog-header">
          <div className="dialog-error">
            <ErrorOutline className="icon icon-error" />
            <Alert severity="error">{message}</Alert>
          </div>
          <div className="btn-close">
            <Button color="primary" onClick={handleClose}>
              Close
            </Button>
          </div>
        </DialogContent>
      )}
    </Dialog>
  );
}

export default DialogComponent;
