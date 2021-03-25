import React from "react";
import "./status-template.scss";
export const StatusItemTemplate = (options) => {
  return (
    <div className={`post-status status-${options}`}>
      {options.toUpperCase().replace("_", " ")}
    </div>
  );
};
export const SelectedStatusTemplate = (option, props) => {
  if (option) {
    return (
      <div className={`post-status status-${option}`}>
        {option.toUpperCase().replace("_", " ")}
      </div>
    );
  }
  return <span>{props.placeholder}</span>;
};
export const StatusBodyTemplate = (rowData) => {
  return (
    <span className={`post-status status-${rowData.status.toLowerCase()}`}>
      {rowData.status.toUpperCase().replace("_", " ")}
    </span>
  );
};
