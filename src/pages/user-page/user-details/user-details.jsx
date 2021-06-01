import { Avatar } from "primereact/avatar";
import { Button } from "primereact/button";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { actionGetUser } from "redux/slices/userSlice";
import { InputText } from "primereact/inputtext";
import "./user-details.scss";
import GLOBAL_VARIABLE from "utils/global_variable";
import { actionHideDialog, actionShowDialog } from "redux/slices/commonSlice";
import DialogNotify from "common-components/dialog/dialog-notify/dialog-notify";
import DialogDeleteUser from "common-components/dialog/dialog-delete-user/dialog-delete-user";
import { convertDate } from "utils/helper";
function UserDetailsPage() {
  const { userId } = useParams();
  const dispatch = useDispatch();
  const history = useHistory();
  const user = useSelector((state) => state.user.selectedUser);
  const [created, setCreated] = useState("");
  const [bod, setBod] = useState("");
  const [address, setAddress] = useState("");

  useEffect(() => {
    dispatch(actionGetUser(userId));
    return () => {
      dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
      dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_DELETE_USER));
    };
  }, []);
  useEffect(() => {
    if (user) {
      setCreated(convertDate(user.created_on));
      setBod(convertDate(user.bod));
      if (user.address) {
        setAddress(user.address);
      }
    }
  }, [user]);
  return (
    <div className="user-details-container">
      <div className="header">
        <div className="icon-back">
          <i
            onClick={() => history.push("/user")}
            className="pi pi-chevron-left"
          ></i>
        </div>
        <div className="title">User Profile</div>
      </div>
      {user && (
        <div className="container">
          <DialogNotify userList={[user._id]} />
          <DialogDeleteUser user={user} />
          <div className="container-header">
            <Avatar
              image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${user.avatar}`}
              className="avatar-user"
              shape="circle"
            />
            <div className="header-info">
              <span className="display-name">{user.displayName}</span>
              <span className="email">{user.email}</span>
              <span className="role">Role: USER</span>
            </div>
            <div className="header-actions">
              <Button
                label="Send Notification"
                className="p-button-warning"
                icon="pi pi-send"
                iconPos="right"
                onClick={() =>
                  dispatch(actionShowDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY))
                }
              />
              <Button
                label="Delete Account"
                className="p-button-danger"
                icon="pi pi-trash"
                iconPos="right"
                onClick={() =>
                  dispatch(actionShowDialog(GLOBAL_VARIABLE.DIALOG_DELETE_USER))
                }
              />
            </div>
          </div>
          <div className="container-info">
            <h2>Account</h2>
            <div className="p-fluid content-info">
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Display Name:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={user.displayName} />
                </div>
              </div>

              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Email:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="email" disabled value={user.email} />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Post:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={user.userInfo.postList.length} />
                </div>
              </div>

              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Blog:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={user.userInfo.blogList.length} />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Gender:</label>
                <div className="p-col-12 p-md-10">
                  <InputText
                    type="text"
                    value={user.gender.toUpperCase()}
                    disabled
                  />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Created on:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={created} />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Address:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={address} />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Birth Day:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={bod} />
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default UserDetailsPage;
