import { Avatar } from "primereact/avatar";
import { Button } from "primereact/button";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { actionGetUser } from "redux/slices/userSlice";
import { InputText } from "primereact/inputtext";
import "./user-details.scss";
import GLOBAL_VARIABLE from "utils/global_variable";
import { actionShowDialog } from "redux/slices/commonSlice";
import DialogNotify from "common-components/dialog/dialog-notify/dialog-notify";

function UserDetailsPage() {
  const { userId } = useParams();
  const dispatch = useDispatch();
  const history = useHistory();
  const user = useSelector((state) => state.user.selectedUser);

  useEffect(() => {
    dispatch(actionGetUser(userId));
  }, []);
  useEffect(() => {
    console.log("user profile:", user);
  }, [user]);
  return (
    <div className="user-details-container">
      <div className="header">
        <div className="icon-back">
          <i
            onClick={() => history.goBack()}
            className="pi pi-chevron-left"
          ></i>
        </div>
        <div className="title">User Profile</div>
      </div>
      {user && (
        <div className="container">
          <DialogNotify user={user} />
          <div className="container-header">
            <Avatar
              image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${user.imgUser}`}
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
              />
            </div>
          </div>
          <div className="container-info">
            <h2>Account</h2>
            <div className="p-fluid content-info">
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Account ID:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" value={user._id} disabled />
                </div>
              </div>

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
                <label className="p-col-12 p-md-2">Password:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="password" disabled value={user.pass} />
                </div>
              </div>

              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Post:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={user.post.length} />
                </div>
              </div>

              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Blog:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={user.blog.length} />
                </div>
              </div>

              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Created on:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={"07/05/2000"} />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Address:</label>
                <div className="p-col-12 p-md-10">
                  <InputText
                    type="text"
                    disabled
                    value={"150 Cong Hoa, Tan Binh, HCM"}
                  />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Birth Day:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={"07/07/2000"} />
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
