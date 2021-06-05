export const customResponse = (data, message) => {
  return { data, status: 200, message: message ? message : "Lấy dữ liệu thành công" };
};
