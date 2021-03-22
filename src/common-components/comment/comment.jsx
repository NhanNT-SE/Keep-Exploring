import { Avatar } from "primereact/avatar";
import { Button } from "primereact/button";
import React, { useEffect, useState } from "react";
import { useHistory } from "react-router";
import GLOBAL_VARIABLE from "utils/global_variable";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";
import "./comment.scss";
import { useDispatch } from "react-redux";
import { actionDeleteComment } from "redux/slices/blogSlice";
function CommentComponent(props) {
  const { commentList, type } = props;
  const history = useHistory();
  const dispatch = useDispatch();
  const [list, setList] = useState(commentList);

  const dateTemplate = (dateInput) => {
    const date = new Date(dateInput);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const year = date.getFullYear();
    let dayString = "";
    let monthString = "";
    if (day > 10) {
      dayString = day;
    } else {
      dayString = "0" + day;
    }
    if (month > 10) {
      monthString = month;
    } else {
      monthString = "0" + month;
    }
    const stringDate = dayString + "-" + monthString + "-" + year;
    return stringDate;
  };
  useEffect(() => {
    // 6055a4b2b69c7033b4c89dee 6055a4ceb69c7033b4c89df0

    console.log("temp list:", list);
  }, [list]);
  const confirmDelete = (commentId) => {
    confirmDialog({
      message: "Do you want to delete this comment?",
      header: "Delete Comment",
      baseZIndex: 1000,
      accept: () => {
        dispatch(actionDeleteComment(commentId));
        const tempList = list.filter((e) => e._id !== commentId);
        setList(tempList);
      },
      reject: () => {},
    });
  };
  return list.map((item) => (
    <div key={item._id} className="comment-item">
      <div className="comment-header">
        <Avatar
          image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${item.idUser.imgUser}`}
          imageAlt="avatar"
          className="p-mr-2"
          shape="circle"
        />
        <p
          className="comment-user-name"
          onClick={() => history.push(`/user/${item.idUser._id}`)}
        >
          {item.idUser.displayName}
        </p>
        <p className="date-comment">{dateTemplate(item.date)}</p>
        <div className="actions-comment">
          <Button
            className="p-button-danger"
            onClick={() => {
              confirmDelete(item._id);
            }}
            icon="pi pi-trash"
          />
        </div>
      </div>
      <div className="comment-content">{item.content}</div>
      {item.img && (
        <div
          className="comment-image"
          style={{
            background: `url(${GLOBAL_VARIABLE.BASE_URL_IMAGE}/comment/${type}/${item.img})`,
          }}
        />
      )}
    </div>
  ));
}

export default CommentComponent;
