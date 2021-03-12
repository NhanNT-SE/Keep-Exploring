import React from "react";
import "./not-found.scss";
function NotFound() {
  return (
    <div className="not-found-container">
      <img src={process.env.PUBLIC_URL + "/images/page-not-found.png"} alt="not found" />;
    </div>
  );
}

export default NotFound;
