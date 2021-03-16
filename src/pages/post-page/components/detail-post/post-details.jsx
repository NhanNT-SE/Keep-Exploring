import React, { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { Carousel } from "primereact/carousel";
import { Button } from "primereact/button";
import { TabView, TabPanel } from "primereact/tabview";
import "./post-details.scss";
import { OverlayPanel } from "primereact/overlaypanel";
import { Tag } from "primereact/tag";
import { Dialog } from "primereact/dialog";
import { Dropdown } from "primereact/dropdown";
import { Avatar } from "primereact/avatar";
import { InputTextarea } from "primereact/inputtextarea";

function PostDetailsPage() {
  const statuses = ["pending", "done", "need_update"];
  const userList = [
    { id: "User 1", displayName: "New York", imageUser: "NY" },
    { id: "User 2", displayName: "Rome", imageUser: "RM" },
    { id: "User 3", displayName: "London", imageUser: "LDN" },
    { id: "User 4", displayName: "Istanbul", imageUser: "IST" },
    { id: "User 5", displayName: "Paris", imageUser: "PRS" },
    { id: "User 6", displayName: "Japan", imageUser: "PRS" },
    { id: "User 7", displayName: "Singapore", imageUser: "PRS" },
    { id: "User 8", displayName: "Thailand", imageUser: "PRS" },
    { id: "User 9", displayName: "HongKong", imageUser: "PRS" },
  ];
  const { postId } = useParams();
  const post = useSelector((state) => state.post.selectedPost);

  const [products, setProducts] = useState([
    {
      id: "1000",
      image:
        "https://i.picsum.photos/id/980/500/500.jpg?hmac=xxuslS3B6e6w4Bi3zur1mfICYM9HBN9Q1FvzYvG8mZ0",
    },
    {
      id: "1001",
      image:
        "https://i.picsum.photos/id/864/500/500.jpg?hmac=6oDZal362HyVtiF-frWDtY3975WBzEKjNnLAdyrHmxM",
    },
    {
      id: "1002",
      image:
        "https://i.picsum.photos/id/106/500/500.jpg?hmac=gJXKJglipb1aD_WnrD1O_wu2AQFYs8s7Izy6Yww8fp4",
    },
    {
      id: "1003",
      image:
        "https://i.picsum.photos/id/788/500/500.jpg?hmac=hmkfUVM3wbl2LlUyC7S26_oYqQ8F13V1Yh34ayBAJeI",
    },

    {
      id: "1004",
      image:
        "https://i.picsum.photos/id/662/500/500.jpg?hmac=_urzWs8zVmCmWpTWndJYlUGd2CLInnuEQ9djt7xmMUk",
    },
    {
      id: "1005",
      image:
        "https://i.picsum.photos/id/974/500/500.jpg?hmac=0-kGKWck7qYIg2J6l2HYdQ5Sc8Ws-GAtRyM6FjzVs8o",
    },
    {
      id: "1006",
      image:
        "https://i.picsum.photos/id/108/500/500.jpg?hmac=7WKPANQU4VkBIm-d4cf8GYG6stOioNalwAiLV7WTJG4",
    },
    {
      id: "1007",
      image:
        "https://i.picsum.photos/id/1054/500/500.jpg?hmac=kTXCnb-RP6lL2xuHJX0q4bETaN63R3ptOL4DEujXMiw",
    },
    {
      id: "1008",

      image:
        "https://i.picsum.photos/id/11/500/500.jpg?hmac=vBxrp0EVJJpmTzeSlOFOeBQtyfndbeDsqehwcbzRvNA",
    },
    {
      id: "1009",

      image:
        "https://i.picsum.photos/id/960/500/500.jpg?hmac=cYm3068hbCL497mLpF-M7qYMI1ri4__6jTUal98TfB0",
    },
    {
      id: "1010",

      image:
        "https://i.picsum.photos/id/100/500/500.jpg?hmac=BOkw6qJR5MG-PbDD0YehqftfKnxDnJ5EymZ9CxnwbBo",
    },
    {
      id: "1011",

      image:
        "https://i.picsum.photos/id/617/500/500.jpg?hmac=fVME9HnWsD-3FapHlsFCPs3yMpHoZXsyfRXHQcSzTY0",
    },
  ]);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [notify, setNotify] = useState(null);
  const [urlImageOverlayPanel, setUrlImageOverlayPanel] = useState("");
  const [isShowDialog, setIsShowDialog] = useState(false);
  const op = useRef(null);
  const opLike = useRef(null);
  const isMounted = useRef(false);
  const history = useHistory();
  useEffect(() => {
    // if (!post || postId != post.id.toString()) {
    //   history.push("/post");
    // }
  }, []);
  useEffect(() => {
    if (isMounted.current) {
      op.current.hide();
      opLike.current.hide();
    }
  }, []);
  useEffect(() => {
    isMounted.current = true;
  }, []);

  const onStatusChange = (e) => {
    if (e.value) {
      setSelectedStatus(e.value);
    }
  };
  const responsiveOptions = [
    {
      breakpoint: "1024px",
      numVisible: 3,
      numScroll: 3,
    },
    {
      breakpoint: "600px",
      numVisible: 2,
      numScroll: 2,
    },
    {
      breakpoint: "480px",
      numVisible: 1,
      numScroll: 1,
    },
  ];
  const hideDialog = () => {
    setIsShowDialog(false);
  };
  const showDialog = () => {
    setIsShowDialog(true);
  };
  const selectedStatusTemplate = (option, props) => {
    if (option) {
      return (
        <div className={`post-status status-${option}`}>
          {option.toUpperCase().replace("_", " ")}
        </div>
      );
    }
    return <span>{props.placeholder}</span>;
  };
  const statusItemTemplate = (option) => {
    return (
      <div className={`post-status status-${option}`}>
        {option.toUpperCase().replace("_", " ")}
      </div>
    );
  };
  const renderFooter = () => {
    return (
      <div>
        <Button label="Cancel" icon="pi pi-times" onClick={hideDialog} />
        <Button
          label="Delete"
          icon="pi pi-trash"
          className="p-button-danger"
          disabled={notify ? false : true}
          onClick={hideDialog}
        />
        <Button
          label="Submit"
          icon="pi pi-check"
          disabled={notify ? false : true}
          className="p-button-success"
          onClick={hideDialog}
        />
      </div>
    );
  };

  const UserLikeTemplate = () => {
    return userList.map((item) => (
      <div key={item.id} className="user-like" onClick={() => console.log(item)}>
        <Avatar icon="pi pi-user" className="p-mr-2" shape="circle" />
        <div>{item.displayName}</div>
      </div>
    ));
  };
  const postImageTemplate = (product) => {
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
  return (
    <div className="post-details-container">
      <div className="carousel-container">
        <Carousel
          value={products}
          numVisible={4}
          numScroll={2}
          responsiveOptions={responsiveOptions}
          itemTemplate={postImageTemplate}
        />
        <OverlayPanel
          ref={op}
          showCloseIcon
          id="overlay_panel"
          style={{
            background: urlImageOverlayPanel,
          }}
        ></OverlayPanel>
      </div>
      <div className="content-container">
        <TabView className="tab-view-custom">
          <TabPanel
            className="tab-content"
            header="Content"
            leftIcon="pi pi-calendar"
          >
            <div className="header">
              <Tag className="tag-status" severity="success" value="Done"></Tag>
              <i
                className="pi pi-thumbs-up"
                onClick={(e) => {
                  opLike.current.toggle(e);
                }}
                aria-haspopup
                aria-controls="overlay_panel_like"
              >
                <span>Likes(30)</span>
              </i>
              <OverlayPanel ref={opLike} showCloseIcon id="overlay_panel_like">
                <UserLikeTemplate />
              </OverlayPanel>
              <i className="pi pi-comments">
                <span>Comments(30)</span>
              </i>
              <Button
                icon="pi pi-pencil"
                className="p-button-rounded p-button-success p-mr-2"
                onClick={() => setIsShowDialog(true)}
              />
            </div>
            <div className="title">
              <h3>Tittle</h3>
              <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
              </p>
            </div>
            <div className="description">
              <h3>Description</h3>
              <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor incididunt ut labore et dolore magna aliqua.
              </p>
            </div>
          </TabPanel>
          <TabPanel
            header="Comment(25)"
            className="tab-comment"
            leftIcon="pi pi-comments"
          >
            <p>
              At vero eos et accusamus et iusto odio dignissimos ducimus qui
              blanditiis praesentium voluptatum deleniti atque corrupti quos
              dolores et quas molestias excepturi sint occaecati cupiditate non
              provident, similique sunt in culpa qui officia deserunt mollitia
              animi, id est laborum et dolorum fuga. Et harum quidem rerum
              facilis est et expedita distinctio. Nam libero tempore, cum soluta
              nobis est eligendi optio cumque nihil impedit quo minus.
            </p>
          </TabPanel>
        </TabView>
      </div>
      <Dialog
        header="Edit Post"
        visible={isShowDialog}
        style={{ width: "50vw" }}
        footer={renderFooter()}
        onHide={hideDialog}
        className="dialog-edit"
      >
        <div className="owner">
          <Avatar
            label="AV"
            className="p-mr-2"
            style={{ backgroundColor: "#2196F3", color: "#ffffff" }}
            shape="circle"
          />
          <p>Supper Admin</p>
        </div>
        <Dropdown
          value={selectedStatus}
          options={statuses}
          onChange={onStatusChange}
          placeholder="Select a Status"
          itemTemplate={statusItemTemplate}
          valueTemplate={selectedStatusTemplate}
        />
        <h5>Send notification for user</h5>
        <InputTextarea
          rows={5}
          cols={30}
          value={notify}
          onChange={(e) => setNotify(e.target.value)}
          placeholder="Send notification for user after update or delete this post"
          required
        />
      </Dialog>
    </div>
  );
}

export default PostDetailsPage;
