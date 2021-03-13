import { Tooltip } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import React from "react";
const drawerTooltip = makeStyles(() => ({
  popper: {
    left: "-18px !important",
  },
  tooltip: {
    borderRadius: "0px",
    backgroundColor: "#f5f5f5",
    color: "#4272d7",
    margin: "0px",
    lineHeight: "4.2em",
    width: "80px",
    fontWeight:"bold",
  },
}));
function DrawerTooltip(props) {
  const classes = drawerTooltip();
  return <Tooltip classes={classes} {...props} />;
}

export default DrawerTooltip;
