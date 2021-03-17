const localStorageService = {
  clearStorage: () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("latestAction");
  },
  clearUser: () => {
    localStorage.removeItem("user");
  },
  getAccessToken: () => {
    return localStorage.getItem("accessToken");
  },
  getLatestAction: () => {
    return localStorage.getItem("latestAction");
  },
  getRefreshToken: () => {
    return localStorage.getItem("refreshToken");
  },
  getUser: () => {
    return  localStorage.getItem("user");
  },
  setAccessToken: (accessToken) => {
    localStorage.setItem("accessToken", accessToken);
  },
  setLatestAction: (latestAction) => {
    localStorage.setItem("latestAction", latestAction);
  },
  setUser: (user) => {
    localStorage.setItem("user", JSON.stringify(user));
  },
  setToken: (accessToken, refreshToken) => {
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
  },
};
export default localStorageService;
