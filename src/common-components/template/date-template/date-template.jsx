import React from "react";
import { convertDate } from "utils/helper";
const DateBodyTemplate = (rowData) => {
  const stringDate = convertDate(rowData.created_on);
  return <span>{stringDate}</span>;
};

export default DateBodyTemplate;
