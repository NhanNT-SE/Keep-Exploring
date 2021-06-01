import TableComponent from "common-components/table/table";
import { AvatarBodyTemplate } from "common-components/template/avatar-template/avater-template";
import DateBodyTemplate from "common-components/template/date-template/date-template";
import {
  GenderBodyTemplate,
  SelectedGenderTemplate,
  GenderItemTemplate,
} from "common-components/template/gender-template/gender-template";
import {
  PostBodyTemplate,
  BlogBodyTemplate,
} from "common-components/template/post-template/post-blog-template";
import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { Dropdown } from "primereact/dropdown";
import { MultiSelect } from "primereact/multiselect";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { actionGetListUser } from "redux/slices/userSlice";
import GLOBAL_VARIABLE from "utils/global_variable";
import "./table-user.scss";
function TableUser() {
  const dt = useRef(null);
  const columns = GLOBAL_VARIABLE.COLUMNS_USER;
  const history = useHistory();
  const dispatch = useDispatch();
  const userList = useSelector((state) => state.user.userList);
  const [selectedColumns, setSelectedColumns] = useState(columns);
  const [selectedGender, setSelectedGender] = useState(null);
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
  const onGenderChange = (e) => {
    dt.current.filter(e.value, "gender", "equals");
    setSelectedGender(e.value);
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
  const genderFilter = (
    <Dropdown
      value={selectedGender}
      options={GLOBAL_VARIABLE.GENDER_LIST}
      onChange={onGenderChange}
      itemTemplate={GenderItemTemplate}
      valueTemplate={SelectedGenderTemplate}
      placeholder="Select a gender"
      className="p-column-filter"
      showClear
    />
  );
  const dynamicColumns = selectedColumns.map((col, i) => {
    return (
      <Column
        key={col.field}
        field={col.field}
        filter={
          col.field === "userInfo.postList.length" ||
          col.field === "userInfo.blogList.length" ||
          col.field === "avatar"
            ? false
            : true
        }
        filterMatchMode="contains"
        sortable={col.field === "avatar" ? false : true}
        header={col.header}
        body={
          col.field === "avatar"
            ? AvatarBodyTemplate
            : col.header === "Post"
            ? (rowData) => PostBodyTemplate(rowData, onPostClick)
            : col.header === "Blog"
            ? (rowData) => BlogBodyTemplate(rowData, onBlogClick)
            : col.field === "gender"
            ? GenderBodyTemplate
            : col.field === "created_on"
            ? DateBodyTemplate
            : null
        }
        filterElement={col.field === "gender" ? genderFilter : null}
        style={col.field === "avatar" ? { width: "4rem" } : null}
      />
    );
  });
  useEffect(() => {
    dispatch(actionGetListUser());
  }, []);
  useEffect(() => {
    console.log(userList);
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
