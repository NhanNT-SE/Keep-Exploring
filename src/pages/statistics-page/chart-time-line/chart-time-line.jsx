import { Chart } from "primereact/chart";
import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./chart-time-line.scss";

function ChartTimeLine() {
  const data = useSelector((state) => state.statistics.timeLineData);

  const [dataPost, setDataPost] = useState([]);
  const [dataBlog, setDataBlog] = useState([]);
  const [dataUser, setDataUser] = useState([]);
  useEffect(() => {
    if (data) {
      setDataUser(data.user);
      setDataPost(data.post);
      setDataBlog(data.blog);
    }
  }, [data]);
  const labels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
  const dataPostBlog = {
    labels,
    datasets: [
      {
        label: "Post",
        backgroundColor: "#42A5F5",
        data: [...dataPost],
      },
      {
        label: "Blog",
        backgroundColor: "#66BB6A",
        data: [...dataBlog],
      },
    ],
  };
  const dataUserRender = {
    labels,
    datasets: [
      {
        label: "User",
        backgroundColor: "#FFA726",
        data: [...dataUser],
      },
    ],
  };
  const optionBar = {
    legend: {
      labels: {
        fontColor: "#495057",
      },
    },
    scales: {
      xAxes: [
        {
          ticks: {
            fontColor: "#495057",
          },
        },
      ],
      yAxes: [
        {
          ticks: {
            fontColor: "#495057",
          },
        },
      ],
    },
  };
  const generalData = {
    labels,
    datasets: [
      {
        label: "User",
        data: [...dataUser],
        fill: false,
        borderColor: "#FFA726",
      },
      {
        label: "Post",
        data: [...dataPost],
        fill: false,
        borderColor: "#42A5F5",
      },
      {
        label: "Blog",
        data: [...dataBlog],
        fill: false,
        borderColor: "#66BB6A",
      },
    ],
  };
  const OptionsTimeLine = {
    legend: {
      labels: {
        fontColor: "#495057",
      },
    },
    scales: {
      xAxes: [
        {
          ticks: {
            fontColor: "#495057",
          },
        },
      ],
      yAxes: [
        {
          ticks: {
            fontColor: "#495057",
          },
        },
      ],
    },
  };
  return data ? (
    <div className="chart-time-line-container">
      <div className="chart-container">
        <Chart type="line" data={generalData} options={OptionsTimeLine} />
        <h4>General</h4>
      </div>
      <div className="chart-container">
        <Chart type="bar" data={dataUserRender} options={optionBar} />
        <h4>User</h4>
      </div>
      <div className="chart-container">
        <Chart type="bar" data={dataPostBlog} options={optionBar} />
        <h4>Post-Blog</h4>
      </div>
    </div>
  ) : (
    <div className="loading">
      <i
        className="pi pi-spin pi-spinner"
        style={{ fontSize: "5em", color: "#4272d7" }}
      ></i>
    </div>
  );
}

export default ChartTimeLine;
