import React from "react";
import "./gender-template.scss";

export const GenderItemTemplate = (gender) => {
  return <span className={`gender gender-${gender}`}>{gender}</span>;
};
export const SelectedGenderTemplate = (gender, props) => {
  if (gender) {
    return <span className={`gender gender-${gender}`}>{gender}</span>;
  }
  return <span>{props.placeholder}</span>;
};

export const GenderBodyTemplate = (rowData) => {
  return (
    <span className={`gender gender-${rowData.gender}`} value={rowData.gender}>
      {rowData.gender}
    </span>
  );
};
