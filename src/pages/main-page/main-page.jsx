import { makeStyles } from "@material-ui/core/styles";
import localStorageService from "api/localStorageService";
import DialogComponent from "common-components/dialog/dialog";
import DrawerComponent from "common-components/drawer/drawer";
import HeaderComponent from "common-components/header/header";
import LoadingComponent from "common-components/loading/loading";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { actionLogout } from "redux/slices/userSlice";
import "./styles/main-page.scss";
function MainPage() {
  const user = useSelector((state) => state.user.user);
  const loadingStore = useSelector((state) => state.common.isLoading);
  const postList = useSelector((state) => state.post.postList);
  const history = useHistory();
  const dispatch = useDispatch();

  const logOut = () => {
    dispatch(actionLogout());
    localStorageService.clearUser();
  };

  return (
    <div className="main-page">
      {loadingStore && <LoadingComponent />}
      <DialogComponent />
      <HeaderComponent />
      <DrawerComponent />
      <main>
        <div />
        <div className="content">Hello</div>
      </main>
    </div>
  );
}

export default MainPage;
