import React, { useEffect, useRef, useState } from "react";
import {COL_TABLE,TEMPLATE_TABLE} from "utils/global_variable";
import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { Dropdown } from "primereact/dropdown";
import { MultiSelect } from "primereact/multiselect";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { StatusBodyTemplate } from "common-components/template/status-template/status-template";
import DateBodyTemplate from "common-components/template/date-template/date-template";
import { StatusItemTemplate } from "common-components/template/status-template/status-template";
import { SelectedStatusTemplate } from "common-components/template/status-template/status-template";
import "./table-blog.scss";
import { actionGetAllBlog } from "redux/slices/blog.slice";
import DisplayNameBodyTemplate from "common-components/template/display-name-template/display-name-template";
import TableComponent from "common-components/table/table";
function TableBlogComponent() {
  const columns = COL_TABLE.COLUMNS_BLOG;
  const dt = useRef(null);
  const history = useHistory();
  const dispatch = useDispatch();
  const blogs = useSelector((state) => state.blog.blogList);
  const [selectedColumns, setSelectedColumns] = useState(columns);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const onStatusChange = (e) => {
    dt.current.filter(e.value, "status", "equals");
    setSelectedStatus(e.value);
  };
  const editBlog = (blog) => {
    history.push(`blog/${blog._id}`);
  };
  useEffect(() => {
    dispatch(actionGetAllBlog());
  }, []);
  const displayNameClick = (userId) => {
    history.push(`/user/${userId}`);
  };
  const statusFilter = (
    <Dropdown
      value={selectedStatus}
      options={TEMPLATE_TABLE.STATUS_LIST}
      onChange={onStatusChange}
      itemTemplate={StatusItemTemplate}
      valueTemplate={SelectedStatusTemplate}
      placeholder="Select a Status"
      className="p-column-filter"
      showClear
    />
  );
  const onColumnToggle = (event) => {
    let selectedColumns = event.value;
    let orderedSelectedColumns = columns.filter((col) =>
      selectedColumns.some((sCol) => sCol.field === col.field)
    );
    setSelectedColumns(orderedSelectedColumns);
  };
  const header = (
    <div className="table-header">
      <MultiSelect
        value={selectedColumns}
        options={columns}
        optionLabel="header"
        onChange={onColumnToggle}
        style={{ width: "20em" }}
      />
    </div>
  );
  const actionBodyTemplate = (rowData) => {
    return (
      <Button
        icon="pi pi-pencil"
        className="p-button-rounded p-button-success p-mr-2"
        onClick={() => editBlog(rowData)}
        // onClick={() => console.log(rowData)}
      />
    );
  };
  const dynamicColumns = selectedColumns.map((col, i) => {
    return (
      <Column
        key={col.field}
        field={col.field}
        filter
        filterMatchMode="contains"
        body={
          col.field === "status"
            ? StatusBodyTemplate
            : col.field === "created_on"
            ? DateBodyTemplate
            : col.field === "owner.displayName"
            ? (rowData) => DisplayNameBodyTemplate(rowData, displayNameClick)
            : null
        }
        filterElement={col.field === "status" ? statusFilter : null}
        sortable
        header={col.header}
      />
    );
  });
  return (
    <div className="table-blog-container">
      <TableComponent
        dt={dt}
        data={blogs}
        dataType="blogs"
        header={header}
        columns={dynamicColumns}
        actionBodyTemplate={actionBodyTemplate}
      />
    </div>
  );
}

export default TableBlogComponent;
