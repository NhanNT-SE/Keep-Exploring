import DialogComponent from "common-components/dialog/dialog";
import DrawerComponent from "common-components/drawer/drawer";
import HeaderComponent from "common-components/header/header";
import LoadingComponent from "common-components/loading/loading";
import React from "react";
import { useSelector } from "react-redux";
import "./styles/main-page.scss";
function MainPage() {
  const loadingStore = useSelector((state) => state.common.isLoading);
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
