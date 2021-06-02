import { createSlice } from "@reduxjs/toolkit";
const statistics = createSlice({
  name: "statistics",
  initialState: {
    numberData: null,
    timeLineData: null,
  },
  reducers: {
    actionGetStatisticsNumber() {},
    actionGetStatisticsTimeLine() {},

    actionSetStatisticsNumber: (state, action) => {
      state.numberData = action.payload;
    },
    actionSetStatisticsTimeLine: (state, action) => {
      state.timeLineData = action.payload;
    },
  },
});
export const {
  actionGetStatisticsNumber,
  actionGetStatisticsTimeLine,
  actionSetStatisticsNumber,
  actionSetStatisticsTimeLine,
} = statistics.actions;
export default statistics.reducer;
