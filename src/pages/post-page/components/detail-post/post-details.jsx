import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { Carousel } from "primereact/carousel";
import { Button } from "primereact/button";
import "./post-details.scss";
function PostDetailsPage() {
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

  const history = useHistory();
  useEffect(() => {
    if (!post || postId != post.id.toString()) {
      history.push("/post");
    }
  }, []);
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

  const productTemplate = (product) => {
    return (
      <div className="product-item">
        <div className="product-item-content">
          <div
            className="image"
            style={{
              backgroundImage: `url(${product.image})`,
            }}
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
          itemTemplate={productTemplate}
        />
      </div>
      <div className="post-details-container"></div>
    </div>
  );
}

export default PostDetailsPage;
