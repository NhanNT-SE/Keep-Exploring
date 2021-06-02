import DialogChangePassword from "common-components/dialog/dialog-change-password/dialog-change-password";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";
import { InputText } from "primereact/inputtext";
import React, { useEffect, useState } from "react";
import ImageUploader from "react-images-upload";
import { useDispatch, useSelector } from "react-redux";
import { actionShowDialog } from "redux/slices/common.slice";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Switch from "@material-ui/core/Switch";
import {
  actionGetMyProfile,
  actionUpdateProfile,
} from "redux/slices/profile.slice";
import GLOBAL_VARIABLE from "utils/global_variable";
import { convertDate } from "utils/helper";
import "./profile-page.scss";
function ProfilePage() {
  const genderList = [
    { label: "Male", value: "male" },
    { label: "Female", value: "female" },
  ];
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user.selectedUser);
  const [displayName, setDisplayName] = useState("");
  const [address, setAddress] = useState("");
  const [gender, setGender] = useState("");
  const [created, setCreated] = useState("");
  const [file, setFile] = useState("");
  const [mfa, setMFA] = useState(false);
  const [imageSubmit, setImageSubmit] = useState(undefined);
  const updateProfile = () => {
    const profile = { displayName, address, gender, avatar: imageSubmit };
    dispatch(actionUpdateProfile(profile));
  };
  useEffect(() => {
    dispatch(actionGetMyProfile());
  }, []);
  useEffect(() => {
    if (user) {
      setDisplayName(user.displayName);
      setCreated(convertDate(user.created_on));
      setGender(user.gender);
      setAddress(user.address);
      if (user.mfa === "active") {
        setMFA(true);
      }
    }
  }, [user]);
  const toggleMFA = (e) => {
    const statusMFA = user.mfa;
    const isMFA = e.target.checked;
    setMFA(isMFA);
    console.log({
      statusMFA,
      isMFA,
    });
  };
  const onImage = async (e) => {
    setFile(URL.createObjectURL(e[0]));
    setImageSubmit(e[0]);
    console.log(e);
  };

  return (
    <div className="profile-page-container">
      <DialogChangePassword />
      <div className="header">
        <div className="title">My Profile</div>
      </div>

      {user && (
        <div className="container">
          <div className="container-header">
            <div className="avatar-user-container">
              <div className="avatar-user">
                <img
                  name="image_user"
                  src={
                    file
                      ? file
                      : `${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${user.avatar}`
                  }
                  alt="avatar"
                />
              </div>
              <ImageUploader
                key="image-uploader"
                singleImage={true}
                withIcon={false}
                withLabel={false}
                buttonText="Choose an image"
                onChange={onImage}
                imgExtension={[".jpg", ".png", ".jpeg"]}
                maxFileSize={5242880}
              />
            </div>

            <div className="header-info">
              <span className="display-name">{user.displayName}</span>
              <span className="email">{user.email}</span>
              <span className="role">Role: ADMINISTRATOR</span>
            </div>
            <div className="header-info">
              <FormControlLabel
                control={
                  <Switch checked={mfa} onChange={toggleMFA} name="checkedA" />
                }
                label="MFA"
              />
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
                  dispatch(
                    actionShowDialog(GLOBAL_VARIABLE.DIALOG_CHANGE_PASSWORD)
                  );
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
