import React from "react";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./blog-content.scss";
function BlogContent(props) {
  const { blogDetail } = props;
  return (
    <div className="blog-content-container">
      <div
        className="image-content"
        style={{
          backgroundImage: `url(${GLOBAL_VARIABLE.BASE_URL_IMAGE}/blog/${blogDetail.img})`,
        }}
      ></div>
      <div className="desc-content">
        <p>{blogDetail.content}</p>
      </div>
    </div>
  );
}

export default BlogContent;
