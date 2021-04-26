import { Chart } from "primereact/chart";
import React from "react";
import { useSelector } from "react-redux";
import "./chart-number.scss";

function ChartNumber() {
  const data = useSelector((state) => state.post.statisticsData);
  const configDoughnutChart = (data) => {
    return {
      labels: ["Done", "Pending", "Need Update"],
      datasets: [
        {
          data,
          backgroundColor: ["#36A2EB", "#FFCE56", "#FF6384"],
          hoverBackgroundColor: ["#36A2EB", "#FFCE56", "#FF6384"],
        },
      ],
    };
  };
  const configPieChart = (data, labels) => {
    return {
      labels,
      datasets: [
        {
          data,
          backgroundColor: ["#36A2EB", "#FFCE56"],
          hoverBackgroundColor: ["#36A2EB", "#FFCE56"],
        },
      ],
    };
  };
  const configOptions = (text) => {
    return {
      title: {
        display: true,
        text,
        fontSize: 16,
        position: "bottom",
      },
      legend: {
        labels: {
          fontColor: "#495057",
        },
      },
    };
  };

  return data ? (
    <div className="chart-statistics-container">
      <div className="chart-container">
        <Chart
          type="pie"
          data={configPieChart(data.postBlog.data, ["Post", "Blog"])}
          options={configOptions(data.postBlog.title)}
        />
      </div>

      <div className="chart-container">
        <Chart
          type="pie"
          data={configPieChart(data.user.data, ["Admin", "User"])}
          options={configOptions(data.user.title)}
        />
      </div>
      <div className="chart-container">
        <Chart
          type="doughnut"
          data={configDoughnutChart(data.post.data)}
          options={configOptions(data.post.title)}
        />
      </div>
      <div className="chart-container">
        <Chart
          type="doughnut"
          data={configDoughnutChart(data.blog.data)}
          options={configOptions(data.blog.title)}
        />
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

export default ChartNumber;
