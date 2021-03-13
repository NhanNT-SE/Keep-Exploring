import Divider from "@material-ui/core/Divider";
import Drawer from "@material-ui/core/Drawer";
import IconButton from "@material-ui/core/IconButton";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import {
  AssignmentInd,
  BarChart,
  CardTravel,
  ChevronLeft,
  ChevronRight,
  Dashboard,
  MailOutline,
  PictureInPictureAlt,
  PowerSettingsNew,
} from "@material-ui/icons";
import localStorageService from "api/localStorageService";
import clsx from "clsx";
import { STYLES_GLOBAL } from "common-components/styles-global";
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { actionCloseDrawer, actionOpenDrawer } from "redux/slices/commonSlice";
import { actionLogout } from "redux/slices/userSlice";
import DrawerTooltip from "./components/drawer-tooltip/drawer-tooltip";
import "./drawer.scss";

function DrawerComponent() {
  const classes = STYLES_GLOBAL();
  const open = useSelector((state) => state.common.isOpenDrawer);
  const [active, setActive] = useState(0);
  const dispatch = useDispatch();
  const history = useHistory();
  const toggleDrawer = () => {
    if (open) {
      return dispatch(actionCloseDrawer());
    }
    return dispatch(actionOpenDrawer());
  };
  const onItemClick = (url, index) => {
    setActive(index);
    if (url === "/logout") {
      dispatch(actionLogout());
      localStorageService.clearUser();
    } else {
      history.push(url);
    }
  };

  return (
    <div className="drawer-container">
      <Drawer
        variant="permanent"
        className={
          clsx(classes.drawer, {
            [classes.drawerOpen]: open,
            [classes.drawerClose]: !open,
          }) + " drawer-root"
        }
        classes={{
          paper: clsx({
            [classes.drawerOpen]: open,
            [classes.drawerClose]: !open,
          }),
        }}
      >
        <List>
          <div className={`${classes.toolbar} header-toolbar`}>
            ADMIN DASHBOARD
          </div>
          <Divider />
          {[
            { label: "Dashboard", url: "/home", icon: <Dashboard /> },
            { label: "User", url: "/user", icon: <AssignmentInd /> },
            { label: "Post", url: "/post", icon: <PictureInPictureAlt /> },
            { label: "Blog", url: "/blog", icon: <CardTravel /> },
            {
              label: "Notification",
              url: "/notify",
              icon: <MailOutline />,
            },
            { label: "Statistics", url: "/statistics", icon: <BarChart /> },
            { label: "Logout", url: "/logout", icon: <PowerSettingsNew /> },
          ].map((item, index) => (
            <ListItem
              button
              key={item.label}
              onClick={() => onItemClick(item.url, index)}
              className={active === index ? "active-link" : ""}
            >
              {open ? (
                <ListItemIcon>{item.icon}</ListItemIcon>
              ) : (
                <DrawerTooltip title={item.label} placement="right">
                  <ListItemIcon>{item.icon}</ListItemIcon>
                </DrawerTooltip>
              )}

              <ListItemText primary={item.label} />
            </ListItem>
          ))}
        </List>
        <Divider />
        <div className={classes.toolbar}>
          <IconButton onClick={toggleDrawer}>
            {open ? <ChevronLeft /> : <ChevronRight />}
          </IconButton>
        </div>
      </Drawer>
    </div>
  );
}

export default DrawerComponent;
