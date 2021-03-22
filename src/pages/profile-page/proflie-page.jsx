import DialogChangePassword from "common-components/dialog/dialog-change-password/dialog-change-password";
import { Avatar } from "primereact/avatar";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionGetUser, actionUpdateProfile } from "redux/slices/userSlice";
import GLOBAL_VARIABLE from "utils/global_variable";
import localStorageService from "utils/localStorageService";
import "./profile-page.scss";

function ProfilePage() {
  const userStorage = localStorageService.getUser();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user.selectedUser);
  const [displayName, setDisplayName] = useState("");
  const [address, setAddress] = useState("");
  const [bod, setBod] = useState("");

  const updateProfile = () => {
    const profile = { displayName, address, bod };
    dispatch(actionUpdateProfile(profile));
  };
  useEffect(() => {
    if (userStorage) {
      const userId = JSON.parse(userStorage)._id;
      dispatch(actionGetUser(userId));
    }
  }, []);
  useEffect(() => {
    if (user) {
      setDisplayName(user.displayName);
      setAddress("130 Cong Hoa, Tan Binh, HCM");
      setBod("07/05/2000");
    }
  }, [user]);
  return (
    <div className="profile-page-container">
      <DialogChangePassword />
      <div className="header">
        <div className="title">My Profile</div>
      </div>
      {user && (
        <div className="container">
          <div className="container-header">
            <Avatar
              image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${user.imgUser}`}
              className="avatar-user"
              shape="circle"
            />
            <div className="header-info">
              <span className="display-name">{user.displayName}</span>
              <span className="email">{user.email}</span>
              <span className="role">Role: ADMINISTRATOR</span>
            </div>
          </div>
          <div className="container-info">
            <h2>Account</h2>
            <div className="p-fluid content-info">
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Email:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="email" disabled value={user.email} />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Display Name:</label>
                <div className="p-col-12 p-md-10">
                  <InputText
                    type="text"
                    value={displayName}
                    onChange={(e) => setDisplayName(e.target.value)}
                  />
                </div>
              </div>

              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Address:</label>
                <div className="p-col-12 p-md-10">
                  <InputText
                    type="text"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                  />
                </div>
              </div>

              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Birth Day:</label>
                <div className="p-col-12 p-md-10">
                  <InputText
                    type="text"
                    value={bod}
                    onChange={(e) => setBod(e.target.value)}
                  />
                </div>
              </div>
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Created on:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={"07/05/2000"} />
                </div>
              </div>
            </div>
            <div className="container-actions">
              <Button
                label="Update Profile"
                className="p-button-warning update-profile"
                icon="pi pi-user-edit"
                iconPos="right"
                onClick={updateProfile}
              />
              <Button
                label="Change Password"
                className="p-button-warning"
                icon="pi pi-lock"
                iconPos="right"
                onClick={() => {}}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProfilePage;
