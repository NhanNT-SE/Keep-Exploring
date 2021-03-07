import React from "react";
import "./not-found.scss";
function NotFound() {
  return (
    <div className="not-found-container">
      <img src={process.env.PUBLIC_URL + "/images/page-not-found.png"} />;
    </div>
  );
}

export default NotFound;
