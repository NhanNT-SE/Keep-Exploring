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
function PostDetailsPage() {
  const likeList = [
    {
      user_id: "keep-exploring-user-01",
      displayName: "New York",
      imageUser: "NY",
    },
    { user_id: "keep-exploring-user-02", displayName: "Rome", imageUser: "RM" },
    {
      user_id: "keep-exploring-user-03",
      displayName: "London",
      imageUser: "LDN",
    },
    {
      user_id: "keep-exploring-user-04",
      displayName: "Istanbul",
      imageUser: "IST",
    },
    {
      user_id: "keep-exploring-user-05",
      displayName: "Paris",
      imageUser: "PRS",
    },
    {
      user_id: "keep-exploring-user-06",
      displayName: "Japan",
      imageUser: "PRS",
    },
    {
      user_id: "keep-exploring-user-07",
      displayName: "Singapore",
      imageUser: "PRS",
    },
    {
      user_id: "keep-exploring-user-08",
      displayName: "Thailand",
      imageUser: "PRS",
    },
    {
      user_id: "keep-exploring-user-09",
      displayName: "HongKong",
      imageUser: "PRS",
    },
  ];
  const commentList = [
    {
      id: "d3bad86b-01cd-4844-85b3-e49448378a64",
      user_id: "11c8b801-cc84-4329-9f07-7a6a3d6f2252",
      displayName: "Minnie Tutt",
      imageUser: "http://dummyimage.com/250x250.jpg/ff4444/ffffff",
      content:
        "Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero.",
    },
    {
      id: "97eed16e-8bd6-47b5-9261-efcc67d58f10",
      user_id: "262de4f0-0dd5-4555-9653-f33b69df1c3c",
      displayName: "Chev Vinten",
      imageUser: "http://dummyimage.com/250x250.png/cc0000/ffffff",
      content:
        "Curabitur gravida nisi at nibh. In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem.",
    },
    {
      id: "252cfbd6-80a7-428f-b885-e3698d753d0d",
      user_id: "b7131c08-d8fc-4297-b8d5-1aecb5d04d9b",
      displayName: "Ole Pepin",
      imageUser: "http://dummyimage.com/250x250.png/cc0000/ffffff",
      content:
        "Curabitur gravida nisi at nibh. In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem.",
    },
    {
      id: "5712780a-7ad7-4d5d-97c9-c71fc2875055",
      user_id: "17fdd819-d2ac-4dbd-98fb-d55e9d7de7c7",
      displayName: "Amabelle Monnelly",
      imageUser: "http://dummyimage.com/250x250.bmp/ff4444/ffffff",
      content:
        "Morbi non lectus. Aliquam sit amet diam in magna bibendum imperdiet. Nullam orci pede, venenatis non, sodales sed, tincidunt eu, felis.\n\nFusce posuere felis sed lacus. Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.",
    },
    {
      id: "16075e95-fb17-4c67-8ce1-b729f8903919",
      user_id: "69ad6640-a723-4bfe-9ee3-5a27403973b7",
      displayName: "Bruce Deniset",
      imageUser: "http://dummyimage.com/250x250.bmp/5fa2dd/ffffff",
      content:
        "In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus.",
    },
    {
      id: "9947e3b8-1291-4ae5-a5bc-ce5fb1c339cf",
      user_id: "1b61a0cd-98db-4d14-997c-623939aa4a05",
      displayName: "Nessa Jurs",
      imageUser: "http://dummyimage.com/250x250.jpg/dddddd/000000",
      content:
        "Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus.\n\nIn sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus.",
    },
    {
      id: "4b7ce755-f27e-4237-9370-52e392d8b67a",
      user_id: "377f5d46-7a5f-4538-abd6-fac9a1015995",
      displayName: "Edythe Kenright",
      imageUser: "http://dummyimage.com/250x250.bmp/ff4444/ffffff",
      content:
        "Proin interdum mauris non ligula pellentesque ultrices. Phasellus id sapien in sapien iaculis congue. Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl.\n\nAenean lectus. Pellentesque eget nunc. Donec quis orci eget orci vehicula condimentum.",
    },
    {
      id: "4372aaaf-9316-4d21-8b39-e0b8583d3e3e",
      user_id: "bc744e36-3dd8-43af-825b-5b3e57c9a11a",
      displayName: "Kayle Goodacre",
      imageUser: "http://dummyimage.com/250x250.bmp/dddddd/000000",
      content:
        "Proin leo odio, porttitor id, consequat in, consequat ut, nulla. Sed accumsan felis. Ut at dolor quis odio consequat varius.",
    },
    {
      id: "57a25be9-61ea-4204-a1f8-4020893677a2",
      user_id: "3b3e956e-ffd6-434d-a925-0ced90bdda2b",
      displayName: "Bartie Sheddan",
      imageUser: "http://dummyimage.com/250x250.bmp/dddddd/000000",
      content:
        "Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.\n\nCurabitur at ipsum ac tellus semper interdum. Mauris ullamcorper purus sit amet nulla. Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam.",
    },
    {
      id: "c8796ca7-b0f5-411e-8413-5107ee91f248",
      user_id: "91434d30-6d1b-47d4-8cb7-b4f1fa659ef1",
      displayName: "Ricoriki Godrich",
      imageUser: "http://dummyimage.com/250x250.bmp/dddddd/000000",
      content:
        "Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus.\n\nIn sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus.",
    },
    {
      id: "0262b783-aecf-4abd-b0cd-752dc7b8b310",
      user_id: "6790b5bc-bfc8-4187-98ec-f82fe033a9d7",
      displayName: "Lynda Handrik",
      imageUser: "http://dummyimage.com/250x250.png/ff4444/ffffff",
      content:
        "Fusce posuere felis sed lacus. Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.",
    },
    {
      id: "946b2130-8373-4319-be2e-f9920de86f49",
      user_id: "fae7316a-c2af-477b-b660-c9bfbee0a136",
      displayName: "Agustin Waszkiewicz",
      imageUser: "http://dummyimage.com/250x250.jpg/5fa2dd/ffffff",
      content:
        "Pellentesque at nulla. Suspendisse potenti. Cras in purus eu magna vulputate luctus.\n\nCum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus vestibulum sagittis sapien. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
    },
    {
      id: "10f50a8f-d878-47c9-8840-3e704e8b3390",
      user_id: "38d908c1-ea24-40eb-ad37-c988820171c6",
      displayName: "Tobias Wybron",
      imageUser: "http://dummyimage.com/250x250.jpg/dddddd/000000",
      content:
        "Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui.",
    },
    {
      id: "9bc079a5-1c73-482f-b2c1-4b6fd13cfac5",
      user_id: "83b630ca-78f2-4631-8e74-c89f1b9700cd",
      displayName: "Ardra Shillitoe",
      imageUser: "http://dummyimage.com/250x250.bmp/ff4444/ffffff",
      content:
        "Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui.\n\nMaecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam. Suspendisse potenti.",
    },
    {
      id: "afaf4494-a12c-4d67-a0be-a5a89a9ed413",
      user_id: "86bf1ac8-0ff5-41c8-8985-2002433f2f01",
      displayName: "Crystie Towns",
      imageUser: "http://dummyimage.com/250x250.png/5fa2dd/ffffff",
      content:
        "In hac habitasse platea dictumst. Etiam faucibus cursus urna. Ut tellus.",
    },
    {
      id: "79d5f754-cd33-4686-aefd-f02c23e8e1c0",
      user_id: "e71be0f2-12bf-4d32-aadf-3bbb0a2c4f27",
      displayName: "Dianemarie Lait",
      imageUser: "http://dummyimage.com/250x250.png/cc0000/ffffff",
      content:
        "Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat.",
    },
    {
      id: "fd52e0fc-7bd8-4394-83e5-4c9fc382bfcd",
      user_id: "5bc0e9b7-673d-4b6d-8bff-6f01c1f3b60b",
      displayName: "Fernanda Davidovsky",
      imageUser: "http://dummyimage.com/250x250.png/5fa2dd/ffffff",
      content:
        "Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus.\n\nIn sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus.",
    },
    {
      id: "0ae9a67a-a303-465a-81f6-ddd7bad720cf",
      user_id: "aba82051-e8ed-4c76-b7db-8c4d3cfc20d9",
      displayName: "Rayshell Gillingham",
      imageUser: "http://dummyimage.com/250x250.bmp/dddddd/000000",
      content:
        "Proin interdum mauris non ligula pellentesque ultrices. Phasellus id sapien in sapien iaculis congue. Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl.",
    },
    {
      id: "5c052e41-4acb-43a0-9103-ce10b82c2897",
      user_id: "e5d2167b-7cb6-4aba-adf3-457b94711f81",
      displayName: "Rickey Jakel",
      imageUser: "http://dummyimage.com/250x250.bmp/5fa2dd/ffffff",
      content:
        "Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa. Donec dapibus. Duis at velit eu est congue elementum.\n\nIn hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.",
    },
    {
      id: "0eb9d04b-068f-4c28-bd5a-ee733b444d7a",
      user_id: "364cbf43-0bcc-4608-a119-c4bc16c78d7d",
      displayName: "Eugenie Dunseath",
      imageUser: "http://dummyimage.com/250x250.png/5fa2dd/ffffff",
      content:
        "Aenean lectus. Pellentesque eget nunc. Donec quis orci eget orci vehicula condimentum.\n\nCurabitur in libero ut massa volutpat convallis. Morbi odio odio, elementum eu, interdum eu, tincidunt in, leo. Maecenas pulvinar lobortis est.",
    },
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
  const [urlImageOverlayPanel, setUrlImageOverlayPanel] = useState("");
  const op = useRef(null);
  const opLike = useRef(null);
  const isMounted = useRef(false);
  const history = useHistory();
  const dispatch = useDispatch();
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

  return (
    <div className="post-details-container">
      <div className="carousel-container">
        <Carousel
          value={products}
          numVisible={4}
          numScroll={2}
          responsiveOptions={GLOBAL_VARIABLE.RESPONSIVE_OPTIONS}
          itemTemplate={(product) => (
            <PostImageTemplate
              product={product}
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
              <Tag className="tag-status" severity="success" value="Done"></Tag>
              <i
                className="pi pi-thumbs-up"
                onClick={(e) => {
                  opLike.current.toggle(e);
                }}
                aria-haspopup
                aria-controls="overlay_panel_like"
              >
                <span>Likes({likeList.length})</span>
              </i>
              <i className="pi pi-comments">
                <span>{`Comments(${commentList.length})`}</span>
              </i>
              <Button
                icon="pi pi-pencil"
                className="p-button-rounded p-button-success p-mr-2"
                onClick={() =>
                  dispatch(actionShowDialog(GLOBAL_VARIABLE.DIALOG_EDIT_POST))
                }
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
            header={`Comment${commentList.length}`}
            className="tab-comment"
            leftIcon="pi pi-comments"
          >
            <div className="comment-container">
              <CommentComponent commentList={commentList} />
            </div>
          </TabPanel>
        </TabView>
      </div>
      <DialogEditPost />
      <OverlayUserLike opLike={opLike} likeList={likeList} />
      <OverLayPanelImagePost
        op={op}
        urlImageOverlayPanel={urlImageOverlayPanel}
      />
    </div>
  );
}

export default PostDetailsPage;
