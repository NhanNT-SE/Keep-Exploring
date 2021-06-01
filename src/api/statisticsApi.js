import axiosClient from "./axiosClient";

const statisticsApi = {
  getData: () => {
    const url = "/admin/statistics/number";
    return axiosClient.get(url);
  },
  getDataTimeLine: () => {
    const url = "/admin/statistics/time-line";
    return axiosClient.get(url);
  },
};

export default statisticsApi;
