import {
  AppBar,
  Badge,
  IconButton,
  Toolbar,
  Typography,
} from "@material-ui/core";
import CssBaseline from "@material-ui/core/CssBaseline";
import { Menu, Mail, Notifications } from "@material-ui/icons";
import clsx from "clsx";
import { STYLES_GLOBAL } from "common-components/styles-global";
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionOpenDrawer } from "redux/slices/commonSlice";
import HeaderMenu from "./components/header-menu/header-menu";
import "./header.scss";
function HeaderComponent(props) {
  const classes = STYLES_GLOBAL();
  const open = useSelector((state) => state.common.isOpenDrawer);
  const dispatch = useDispatch();
  return (
    <div className="header-container">
      <CssBaseline />
      <AppBar
        position="fixed"
        className={clsx(classes.appBar, {
          [classes.appBarShift]: open,
        })}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={() => dispatch(actionOpenDrawer())}
            edge="start"
            className={clsx(classes.menuButton, {
              [classes.hide]: open,
            })}
          >
            <Menu />
          </IconButton>
          <Typography variant="h6" noWrap>
            Keep Exploring
          </Typography>
        </Toolbar>
        <div className="button-container">
          {/* <Badge badgeContent={4} color="secondary">
            <Mail />
          </Badge>
          <Badge badgeContent={4} color="secondary">
            <Notifications />
          </Badge> */}
          <div className="user-action">
            <HeaderMenu user={props.user} />
          </div>
        </div>
      </AppBar>
    </div>
  );
}

export default HeaderComponent;
