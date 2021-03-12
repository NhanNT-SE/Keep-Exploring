import { Badge } from "@material-ui/core";
import Divider from "@material-ui/core/Divider";
import Drawer from "@material-ui/core/Drawer";
import IconButton from "@material-ui/core/IconButton";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import { makeStyles } from "@material-ui/core/styles";
import {
  AssignmentInd,
  BarChart,
  CardTravel,
  ChevronLeft,
  ChevronRight,
  Dashboard,
  Mail,
  PictureInPictureAlt,
  PowerSettingsNew,
} from "@material-ui/icons";
import localStorageService from "api/localStorageService";
import clsx from "clsx";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { actionCloseDrawer, actionOpenDrawer } from "redux/slices/commonSlice";
import { actionLogout } from "redux/slices/userSlice";
import "./drawer.scss";
const useStyles = makeStyles((theme) => ({
  drawer: {
    width: 240,
    flexShrink: 0,
    whiteSpace: "nowrap",
  },
  drawerOpen: {
    width: 240,
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerClose: {
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: "hidden",
    width: theme.spacing(7) + 1,
    [theme.breakpoints.up("sm")]: {
      width: theme.spacing(9) + 1,
    },
  },
  toolbar: {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
  },
}));
function DrawerComponent() {
  const user = useSelector((state) => state.user.user);
  const classes = useStyles();
  const open = useSelector((state) => state.common.isOpenDrawer);
  const dispatch = useDispatch();
  const history = useHistory();
  const toggleDrawer = () => {
    if (open) {
      return dispatch(actionCloseDrawer());
    }
    return dispatch(actionOpenDrawer());
  };
  const onItemClick = (url) => {
    if (url === "/logout") {
      dispatch(actionLogout());
      localStorageService.clearUser();
    }
  };
  useEffect(() => {
    if (!user) {
      history.push("/login");
    }
  }, [user]);
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
        <div className={`${classes.toolbar} header-toolbar`}>
          ADMIN DASHBOARD
        </div>
        <Divider />
        <List>
          {[
            { label: "Dashboard", url: "/home", icon: <Dashboard /> },
            { label: "User", url: "/user", icon: <AssignmentInd /> },
            { label: "Post", url: "/post", icon: <PictureInPictureAlt /> },
            { label: "Blog", url: "/blog", icon: <CardTravel /> },
            {
              label: "Notify",
              url: "/notify",
              icon: (
                <Badge badgeContent={4} color="secondary">
                  <Mail />
                </Badge>
              ),
            },
            { label: "Statistics", url: "/statistics", icon: <BarChart /> },
            { label: "Logout", url: "/logout", icon: <PowerSettingsNew /> },
          ].map((item) => (
            <ListItem
              button
              key={item.label}
              onClick={() => onItemClick(item.url)}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
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
