import { takeLatest } from "@redux-saga/core/effects";
import {
  actionGetStatisticsNumber,
  actionGetStatisticsTimeLine,
} from "redux/slices/statistics.slice";
import {
  handlerGetStatisticsNumber,
  handlerGetStatisticsTimeLine,
} from "../handlers/statistics.handler";

export function* sagaGetStatisticsNumber() {
  yield takeLatest(actionGetStatisticsNumber.type, handlerGetStatisticsNumber);
}
export function* sagaGetStatisticsTimeLine() {
  yield takeLatest(
    actionGetStatisticsTimeLine.type,
    handlerGetStatisticsTimeLine
  );
}
