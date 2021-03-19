const GLOBAL_VARIABLE = {
  // BASE_URL: process.env.REACT_APP_BASE_URL_PRO,
  // BASE_URL_IMAGE: process.env.REACT_APP_URL_IMAGE_PRO,
  BASE_URL: process.env.REACT_APP_BASE_URL_DEV,
  BASE_URL_IMAGE: process.env.REACT_APP_URL_IMAGE_DEV,
  DIALOG_MESSAGE: "DIALOG_MESSAGE",
  DIALOG_EDIT_POST: "DIALOG_EDIT_POST",
  STATUS_LIST: ["pending", "done", "need_update"],
  CATEGORY_LIST: ["hotel", "food", "check_in"],
  RATING_LIST: [0, 1, 2, 3, 4, 5],
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
  RESPONSIVE_OPTIONS: [
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
  ],
};

export default GLOBAL_VARIABLE;
