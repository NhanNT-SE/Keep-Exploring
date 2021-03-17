import { useState } from "react";
import "./post-image-template.scss";
export const PostImageTemplate = (props) => {
  const {product,op, setUrlImageOverlayPanel} = props;
  return (
    <div className="product-item">
      <div className="product-item-content">
        <div
          className="image"
          style={{
            backgroundImage: `url(${product.image})`,
          }}
          onClick={(e) => {
            setUrlImageOverlayPanel(`url(${product.image})`);
            op.current.toggle(e);
          }}
          aria-haspopup
          aria-controls="overlay_panel"
        ></div>
      </div>
    </div>
  );
};
