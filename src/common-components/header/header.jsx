import { AppBar, IconButton, Toolbar, Typography } from "@material-ui/core";
import CssBaseline from "@material-ui/core/CssBaseline";
import { makeStyles } from "@material-ui/core/styles";
import MenuIcon from "@material-ui/icons/Menu";
import clsx from "clsx";
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionOpenDrawer } from "redux/slices/commonSlice";
import "./header.scss";
const useStyles = makeStyles((theme) => ({
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: 240,
    width: `calc(100% - ${240}px)`,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  hide: {
    display: "none",
  },
}));
function HeaderComponent() {
  const classes = useStyles();
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
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap>
            Mini variant drawer
          </Typography>
        </Toolbar>
      </AppBar>
    </div>
  );
}

export default HeaderComponent;
