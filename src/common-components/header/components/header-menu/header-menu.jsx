import { Avatar, ListItemIcon, Typography } from "@material-ui/core";
import Badge from "@material-ui/core/Badge";
import ClickAwayListener from "@material-ui/core/ClickAwayListener";
import Grow from "@material-ui/core/Grow";
import MenuItem from "@material-ui/core/MenuItem";
import MenuList from "@material-ui/core/MenuList";
import Paper from "@material-ui/core/Paper";
import Popper from "@material-ui/core/Popper";
import { makeStyles } from "@material-ui/core/styles";
import { AccountCircle, PowerSettingsNew } from "@material-ui/icons";
import NotificationsActiveIcon from "@material-ui/icons/NotificationsActive";
import { OverlayPanel } from "primereact/overlaypanel";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useHistory } from "react-router";
import { actionLogout } from "redux/slices/userSlice";
import { io } from "socket.io-client";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./header-menu.scss";

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
  const [badge, setBadge] = useState(0);
  const [notifyList, setNotifyList] = useState([]);
  const [notify, setNotify] = useState(null);
  const anchorRef = useRef(null);
  const { user } = props;
  const opNotify = useRef(null);
  const isMounted = useRef(false);
  const dispatch = useDispatch();
  const BASE_URL_IMAGE = GLOBAL_VARIABLE.BASE_URL_IMAGE;
  const prevOpen = useRef(open);

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
    dispatch(actionLogout({ userId: user._id }));
  };
  const onNotifyClick = (notify) => {
    const { type, id } = notify;
    const tempList = [...notifyList];
    const index = tempList.findIndex((e) => e.id === notify.id);
    if (index >= 0) {
      tempList.splice(index, 1);
    }
    setNotifyList(tempList);
    if (type === "post") {
      history.push(`/post/${id}`);
      return;
    }
    history.push(`/blog/${id}`);
  };

  function handleListKeyDown(event) {
    if (event.key === "Tab") {
      event.preventDefault();
      setOpen(false);
    }
  }

  useEffect(() => {
    if (prevOpen.current === true && open === false) {
      anchorRef.current.focus();
    }

    prevOpen.current = open;
  }, [open]);
  useEffect(() => {
    if (isMounted.current) {
      opNotify.current.hide();
    }
    isMounted.current = true;

    const socket = io(GLOBAL_VARIABLE.BASE_URL);
    socket.on("notification:admin", (data) => {
      setNotify(data);
    });

    return () => {
      socket.disconnect();
    };
  }, []);
  useEffect(() => {
    if (notify) {
      const tempList = [...notifyList];
      tempList.push(notify);
      setNotifyList(tempList);
      setBadge(badge + 1);
    }
  }, [notify]);

  return (
    <div className="header-menu-wrapper">
      <div
        className="badge-container"
        onClick={(e) => {
          opNotify.current.toggle(e);
          setBadge(0);
        }}
      >
        <Badge badgeContent={badge} color="secondary">
          <NotificationsActiveIcon />
        </Badge>
      </div>
      <div className="header-menu-container">
        <div>
          <Avatar
            ref={anchorRef}
            aria-controls={open ? "menu-list-grow" : undefined}
            aria-haspopup="true"
            className={classes.avatar}
            onClick={handleToggle}
            src={
              user
                ? user.avatar
                : `${process.env.PUBLIC_URL}/images/avatar1.png`
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
                                ? user.avatar
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
      <OverlayPanel ref={opNotify} showCloseIcon id="overlay_panel_notify">
        <div className="notify-list-wrapper">
          {notifyList.map((e, index) => {
            return (
              <div
                className="overLay-notify-container"
                key={index}
                onClick={() => onNotifyClick(e)}
              >
                <Avatar
                  alt="Notification"
                  src={`${BASE_URL_IMAGE}/${e.type}/${e.image}`}
                />
                <span>{e.message}</span>
              </div>
            );
          })}
        </div>
      </OverlayPanel>
    </div>
  );
}

export default HeaderMenu;
