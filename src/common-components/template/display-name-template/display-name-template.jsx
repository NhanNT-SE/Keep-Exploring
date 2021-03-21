import React from "react";
import { useHistory } from "react-router";
import "./display-name-template.scss";
function DisplayNameBodyTemplate(rowData, onItemClick) {
  //   const history = useHistory();

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
