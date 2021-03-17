import localStorageService from "utils/localStorageService";
import DialogMessage from "common-components/dialog/dialog-message/dialog-message";
import DrawerComponent from "common-components/drawer/drawer";
import HeaderComponent from "common-components/header/header";
import LoadingComponent from "common-components/loading/loading";
import { STYLES_GLOBAL } from "common-components/styles-global";
import BlogPage from "pages/blog-page/blog-page";
import HomePage from "pages/home-page/home-page";
import NotifyPage from "pages/notify-page/notify-page";
import PostDetailsPage from "pages/post-page/components/detail-post/post-details";
import PostPage from "pages/post-page/post-page";
import StatisticsPage from "pages/statistics-page/statistics-page";
import UserDetailsPage from "pages/user-page/components/user-details-page/user-details";
import UserPage from "pages/user-page/user-page";
import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { Redirect, Route, Switch, useHistory } from "react-router-dom";
import "./styles/main-page.scss";
function MainPage() {
  const loadingStore = useSelector((state) => state.common.isLoading);
  const user = useSelector((state) => state.user.user);
  const classes = STYLES_GLOBAL();
  const history = useHistory();
  const userStorage = JSON.parse(localStorageService.getUser());
  useEffect(() => {
    if (!userStorage) {
      history.push("/login");
    }
  }, [user]);

  return (
    <div className="main-page">
      {loadingStore && <LoadingComponent />}
      <DialogMessage />
      <HeaderComponent user={user} />
      <DrawerComponent user={user} />
      <main className={`${classes.content} page-container`}>
        <div>
          <Switch>
            <Route exact path="/home" component={HomePage}></Route>
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
            <Route exact path="/notify" component={NotifyPage}></Route>
            <Route exact path="/statistics" component={StatisticsPage}></Route>
            <Redirect from="*" to="/home" />
          </Switch>
        </div>
      </main>
    </div>
  );
}

export default MainPage;
