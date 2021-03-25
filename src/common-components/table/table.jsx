import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import React from "react";
import "./table.scss";
function TableComponent(props) {
  const {
    dt,
    data,
    dataType,
    header,
    columns,
    actionBodyTemplate,
    selectionMode,
    selectedData,
    setSelectedData,
  } = props;
  return (
    <DataTable
      ref={dt}
      value={data}
      dataKey="_id"
      paginator
      resizableColumns={true}
      rows={10}
      paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport"
      currentPageReportTemplate={`Showing {first} to {last} of {totalRecords} ${dataType}`}
      header={header}
      scrollable
      selection={selectedData}
      onSelectionChange={(e) => setSelectedData(e.value)}
      scrollHeight="calc(100% - 200px)"
      className="p-datatable-gridlines"
    >
      {selectionMode && (
        <Column
          selectionMode="multiple"
          headerStyle={{ width: "3rem" }}
        ></Column>
      )}
      {actionBodyTemplate && (
        <Column
          body={actionBodyTemplate}
          header="Edit"
          style={{ width: "4rem" }}
        ></Column>
      )}
      {columns}

    </DataTable>
  );
}

export default TableComponent;
