import DialogChangePassword from "common-components/dialog/dialog-change-password/dialog-change-password";
import DialogDisableMFA from "common-components/dialog/dialog-disable-mfa/dialog-disable-mfa";
import DialogEnableMFA from "common-components/dialog/dialog-enable-mfa/dialog-enable-mfa";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";
import { InputText } from "primereact/inputtext";
import React, { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog, actionShowDialog } from "redux/slices/dialog.slice";
import {
  actionGetMyProfile,
  actionUpdateProfile,
} from "redux/slices/profile.slice";
import { DIALOG } from "utils/global_variable";
import { convertDate } from "utils/helper";
import ProfileHeader from "./components/profile-header/profile-header";
import "./profile-page.scss";
function ProfilePage() {
  const genderList = [
    { label: "Male", value: "male" },
    { label: "Female", value: "female" },
  ];
  const dispatch = useDispatch();
  const user = useSelector((state) => state.profile.profile);
  const [displayName, setDisplayName] = useState("");
  const [address, setAddress] = useState("");
  const [gender, setGender] = useState("");
  const [created, setCreated] = useState("");
  const [imageSubmit, setImageSubmit] = useState("");
  const [mfa, setMFA] = useState(false);
  const hideDialog = useCallback(
    (typeDialog) => {
      dispatch(actionHideDialog(typeDialog));
    },
    [dispatch]
  );

  const updateProfile = () => {
    const profile = { displayName, address, gender, avatar: imageSubmit };
    dispatch(actionUpdateProfile(profile));
  };
  useEffect(() => {
    dispatch(actionGetMyProfile());
    return () => {
      hideDialog(DIALOG.DIALOG_ENABLE_MFA);
      hideDialog(DIALOG.DIALOG_DISABLE_MFA);
      hideDialog(DIALOG.DIALOG_CHANGE_PASSWORD);
    };
  }, [dispatch, hideDialog]);
  useEffect(() => {
    if (user) {
      setCreated(convertDate(user.created_on));
      setGender(user.gender);
      setAddress(user.address || "");
      setDisplayName(user.displayName || "");
      if (user.mfa === "enable") {
        setMFA(true);
      } else {
        setMFA(false);
      }
    }
  }, [user]);

  const onImageSelected = (image) => {
    setImageSubmit(image);
    console.log(image);
  };

  return (
    <div className="profile-page-container">
      <DialogChangePassword />
      <DialogEnableMFA />
      <DialogDisableMFA />
      <div className="header">
        <div className="title">My Profile</div>
      </div>
      {user && (
        <div className="container">
          <div className="container-header">
            <ProfileHeader
              user={user}
              onImageSelected={onImageSelected}
              stateMFA={mfa}
            />
          </div>
          <div className="container-info">
            <h2>Account</h2>
            <div className="p-fluid content-info">
              <div className="p-field p-grid">
                <label className="p-col-12 p-md-2">Email:</label>
                <div className="p-col-12 p-md-10">
                  <InputText
                    type="email"
                    disabled
                    value={user.email ? user.email : ""}
                  />
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
                <label className="p-col-12 p-md-2">Gender:</label>
                <div className="p-col-12 p-md-10">
                  <Dropdown
                    value={gender}
                    options={genderList}
                    onChange={(e) => setGender(e.value)}
                    placeholder="Select gender"
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
                <label className="p-col-12 p-md-2">Created on:</label>
                <div className="p-col-12 p-md-10">
                  <InputText type="text" disabled value={created} />
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
                onClick={() => {
                  dispatch(actionShowDialog(DIALOG.DIALOG_CHANGE_PASSWORD));
                }}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProfilePage;
