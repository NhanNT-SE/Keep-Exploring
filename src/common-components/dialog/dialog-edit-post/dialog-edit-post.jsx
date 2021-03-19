import { StatusItemTemplate } from "common-components/template/status-template/status-template";
import { SelectedStatusTemplate } from "common-components/template/status-template/status-template";
import GLOBAL_VARIABLE from "utils/global_variable";
import { Avatar } from "primereact/avatar";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { Dropdown } from "primereact/dropdown";
import { InputTextarea } from "primereact/inputtextarea";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog } from "redux/slices/commonSlice";
import "./dialog-edit-post.scss";
import { actionUpdatePost } from "redux/slices/postSlice";
function DialogEditPost(props) {
  const { post } = props;
  const { owner } = post;
  const [selectedStatus, setSelectedStatus] = useState(null);
  const isShowDialog = useSelector(
    (state) => state.common.isShowDialogEditPost
  );
  const dispatch = useDispatch();
  const [notify, setNotify] = useState("");
  const hideDialog = () => {
    dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST));
  };
  const onStatusChange = (e) => {
    if (e.value) {
      setSelectedStatus(e.value);
    }
  };
  const updatePost = () => {
    const body = {
      idPost: post._id,
      status: selectedStatus,
      content: notify,
    };
    dispatch(actionUpdatePost(body));
  };
  const renderFooter = () => {
    return (
      <div>
        <Button label="Cancel" icon="pi pi-times" onClick={hideDialog} />
        <Button
          label="Delete"
          icon="pi pi-trash"
          className="p-button-danger"
          disabled={notify && selectedStatus ? false : true}
          onClick={hideDialog}
        />
        <Button
          label="Submit"
          icon="pi pi-check"
          disabled={notify && selectedStatus ? false : true}
          className="p-button-success"
          onClick={updatePost}
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
      className="dialog-edit"
    >
      <div className="owner">
        <Avatar
          image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${owner.imgUser}`}
          imageAlt="avatar"
          className="p-mr-2"
          style={{ backgroundColor: "#2196F3", color: "#ffffff" }}
          shape="circle"
        />
        <p>{owner.displayName}</p>
      </div>
      <Dropdown
        value={selectedStatus}
        options={GLOBAL_VARIABLE.STATUS_LIST.filter((e) => e != post.status)}
        onChange={onStatusChange}
        placeholder={post.status.toUpperCase().replace("_", " ")}
        itemTemplate={StatusItemTemplate}
        valueTemplate={SelectedStatusTemplate}
      />
      <h5>Send notification for user: "{owner.displayName}"</h5>
      <InputTextarea
        rows={5}
        cols={30}
        value={notify}
        onChange={(e) => setNotify(e.target.value)}
        placeholder="Send notification for user after update or delete this post"
        required
      />
    </Dialog>
  );
}

export default DialogEditPost;
