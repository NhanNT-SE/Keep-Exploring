import { Avatar } from "primereact/avatar";
import React from "react";
import { useHistory } from "react-router";
import "./like.scss";
function LikeComponent(props) {
  const { likeList } = props;
  const history = useHistory();
  return likeList.map((item) => (
    <div
      key={item.user_id}
      className="user-like"
      onClick={() => history.push(`/user/${item.user_id}`)}
    >
      <Avatar icon="pi pi-user" className="p-mr-2" shape="circle" />
      <div>{item.displayName}</div>
    </div>
  ));
}

export default LikeComponent;
