import React from "react";
export const DateBodyTemplate = (rowData) => {
  const date = new Date(rowData.created_on);
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const year = date.getFullYear();
  let dayString = "";
  let monthString = "";
  if (day > 10) {
    dayString = day;
  } else {
    dayString = "0" + day;
  }
  if (month > 10) {
    monthString = month;
  } else {
    monthString = "0" + month;
  }
  const stringDate = dayString + "-" + monthString + "-" + year;
  return <span>{stringDate}</span>;
};

export default DateBodyTemplate;
