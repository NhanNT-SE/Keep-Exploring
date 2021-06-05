import { CONFIG_URL } from "utils/global_variable";
import "./post-image-template.scss";
export const PostImageTemplate = (props) => {
  const { img, op, setUrlImageOverlayPanel } = props;
  return (
    <div className="product-item">
      <div className="product-item-content">
        <div
          className="image"
          style={{
            backgroundImage: `url(${CONFIG_URL.BASE_URL_IMAGE}/post/${img})`,
          }}
          onClick={(e) => {
            setUrlImageOverlayPanel(
              `url(${CONFIG_URL.BASE_URL_IMAGE}/post/${img})`
            );
            op.current.toggle(e);
          }}
          aria-haspopup
          aria-controls="overlay_panel"
        ></div>
      </div>
    </div>
  );
};
