import { CircularProgress } from "@material-ui/core";
import React from "react";
import "./loading.scss";
function LoadingComponent() {
  return (
    <div className="loading-container">
      <CircularProgress color="secondary" />
    </div>
  );
}

export default LoadingComponent;
