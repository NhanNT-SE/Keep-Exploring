import DialogMessage from "common-components/dialog/dialog-message/dialog-message";
import DrawerComponent from "common-components/drawer/drawer";
import HeaderComponent from "common-components/header/header";
import LoadingComponent from "common-components/loading/loading";
import { STYLES_GLOBAL } from "common-components/styles-global";
import BlogDetailsPage from "pages/blog-page/blog-details/blog-details";
import BlogPage from "pages/blog-page/blog-page";
import HomePage from "pages/home-page/home-page";
import NotifyPage from "pages/notify-page/notify-page";
import PostDetailsPage from "pages/post-page/components/detail-post/post-details";
import PostPage from "pages/post-page/post-page";
import ProfilePage from "pages/profile-page/proflie-page";
import StatisticsPage from "pages/statistics-page/statistics-page";
import UserDetailsPage from "pages/user-page/user-details/user-details";
import UserPage from "pages/user-page/user-page";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Redirect, Route, Switch, useHistory } from "react-router-dom";
import {
  actionGetStatisticsNumber,
  actionGetStatisticsTimeLine,
} from "redux/slices/statistics.slice";
import localStorageService from "utils/localStorageService";
import "./main-page.scss";
function MainPage() {
  const loadingStore = useSelector((state) => state.common.isLoading);
  const user = useSelector((state) => state.auth.user);
  const classes = STYLES_GLOBAL();
  const history = useHistory();
  const dispatch = useDispatch();
  const userStorage = JSON.parse(localStorageService.getUser());
  useEffect(() => {
    if (!userStorage) {
      history.push("/login");
    }
  }, [user]);
  useEffect(() => {
    dispatch(actionGetStatisticsTimeLine());
    dispatch(actionGetStatisticsNumber());
  }, []);
  return (
    <div className="main-page">
      {loadingStore && <LoadingComponent />}
      <DialogMessage />
      <HeaderComponent />
      <DrawerComponent user={user} />
      <main className={`${classes.content} page-container`}>
        <div>
          <Switch>
            {/* <Route exact path="/home" component={HomePage}></Route> */}
            <Route exact path="/home" component={ProfilePage}></Route>

            <Route exact path="/user" component={UserPage}></Route>
            <Route
              exact
              path="/user/:userId"
              component={UserDetailsPage}
            ></Route>
            <Route exact path="/post" component={PostPage}></Route>
            <Route
              exact
              path="/post/:postId"
              component={PostDetailsPage}
            ></Route>
            <Route exact path="/blog" component={BlogPage}></Route>
            <Route
              exact
              path="/blog/:blogId"
              component={BlogDetailsPage}
            ></Route>
            <Route exact path="/notify" component={NotifyPage}></Route>
            <Route exact path="/statistics" component={StatisticsPage}></Route>
            <Route exact path="/profile" component={ProfilePage}></Route>
            <Redirect from="*" to="/home" />
          </Switch>
        </div>
      </main>
    </div>
  );
}

export default MainPage;
