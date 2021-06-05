import { TabPanel, TabView } from "primereact/tabview";
import React from "react";
import ChartNumber from "./chart-number/chart-number";
import ChartTimeLine from "./chart-time-line/chart-time-line";
import "./statistics-page.scss";
import TreeSystem from "./tree-sytem/tree-system";

function StatisticsPage() {
  return (
    <div className="statistics-page-container">
      <TabView className="tab-view-statistics">
        <TabPanel header="Time line" leftIcon="pi pi-chart-line">
          <div className="timeLine-container-tab-panel">
            <ChartTimeLine />
          </div>
        </TabPanel>
        <TabPanel header="Number" leftIcon="pi pi-chart-bar">
          <ChartNumber />
        </TabPanel>
        <TabPanel header="Tree System" leftIcon="pi pi-chart-line">
          <TreeSystem />
        </TabPanel>
      </TabView>
    </div>
  );
}

export default StatisticsPage;
