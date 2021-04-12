import { Avatar } from "primereact/avatar";
import React from "react";
import { useHistory } from "react-router";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./like.scss";
function LikeComponent(props) {
  const { likeList } = props;
  const history = useHistory();
  return likeList.map((item) => (
    <div
      key={item._id}
      className="user-like"
      onClick={() => history.push(`/user/${item._id}`)}
    >
      <Avatar
        image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${item.imgUser}`}
        imageAlt="avatar"
        className="p-mr-2"
        shape="circle"
      />
      <div>{item.displayName}</div>
    </div>
  ));
}

export default LikeComponent;
