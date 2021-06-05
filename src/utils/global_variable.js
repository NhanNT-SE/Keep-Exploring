export const CONFIG_URL = {
  // BASE_URL: process.env.REACT_APP_BASE_URL_PRO,
  // BASE_URL_IMAGE: process.env.REACT_APP_URL_IMAGE_PRO,
  BASE_URL: process.env.REACT_APP_BASE_URL_DEV,
  BASE_URL_IMAGE: process.env.REACT_APP_URL_IMAGE_DEV,
};
export const DIALOG = {
  DIALOG_MESSAGE: "DIALOG_MESSAGE",
  DIALOG_EDIT_POST: "DIALOG_EDIT_POST",
  DIALOG_NOTIFY: "DIALOG_NOTIFY",
  DIALOG_DELETE_USER: "DIALOG_DELETE_USER",
  DIALOG_CHANGE_PASSWORD: "DIALOG_CHANGE_PASSWORD",
  DIALOG_ENABLE_MFA: "DIALOG_ENABLE_MFA",
  DIALOG_DISABLE_MFA: "DIALOG_DISABLE_MFA",
};
export const RESPONSIVE_OPTIONS = [
  {
    breakpoint: "1024px",
    numVisible: 3,
    numScroll: 3,
  },
  {
    breakpoint: "600px",
    numVisible: 2,
    numScroll: 2,
  },
  {
    breakpoint: "480px",
    numVisible: 1,
    numScroll: 1,
  },
];
export const TEMPLATE_TABLE = {
  STATUS_LIST: ["pending", "done", "need_update"],
  CATEGORY_LIST: ["hotel", "food", "check_in"],
  GENDER_LIST: ["female", "male"],
  RATING_LIST: [0, 1, 2, 3, 4, 5],
};
export const COL_TABLE = {
  COLUMNS_POST: [
    { field: "owner.displayName", header: "Owner" },
    { field: "title", header: "Title" },
    { field: "category", header: "Category" },
    { field: "status", header: "Status" },
    { field: "desc", header: "Description" },
    { field: "address", header: "Address" },
    { field: "rating", header: "Rating" },
    { field: "created_on", header: "Date" },
  ],
  COLUMNS_BLOG: [
    { field: "owner.displayName", header: "Owner" },
    { field: "title", header: "Title" },
    { field: "status", header: "Status" },
    { field: "created_on", header: "Date" },
  ],
  COLUMNS_USER: [
    { field: "avatar", header: "" },
    { field: "displayName", header: "Display Name" },
    { field: "email", header: "Email" },
    { field: "username", header: "Username" },
    { field: "gender", header: "Gender" },
    { field: "userInfo.blogList.length", header: "Blog" },
    { field: "userInfo.postList.length", header: "Post" },
    { field: "created_on", header: "Date" },
  ],
  COLUMNS_NOTIFY: [
    { field: "displayName", header: "User Name" },
    { field: "email", header: "Email" },
    { field: "gender", header: "Gender" },
    { field: "created_on", header: "Date" },
  ],
};
