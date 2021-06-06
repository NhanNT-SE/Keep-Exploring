import CommentComponent from "common-components/comment/comment";
import DialogEditPost from "common-components/dialog/dialog-edit-post/dialog-edit-post";
import OverlayUserLike from "common-components/overlay-panels/ovelay-user-like/overlay-user-like";
import OverLayPanelImagePost from "common-components/overlay-panels/overlay-image-post/overlay-images-post";
import { PostImageTemplate } from "common-components/template/post-image-template/post-image-template";
import { Button } from "primereact/button";
import { Carousel } from "primereact/carousel";
import { TabPanel, TabView } from "primereact/tabview";
import { Tag } from "primereact/tag";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import {
  actionGetCommentList,
  actionGetLikeList,
} from "redux/slices/comment.slice";
import { actionHideDialog, actionShowDialog } from "redux/slices/dialog.slice";
import { actionGetPost } from "redux/slices/post.slice";
import { DIALOG, RESPONSIVE_OPTIONS } from "utils/global_variable";
import "./post-details.scss";
function PostDetailsPage() {
  const { postId } = useParams();
  const post = useSelector((state) => state.post.selectedPost);
  const commentState = useSelector((state) => state.comment);
  const { commentList, likeList } = commentState;
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
    isMounted.current = true;
    dispatch(
      actionGetPost({
        postId: postId,
        history: history,
      })
    );
    dispatch(
      actionGetCommentList({
        type: "post",
        id: postId,
      })
    );
    dispatch(actionGetLikeList({ type: "post", body: { idPost: postId } }));
    return () => {
      dispatch(actionHideDialog(DIALOG.DIALOG_EDIT_POST));
    };
  }, [dispatch, history, postId]);

  return (
    post && (
      <div className="post-details-container">
        <div className="carousel-container">
          <Carousel
            value={post.imgs}
            numVisible={post.imgs.length >= 4 ? 3 : post.imgs.length}
            numScroll={2}
            responsiveOptions={RESPONSIVE_OPTIONS}
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
                      dispatch(actionShowDialog(DIALOG.DIALOG_EDIT_POST))
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
                {commentList && (
                  <CommentComponent commentList={commentList} type="post" />
                )}
              </div>
            </TabPanel>
          </TabView>
        </div>
        <DialogEditPost post={post} />
        <OverlayUserLike opLike={opLike} likeList={likeList ? likeList : []} />
        <OverLayPanelImagePost
          op={op}
          urlImageOverlayPanel={urlImageOverlayPanel}
        />
      </div>
    )
  );
}

export default PostDetailsPage;
