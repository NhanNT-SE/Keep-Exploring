import React from "react";
import CardComponent from "./card";
import "./styles/home-page.scss";
function HomePage() {
  return (
    <div className="home-page-container">
      {[
        { title: "User Management", url: "/user", image: "bg-user.jpg" },
        { title: "Post Management", url: "/post", image: "bg-post.jpg" },
        { title: "Blog Management", url: "/blog", image: "bg-blog.jpg" },
        { title: "Notification", url: "/notify", image: "bg-notify.jpg" },
        { title: "Statistics", url: "/statistics", image: "bg-statistics.jpg" },
        { title: "Account Setting", url: "/profile", image: "bg-account.jpg" },
      ].map((item) => (
        <CardComponent key={item.url} title={item.title} image={item.image} url={item.url} />
      ))}
    </div>
  );
}

export default HomePage;
