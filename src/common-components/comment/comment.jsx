import { Avatar } from "primereact/avatar";
import React from "react";
import { useHistory } from "react-router";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./comment.scss";
function CommentComponent(props) {
  const { commentList } = props;
  const history = useHistory();
  return commentList.map((item) => (
    <div key={item._id} className="comment-item">
      <div className="comment-header">
        <Avatar
          image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${item.idUser.imgUser}`}
          imageAlt="avatar"
          className="p-mr-2"
          shape="circle"
        />
        <p
          className="comment-user-name"
          onClick={() => history.push(`/user/${item.idUser._id}`)}
        >
          {item.idUser.displayName}
        </p>
      </div>
      <div className="comment-content">{item.content}</div>
      {item.img && (
        <div
          className="comment-image"
          style={{
            background: `url(${GLOBAL_VARIABLE.BASE_URL_IMAGE}/comment/post/${item.img})`,
          }}
        />
      )}
    </div>
  ));
}

export default CommentComponent;
