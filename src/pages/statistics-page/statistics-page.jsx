import React, { useState } from "react";
import "./statistics-page.scss";
import { TabPanel, TabView } from "primereact/tabview";
import ChartStatistics from "./chart-page/chart-statistics";
import TreeSystem from "./tree-sytem/tree-system";

function StatisticsPage() {
  return (
    <div className="statistics-page-container">
      <TabView className="tab-view-statistics">
        <TabPanel
          header="Statistics"
          leftIcon="pi pi-chart-bar"
          className="tab-statistics-chart"
        >
          <ChartStatistics />
        </TabPanel>
        <TabPanel
          header="Tree System"
          leftIcon="pi pi-chart-line"
          className="tab-org-chart"
        >
          <TreeSystem />
        </TabPanel>
      </TabView>
    </div>
  );
}

export default StatisticsPage;
