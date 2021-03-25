import { Chart } from "primereact/chart";
import React, { useEffect, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionGetStatistics } from "redux/slices/postSlice";
import "./chart-statistics.scss";

function ChartStatistics() {
  const dispatch = useDispatch();
  const data = useSelector((state) => state.post.statisticsData);
  const memoData = useMemo(() => {
    return data;
  }, [data]);
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
  useEffect(() => {
    console.log("Data:", data);
  }, [data]);
  useEffect(() => {
    dispatch(actionGetStatistics());
  }, []);
  return memoData ? (
    <div className="chart-statistics-container">
      <div className="chart-container">
        <Chart
          type="pie"
          data={configPieChart(memoData.postBlog.data, ["Post", "Blog"])}
          options={configOptions(memoData.postBlog.title)}
        />
      </div>

      <div className="chart-container">
        <Chart
          type="pie"
          data={configPieChart(memoData.user.data, ["Admin", "User"])}
          options={configOptions(memoData.user.title)}
        />
      </div>
      <div className="chart-container">
        <Chart
          type="doughnut"
          data={configDoughnutChart(memoData.post.data)}
          options={configOptions(memoData.post.title)}
        />
      </div>
      <div className="chart-container">
        <Chart
          type="doughnut"
          data={configDoughnutChart(memoData.blog.data)}
          options={configOptions(memoData.blog.title)}
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

export default ChartStatistics;
