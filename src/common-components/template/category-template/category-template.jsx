import React from "react";

export const CategoryItemTemplate = (option, props) => {
  if (option) {
    return <span>{option.toUpperCase().replace("_", " ")}</span>;
  }
  return <span>{props.placeholder}</span>;
};
export const CategoryItemSelectedTemplate = (option, props) => {
  if (option) {
    return <span>{option.toUpperCase().replace("_", " ")}</span>;
  }
  return <span>{props.placeholder}</span>;
};
