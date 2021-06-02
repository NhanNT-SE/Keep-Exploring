import statisticsApi from "api/statistics.api";
import { call, put } from "redux-saga/effects";
import { actionFailed, actionSuccess } from "redux/slices/common.slice";
import {
  actionGetStatisticsNumber,
  actionGetStatisticsTimeLine,
  actionSetStatisticsNumber,
  actionSetStatisticsTimeLine,
} from "redux/slices/statistics.slice";

import localStorageService from "utils/localStorageService";

export function* handlerGetStatisticsNumber() {
  try {
    localStorageService.setLatestAction(actionGetStatisticsNumber.type);
    const response = yield call(statisticsApi.getData);
    const { data } = response;
    yield put(actionSetStatisticsNumber(data));
    yield put(actionSuccess("Fetch statistics data successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
  }
}
export function* handlerGetStatisticsTimeLine() {
  try {
    localStorageService.setLatestAction(actionGetStatisticsTimeLine.type);
    const response = yield call(statisticsApi.getDataTimeLine);
    const { data } = response;
    yield put(actionSetStatisticsTimeLine(data));
    yield put(actionSuccess("Fetch timeline statistics data successfully!"));
  } catch (error) {
    console.log("post slice: ", error);
    yield put(actionFailed(error.message));
  }
}
