import LikeComponent from "common-components/like/like";
import { OverlayPanel } from "primereact/overlaypanel";
import React from "react";
import "./ovelay-user-like.scss";
function OverlayUserLike(props) {
  const { opLike, likeList } = props;
  return (
    <OverlayPanel ref={opLike} showCloseIcon id="overlay_panel_like">
      <LikeComponent likeList={likeList} />
    </OverlayPanel>
  );
}

export default OverlayUserLike;
