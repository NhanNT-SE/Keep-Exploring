import TableComponent from "common-components/table/table";
import { AvatarBodyTemplate } from "common-components/template/avatar-template/avater-template";
import {
  PostBodyTemplate,
  BlogBodyTemplate,
} from "common-components/template/post-template/post-blog-template";
import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { MultiSelect } from "primereact/multiselect";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { actionGetListUser } from "redux/slices/userSlice";
import GLOBAL_VARIABLE from "utils/global_variable";

function TableUser() {
  const dt = useRef(null);
  const columns = GLOBAL_VARIABLE.COLUMNS_USER;
  const history = useHistory();
  const dispatch = useDispatch();
  const userList = useSelector((state) => state.user.userList);
  const [selectedColumns, setSelectedColumns] = useState(columns);

  const onColumnToggle = (event) => {
    let selectedColumns = event.value;
    let orderedSelectedColumns = columns.filter((col) =>
      selectedColumns.some((sCol) => sCol.field === col.field)
    );
    setSelectedColumns(orderedSelectedColumns);
  };
  const editUser = (user) => {
    history.push(`/user/${user._id}`);
  };
  const onPostClick = (e) => {
    history.push(`/post/${e}`);
  };
  const onBlogClick = (e) => {
    history.push(`/blog/${e}`);
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
        onClick={() => editUser(rowData)}
      />
    );
  };
  const dynamicColumns = selectedColumns.map((col, i) => {
    return (
      <Column
        key={col.field}
        field={col.field}
        filter={
          col.field === "displayName" || col.field === "email" ? true : false
        }
        filterMatchMode="contains"
        sortable={col.field === "imgUser" ? false : true}
        header={col.header}
        body={
          col.field === "imgUser"
            ? AvatarBodyTemplate
            : col.header === "Post"
            ? (rowData) => PostBodyTemplate(rowData, onPostClick)
            : col.header === "Blog"
            ? (rowData) => BlogBodyTemplate(rowData, onBlogClick)
            : null
        }
      />
    );
  });
  useEffect(() => {
    dispatch(actionGetListUser());
  }, []);
  useEffect(() => {
    console.log("user list:", userList);
  }, [userList]);
  return (
    <div className="table-user-container">
      <TableComponent
        dt={dt}
        data={userList}
        dataType="users"
        header={header}
        columns={dynamicColumns}
        actionBodyTemplate={actionBodyTemplate}
      />
    </div>
  );
}

export default TableUser;
