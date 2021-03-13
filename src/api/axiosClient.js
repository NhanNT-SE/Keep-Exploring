import axios from "axios";
import queryString from "query-string";
import {
  actionRefreshToken,
  actionRefreshTokenStarted,
} from "redux/slices/userSlice";
import rootStore from "rootStore";
import localStorageService from "./localStorageService";

const axiosClient = axios.create({
  // baseURL: process.env.REACT_APP_BASE_URL_DEV,
  baseURL: process.env.REACT_APP_BASE_URL_PRO,
  headers: {
    "content-type": "application/json",
  },
  paramsSerializer: (params) => queryString.stringify(params),
});

axiosClient.interceptors.request.use(async (config) => {
  try {
    const accessToken = localStorageService.getAccessToken();
    if (accessToken) {
      config.headers["Authorization"] = `Bearer ${accessToken}`;
    }
    return config;
  } catch (error) {
    throw error;
  }
});
axiosClient.interceptors.response.use(
  (response) => {
    if (response && response.data) {
      const { error } = response.data;
      if (error) {
        throw error;
      }

      return response.data;
    }

    return response;
  },
  (error) => {
    const err = error.response.data.error || error;
    if (error.message === "jwt expired") {
      rootStore.dispatch(actionRefreshTokenStarted());
      rootStore.dispatch(actionRefreshToken());
    }
    console.log("error axios", err);
    throw err;
  }
);

export default axiosClient;