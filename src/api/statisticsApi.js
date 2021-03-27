import axiosClient from "./axiosClient";

const statisticsApi = {
  getData: () => {
    const url = "/admin/statistics";
    return axiosClient.get(url);
  },
};

export default statisticsApi;
