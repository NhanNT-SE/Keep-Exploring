import React from "react";
import "./display-name-template.scss";
function DisplayNameBodyTemplate(rowData, onItemClick) {
  return (
    <span
      className="display-name-body-template"
      onClick={() => {
        onItemClick(rowData.owner._id);
      }}
    >
      {rowData.owner.displayName}
    </span>
  );
}

export default DisplayNameBodyTemplate;
