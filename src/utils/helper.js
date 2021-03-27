export const convertDate = (dateString) => {
  if (dateString) {
    const date = new Date(dateString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const year = date.getFullYear();
    let dayString = "";
    let monthString = "";
    if (day > 10) {
      dayString = day;
    } else {
      dayString = "0" + day;
    }
    if (month > 10) {
      monthString = month;
    } else {
      monthString = "0" + month;
    }
    return dayString + "-" + monthString + "-" + year;
  }
  return "";
};
