import "./post-blog-template.scss";
export const PostBodyTemplate = (rowData, onPostClick) => {
  return (
    <div>
      {rowData.userInfo.postList.map((e) => (
        <div
          className="body-template-post"
          key={e}
          onClick={() => onPostClick(e)}
        >
          {e}
        </div>
      ))}
    </div>
  );
};

export const BlogBodyTemplate = (rowData, onBlogClick) => {
  return (
    <div>
      {rowData.userInfo.blogList.map((e) => (
        <div
          className="body-template-blog"
          key={e}
          onClick={() => onBlogClick(e)}
        >
          {e}
        </div>
      ))}
    </div>
  );
};
