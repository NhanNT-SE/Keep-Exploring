import axios from "axios";
import { CONFIG_URL } from "utils/global_variable";
import queryString from "query-string";
import rootStore from "rootStore";
import localStorageService from "../utils/localStorageService";
import { actionRefreshToken, actionSetUser } from "redux/slices/auth.slice";

const axiosClient = axios.create({
  baseURL: CONFIG_URL.BASE_URL,
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
    if (err.status === 500) {
      err.message = "Internal Server Error";
    }
    if (err.status === 401) {
      if (err.message === "jwt expired") {
        if (+err.payload > 7) {
          err.message = "Vui lòng đăng nhập lại để tiếp tục";
        }
        rootStore.dispatch(actionRefreshToken(+err.payload));
      } else {
        err.message = "Vui lòng đăng nhập lại để tiếp tục";
        localStorageService.clearStorage();
        localStorageService.clearUser();
        rootStore.dispatch(actionSetUser(null));
      }
    }
    console.log("error axios", err);
    throw err;
  }
);

export default axiosClient;
