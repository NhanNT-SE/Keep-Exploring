import { Avatar } from "primereact/avatar";
import React from "react";
import { useHistory } from "react-router";
import "./comment.scss";
function CommentComponent(props) {
  const { commentList } = props;
  const history = useHistory();
  return commentList.map((item) => (
    <div key={item.user_id} className="comment-item">
      <div className="comment-header">
        <Avatar icon="pi pi-user" className="p-mr-2" shape="circle" />
        <p
          className="comment-user-name"
          onClick={() => history.push(`/user/${item.user_id}`)}
        >
          {item.displayName}
        </p>
      </div>
      <div className="comment-content">{item.content}</div>
    </div>
  ));
}

export default CommentComponent;
