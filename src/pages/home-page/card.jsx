import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import React from "react";
import { useHistory } from "react-router";
import "./styles/card.scss";
function CardComponent(props) {
  const { title, image, url } = props;
  const history = useHistory();
  const onCardClick = () => {
    history.push(url);
  };
  return (
    <Card
      className="card-container"
      onClick={onCardClick}
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL}/images/${image})`,
      }}
    >
      <CardActionArea>
        <p className="title">{title}</p>
      </CardActionArea>
    </Card>
  );
}

export default CardComponent;
