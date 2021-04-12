import React from "react";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./blog-content.scss";
function BlogContent(props) {
  const { blog } = props;
  const { blog_detail } = blog;
console.log(`url(${GLOBAL_VARIABLE.BASE_URL_IMAGE}/blog/${blog.img})`);
  return (
    <div>
      <div className="blog-content-container">
        <div
          className="image-content"
          style={{
            backgroundImage: `url(${GLOBAL_VARIABLE.BASE_URL_IMAGE}/blog/${blog.img})`,
          }}
        ></div>
        <div className="desc-content">
          <p>{blog.title}</p>
        </div>
      </div>
      {blog_detail.detail_list.map((item,index) => (
        <div className="blog-content-container" key={index} >
          <div
            className="image-content"
            style={{
              backgroundImage: `url(${item.img})`,
            }}
          ></div>
          <div className="desc-content">
            <p>{item.content}</p>
          </div>
        </div>
      ))}
    </div>
  );
}

export default BlogContent;
