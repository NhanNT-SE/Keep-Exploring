import CommentComponent from "common-components/comment/comment";
import DialogEditPost from "common-components/dialog/dialog-edit-post/dialog-edit-post";
import OverlayUserLike from "common-components/overlay-panels/ovelay-user-like/overlay-user-like";
import OverLayPanelImagePost from "common-components/overlay-panels/overlay-image-post/overlay-images-post";
import { PostImageTemplate } from "common-components/template/post-image-template/post-image-template";
import GLOBAL_VARIABLE from "utils/global_variable";
import { Button } from "primereact/button";
import { Carousel } from "primereact/carousel";
import { TabPanel, TabView } from "primereact/tabview";
import { Tag } from "primereact/tag";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { actionShowDialog } from "redux/slices/commonSlice";
import "./post-details.scss";
import { actionGetPost } from "redux/slices/postSlice";
function PostDetailsPage() {
  const { postId } = useParams();
  const post = useSelector((state) => state.post.selectedPost);
  const commentList = useSelector((state) => state.post.commentList);
  const [urlImageOverlayPanel, setUrlImageOverlayPanel] = useState("");
  const op = useRef(null);
  const opLike = useRef(null);
  const isMounted = useRef(false);
  const history = useHistory();
  const dispatch = useDispatch();
  useEffect(() => {
    if (isMounted.current) {
      op.current.hide();
      opLike.current.hide();
    }
  }, []);
  useEffect(() => {
    isMounted.current = true;
  }, []);
  useEffect(() => {
    const payload = {
      postId: postId,
      history: history,
    };
    dispatch(actionGetPost(payload));
  }, []);
  return (
    post &&
    commentList && (
      <div className="post-details-container">
        <div className="carousel-container">
          <Carousel
            value={post.imgs}
            numVisible={post.imgs.length >= 4 ? 3 : post.imgs.length}
            numScroll={2}
            responsiveOptions={GLOBAL_VARIABLE.RESPONSIVE_OPTIONS}
            itemTemplate={(img) => (
              <PostImageTemplate
                img={img}
                op={op}
                setUrlImageOverlayPanel={setUrlImageOverlayPanel}
              />
            )}
          />
        </div>
        <div className="content-container">
          <TabView className="tab-view-custom">
            <TabPanel
              className="tab-content"
              header="Content"
              leftIcon="pi pi-calendar"
            >
              <div className="header">
                <Tag
                  className={`tag-status ${post.status}`}
                  severity="success"
                  value={post.status.toUpperCase().replace("_", " ")}
                ></Tag>
                <i
                  className="pi pi-thumbs-up"
                  onClick={(e) => {
                    opLike.current.toggle(e);
                  }}
                  aria-haspopup
                  aria-controls="overlay_panel_like"
                >
                  <span>Likes({post.like_list.length})</span>
                </i>
                <i className="pi pi-comments">
                  <span>{`Comments(${post.comment.length})`}</span>
                </i>
                <div className="btn-actions">
                  <Button
                    icon="pi pi-chevron-left"
                    className="p-button-rounded p-button-success p-mr-2"
                    onClick={() => history.goBack()}
                  />
                  <Button
                    icon="pi pi-pencil"
                    className="p-button-rounded p-button-success p-mr-2"
                    onClick={() =>
                      dispatch(
                        actionShowDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST)
                      )
                    }
                  />
                  <Button
                    icon="pi pi-refresh"
                    className="p-button-rounded p-button-success p-mr-2"
                    onClick={() => dispatch(actionGetPost({ postId, history }))}
                  />
                </div>
              </div>
              <div className="title">
                <h3>Tittle</h3>
                <p>{post.title}</p>
              </div>
              <div className="description">
                <h3>Description</h3>
                <p>{post.desc}</p>
              </div>
            </TabPanel>
            <TabPanel
              header={`Comment(${post.comment.length})`}
              className="tab-comment"
              leftIcon="pi pi-comments"
            >
              <div className="comment-container">
                <CommentComponent commentList={commentList} type="post" />
              </div>
            </TabPanel>
          </TabView>
        </div>
        <DialogEditPost post={post} />
        <OverlayUserLike opLike={opLike} likeList={post.like_list} />
        <OverLayPanelImagePost
          op={op}
          urlImageOverlayPanel={urlImageOverlayPanel}
        />
      </div>
    )
  );
}

export default PostDetailsPage;
