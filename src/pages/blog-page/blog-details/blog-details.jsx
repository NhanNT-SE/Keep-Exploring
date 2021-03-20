import CommentComponent from "common-components/comment/comment";
import DialogEditPost from "common-components/dialog/dialog-edit-post/dialog-edit-post";
import OverlayUserLike from "common-components/overlay-panels/ovelay-user-like/overlay-user-like";
import { Button } from "primereact/button";
import { TabPanel, TabView } from "primereact/tabview";
import { Tag } from "primereact/tag";
import React, { useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { actionGetBlog } from "redux/slices/blogSlice";
import { actionShowDialog } from "redux/slices/commonSlice";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./blog-details.scss";
function BlogDetailsPage() {
  const { blogId } = useParams();
  const dispatch = useDispatch();
  const history = useHistory();
  const opLike = useRef(null);
  const isMounted = useRef(false);
  const blog = useSelector((state) => state.blog.selectedBlog);
  useEffect(() => {
    if (isMounted.current) {
      opLike.current.hide();
    }
  }, []);
  useEffect(() => {
    isMounted.current = true;
  }, []);
  useEffect(() => {
    const payload = {
      blogId,
      history,
    };
    dispatch(actionGetBlog(payload));
  }, []);

  return (
    blog && (
      <div className="blog-details-container">
        <div className="content-container">
          <TabView className="tab-view-custom">
            <TabPanel
              className="tab-content"
              header="Content"
              leftIcon="pi pi-calendar"
            >
              <div className="header">
                <Tag
                  className={`tag-status ${blog.status}`}
                  severity="success"
                  value={blog.status.toUpperCase().replace("_", " ")}
                ></Tag>
                <i
                  className="pi pi-thumbs-up"
                  onClick={(e) => {
                    opLike.current.toggle(e);
                  }}
                  aria-haspopup
                  aria-controls="overlay_panel_like"
                >
                  <span>Likes({blog.like_list.length})</span>
                </i>
                <i className="pi pi-comments">
                  <span>{`Comments(${blog.comment.length})`}</span>
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
                    onClick={() => dispatch(actionGetBlog({ blogId, history }))}
                  />
                </div>
              </div>
              <div className="content-details">
               
              </div>
            </TabPanel>
            <TabPanel
              header={`Comment(${blog.comment.length})`}
              className="tab-comment"
              leftIcon="pi pi-comments"
            >
              <div className="comment-container">
                <CommentComponent commentList={blog.comment} />
              </div>
            </TabPanel>
          </TabView>
        </div>
        <DialogEditPost post={blog} />
        <OverlayUserLike opLike={opLike} likeList={blog.like_list} />
      </div>
    )
  );
}

export default BlogDetailsPage;
