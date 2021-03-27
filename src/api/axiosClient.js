import axios from "axios";
import GLOBAL_VARIABLE from "utils/global_variable";
import queryString from "query-string";
import {
  actionRefreshToken,
} from "redux/slices/userSlice";
import rootStore from "rootStore";
import localStorageService from "../utils/localStorageService";

const axiosClient = axios.create({
  baseURL: GLOBAL_VARIABLE.BASE_URL,
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
    if (err.message === "jwt expired") {
      rootStore.dispatch(actionRefreshToken());
    }
    console.log("error axios", err);
    throw err;
  }
);

export default axiosClient;
