import DialogComponent from "common-components/dialog/dialog";
import DrawerComponent from "common-components/drawer/drawer";
import HeaderComponent from "common-components/header/header";
import LoadingComponent from "common-components/loading/loading";
import { STYLES_GLOBAL } from "common-components/styles-global";
import BlogPage from "pages/blog-page/blog-page";
import HomePage from "pages/home-page/home-page";
import NotifyPage from "pages/notify-page/notify-page";
import PostPage from "pages/post-page/post-page";
import StatisticsPage from "pages/statistics-page/statistics-page";
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

  useEffect(() => {
    // if (!user) {
    //   history.push("/login");
    // }
  }, [user]);

  return (
    <div className="main-page">
      {loadingStore && <LoadingComponent />}
      <DialogComponent />
      <HeaderComponent />
      <DrawerComponent />
      <main className={`${classes.content} page-container`}>
        <div>
          <Switch>
            <Route exact path="/home" component={HomePage}></Route>
            <Route exact path="/user" component={UserPage}></Route>
            <Route exact path="/post" component={PostPage}></Route>
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
