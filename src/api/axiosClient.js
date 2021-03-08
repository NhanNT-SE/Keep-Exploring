import axios from "axios";
import queryString from "query-string";
import {
  actionRefreshToken,
  actionRefreshTokenStarted,
} from "redux/slices/userSlice";
import rootStore from "rootStore";
import localStorageService from "./localStorageService";

const axiosClient = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL_DEV,
  // baseURL: process.env.REACT_APP_BASE_URL_PRO,
  headers: {
    "content-type": "application/json",
  },
  paramsSerializer: (params) => queryString.stringify(params),
});

axiosClient.interceptors.request.use(async (config) => {
  try {
    const accessToken = localStorageService.getAccessToken();
    if (accessToken) {
      config.headers["Authorization"] = accessToken;
    }
    return config;
  } catch (error) {
    throw error;
  }
});
axiosClient.interceptors.response.use(
  (response) => {
    if (response && response.data) {
      return response.data;
    }
    return response;
  },
  (error) => {
    const err = error.response.data.error;
    if (err.message === "jwt expired") {
      rootStore.dispatch(actionRefreshTokenStarted());
      rootStore.dispatch(actionRefreshToken());
    }
    throw err;
  }
);

export default axiosClient;
