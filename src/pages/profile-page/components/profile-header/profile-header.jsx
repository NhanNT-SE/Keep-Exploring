import FormControlLabel from "@material-ui/core/FormControlLabel";
import Switch from "@material-ui/core/Switch";
import DialogChangePassword from "common-components/dialog/dialog-change-password/dialog-change-password";
import DialogEnableMFA from "common-components/dialog/dialog-enable-mfa/dialog-enable-mfa";
import DialogDisableMFA from "common-components/dialog/dialog-disable-mfa/dialog-disable-mfa";
import { Button } from "primereact/button";
import { Dropdown } from "primereact/dropdown";
import { InputText } from "primereact/inputtext";
import React, { useEffect, useState } from "react";
import ImageUploader from "react-images-upload";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog, actionShowDialog } from "redux/slices/dialog.slice";
import {
  actionGetMyProfile,
  actionUpdateProfile,
} from "redux/slices/profile.slice";
import { DIALOG } from "utils/global_variable";
import { convertDate } from "utils/helper";
import "./profile-header.scss";
function ProfileHeader(props) {
  const dispatch = useDispatch();
  const { user, onImageSelected } = props;
  const { userInfo } = user;
  const [file, setFile] = useState("");
  const [mfa, setMFA] = useState(false);
  const [imageSubmit, setImageSubmit] = useState(null);
  //   const updateProfile = () => {
  //     const profile = { displayName, address, gender, avatar: imageSubmit };
  //     dispatch(actionUpdateProfile(profile));
  //   };
  useEffect(() => {
    dispatch(actionGetMyProfile());

    return () => {
      hideDialog(DIALOG.DIALOG_ENABLE_MFA);
      hideDialog(DIALOG.DIALOG_DISABLE_MFA);
      hideDialog(DIALOG.DIALOG_CHANGE_PASSWORD);
    };
  }, []);
  useEffect(() => {
    if (user) {
      setStateMFA();
    }
  }, [user]);
  const setStateMFA = () => {
    const { mfa } = user;
    if (mfa === "enable") {
      setMFA(true);
    } else {
      setMFA(false);
    }
  };
  const toggleMFA = (e) => {
    const isMFA = e.target.checked;
    if (isMFA && user.mfa === "disable") {
      hideDialog(DIALOG.DIALOG_DISABLE_MFA);
      showDialog(DIALOG.DIALOG_ENABLE_MFA);
    } else {
      hideDialog(DIALOG.DIALOG_ENABLE_MFA);
      showDialog(DIALOG.DIALOG_DISABLE_MFA);
    }
  };
  const showDialog = (typeDialog) => {
    dispatch(actionShowDialog(typeDialog));
  };
  const hideDialog = (typeDialog) => {
    dispatch(actionHideDialog(typeDialog));
  };
  const onImage = (e) => {
    setFile(URL.createObjectURL(e[0]));
    setImageSubmit(e[0]);
    if (onImageSelected) {
      onImageSelected(e[0]);
    }
  };

  return (
    <div className="profile-header-container">
      {user && userInfo && (
        <div className="main-container">
          <div className="avatar-user-container">
            <div className="avatar-user">
              <img
                name="image_user"
                src={file ? file : user.avatar}
                alt="avatar"
              />
            </div>
            <ImageUploader
              key="image-uploader"
              singleImage={true}
              withIcon={false}
              withLabel={false}
              buttonText="Change avatar"
              onChange={onImage}
              imgExtension={[".jpg", ".png", ".jpeg"]}
              maxFileSize={5242880}
            />
          </div>

          <div className="header-info">
            <div className="info-container">
              <span className="display-name">
                Full Name: {user.displayName}
              </span>
            </div>
            <div className="info-container">
              <span className="display-name">Username: {user.username}</span>
            </div>
            <div className="info-container">
              <p>
                Email: <span className="email">{user.email}</span>{" "}
              </p>
            </div>
          </div>
          <div className="header-info">
            <div className="info-container">
              <span>Role: ADMINISTRATOR</span>
            </div>
            <div className="info-container">
              <span>Rank: {userInfo.rank}</span>
            </div>
            <div className="info-container">
              <span>Stars: {userInfo.stars}</span>
            </div>
          </div>
          <div className="header-info">
            <div className="info-container mfa">
              <span>MFA:&emsp;&emsp;</span>
              <FormControlLabel
                control={
                  <Switch checked={mfa} onChange={toggleMFA} color="primary" />
                }
              />
            </div>
            <div className="info-container">
              <span>Account: {userInfo.accountStatus}</span>
            </div>
            <div className="info-container">
              <span>Status: {userInfo.onlineStatus}</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProfileHeader;
