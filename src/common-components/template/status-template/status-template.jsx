import React from "react";
import "./status-template.scss";
export const StatusItemTemplate = (props) => {
  return (
    <div className={`post-status status-${props.option}`}>
      {props.option.toUpperCase().replace("_", " ")}
    </div>
  );
};
export const SelectedStatusTemplate = (props) => {
  const { option, placeholder } = props;
  if (option) {
    return (
      <div className={`post-status status-${option}`}>
        {option.toUpperCase().replace("_", " ")}
      </div>
    );
  }
  return <span>{placeholder}</span>;
};
