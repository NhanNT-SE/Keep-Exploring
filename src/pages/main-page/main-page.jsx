import localStorageService from "api/localStorageService";
import LoadingComponent from "common-components/loading/loading";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { actionGetAllPost } from "redux/slices/postSlice";
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

  useEffect(() => {
    dispatch(actionGetAllPost());
    localStorageService.setLatestAction(actionGetAllPost().type);
  }, []);
  useEffect(() => {
    console.log("post list:", postList);
  }, [postList]);
  useEffect(() => {
    // if (!user) {
    //   history.push("/login");
    // }
  }, [user]);
  return (
    <div className="main-page">
      {loadingStore && <LoadingComponent />}
      <button onClick={logOut}>Logout</button>
    </div>
  );
}

export default MainPage;
