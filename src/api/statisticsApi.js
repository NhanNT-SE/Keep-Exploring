import axiosClient from "./axiosClient";

const statisticsApi = {
  getData: () => {
    const url = "/statistics";
    return axiosClient.get(url);
  },
};

export default statisticsApi;
