import { Rating } from "primereact/rating";
import React from "react";

export const RatingItemTemplate = (rating) => {
  return <span>{rating} Stars</span>;
};
export const RatingSelectedItemTemplate = (rating, props) => {
  if (rating) {
    return <span>{rating} Stars</span>;
  }
  return <span>{props.placeholder}</span>;
};

export const RatingBodyTemplate = (rowData) => {
  return <Rating value={rowData.rating} readOnly cancel={false} />;
};
