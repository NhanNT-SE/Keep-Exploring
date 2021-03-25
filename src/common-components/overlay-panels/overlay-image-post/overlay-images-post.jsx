import { OverlayPanel } from "primereact/overlaypanel";
import React from "react";
import "./overlay-image-post.scss";
function OverLayPanelImagePost(props) {
  const { op, urlImageOverlayPanel } = props;
  return (
    <OverlayPanel
      ref={op}
      showCloseIcon
      id="overlay_panel"
      style={{
        background: urlImageOverlayPanel,
      }}
    ></OverlayPanel>
  );
}

export default OverLayPanelImagePost;
