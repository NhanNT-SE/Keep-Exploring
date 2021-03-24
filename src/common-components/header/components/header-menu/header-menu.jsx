import React, { useEffect, useRef, useState } from "react";
import Button from "@material-ui/core/Button";
import ClickAwayListener from "@material-ui/core/ClickAwayListener";
import Grow from "@material-ui/core/Grow";
import Paper from "@material-ui/core/Paper";
import Popper from "@material-ui/core/Popper";
import MenuItem from "@material-ui/core/MenuItem";
import MenuList from "@material-ui/core/MenuList";
import { makeStyles } from "@material-ui/core/styles";
import { PowerSettingsNew, AccountCircle } from "@material-ui/icons";
import { Avatar, ListItemIcon, Typography } from "@material-ui/core";
import "./header-menu.scss";
import { useDispatch } from "react-redux";
import localStorageService from "utils/localStorageService";
import { actionLogout } from "redux/slices/userSlice";
import { useHistory } from "react-router";
const useStyles = makeStyles((theme) => ({
  paper: {
    marginRight: theme.spacing(2),
  },

  avatar: {
    width: theme.spacing(4),
    height: theme.spacing(4),
    cursor: "pointer",
  },
}));
function HeaderMenu(props) {
  const history = useHistory();
  const classes = useStyles();
  const [open, setOpen] = useState(false);
  const anchorRef = useRef(null);
  const { user } = props;
  const dispatch = useDispatch();
  const handleToggle = () => {
    setOpen((prevOpen) => !prevOpen);
  };

  const handleClose = (event) => {
    if (anchorRef.current && anchorRef.current.contains(event.target)) {
      return;
    }

    setOpen(false);
  };
  const logout = () => {
    dispatch(actionLogout());
    localStorageService.clearUser();
  };
  function handleListKeyDown(event) {
    if (event.key === "Tab") {
      event.preventDefault();
      setOpen(false);
    }
  }

  const prevOpen = useRef(open);
  useEffect(() => {
    if (prevOpen.current === true && open === false) {
      anchorRef.current.focus();
    }

    prevOpen.current = open;
  }, [open]);
  return (
    <div className="header-menu-container">
      <div>
        <Avatar
          ref={anchorRef}
          aria-controls={open ? "menu-list-grow" : undefined}
          aria-haspopup="true"
          className={classes.avatar}
          onClick={handleToggle}
          src={
            user ? user.imgUser : `${process.env.PUBLIC_URL}/images/avatar1.png`
          }
          alt="avatar"
        />

        <Popper
          open={open}
          anchorEl={anchorRef.current}
          role={undefined}
          transition
          disablePortal
          className="custom-menu"
        >
          {({ TransitionProps, placement }) => (
            <Grow
              {...TransitionProps}
              style={{
                transformOrigin:
                  placement === "bottom" ? "center top" : "center bottom",
              }}
            >
              <Paper>
                <ClickAwayListener onClickAway={handleClose}>
                  <MenuList
                    autoFocusItem={open}
                    id="menu-list-grow"
                    onKeyDown={handleListKeyDown}
                  >
                    <div className="info">
                      <div className="info-image">
                        <img
                          src={
                            user
                              ? user.imgUser
                              : `${process.env.PUBLIC_URL}/images/avatar1.png`
                          }
                          alt="avatar"
                        />
                      </div>
                      <div className="info-text">
                        <p className="display-name">
                          {user ? user.displayName : " Supper Admin"}
                        </p>
                        <p className="email">
                          {user
                            ? user.email
                            : " supper-admin@keep-exploring.com"}
                        </p>
                      </div>
                    </div>
                    <MenuItem
                      onClick={(event) => {
                        handleClose(event);
                        history.push("/profile");
                      }}
                    >
                      <ListItemIcon>
                        <AccountCircle />
                      </ListItemIcon>
                      <Typography variant="inherit">Account</Typography>
                    </MenuItem>
                    <MenuItem
                      onClick={(event) => {
                        handleClose(event);
                        logout();
                      }}
                    >
                      <ListItemIcon>
                        <PowerSettingsNew />
                      </ListItemIcon>
                      <Typography variant="inherit">Logout</Typography>
                    </MenuItem>
                  </MenuList>
                </ClickAwayListener>
              </Paper>
            </Grow>
          )}
        </Popper>
      </div>
    </div>
  );
}

export default HeaderMenu;
