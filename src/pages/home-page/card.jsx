import React from "react";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Button from "@material-ui/core/Button";
import Typography from "@material-ui/core/Typography";
import "./styles/card.scss";
import { useHistory } from "react-router";
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
