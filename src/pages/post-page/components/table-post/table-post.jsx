import TableComponent from "common-components/table/table";
import {
  CategoryItemSelectedTemplate,
  CategoryItemTemplate,
} from "common-components/template/category-template/category-template";
import DateBodyTemplate from "common-components/template/date-template/date-template";
import DisplayNameBodyTemplate from "common-components/template/display-name-template/display-name-template";
import {
  RatingBodyTemplate,
  RatingItemTemplate,
  RatingSelectedItemTemplate,
} from "common-components/template/rating-template/rating-template";
import {
  SelectedStatusTemplate,
  StatusBodyTemplate,
  StatusItemTemplate,
} from "common-components/template/status-template/status-template";
import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { Dropdown } from "primereact/dropdown";
import { MultiSelect } from "primereact/multiselect";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import {
  actionGetAllPost,
  actionSetSelectedPost,
} from "redux/slices/post.slice";
import { COL_TABLE, TEMPLATE_TABLE } from "utils/global_variable";
import "./table-post.scss";
function TablePostComponent() {
  const dt = useRef(null);
  const columns = COL_TABLE.COLUMNS_POST;
  const postList = useSelector((state) => state.post.postList);
  const [posts, setPosts] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [selectedRating, setSelectedRating] = useState(null);
  const [selectedColumns, setSelectedColumns] = useState(columns);
  const dispatch = useDispatch();

  const history = useHistory();
  const editPost = (post) => {
    history.push(`post/${post._id}`);
  };

  const onColumnToggle = (event) => {
    let selectedColumns = event.value;
    let orderedSelectedColumns = columns.filter((col) =>
      selectedColumns.some((sCol) => sCol.field === col.field)
    );
    setSelectedColumns(orderedSelectedColumns);
  };
  const displayNameClick = (userId) => {
    history.push(`/user/${userId}`);
  };
  const categoryBodyTemplate = (rowData) => {
    return <span>{rowData.category.toUpperCase().replace("_", " ")}</span>;
  };

  const actionBodyTemplate = (rowData) => {
    return (
      <Button
        icon="pi pi-pencil"
        className="p-button-rounded p-button-success p-mr-2"
        onClick={() => editPost(rowData)}
      />
    );
  };
  useEffect(() => {
    dispatch(actionGetAllPost());
    dispatch(actionSetSelectedPost(null));
  }, []);
  useEffect(() => {
    const resultList = JSON.parse(JSON.stringify(postList));
    setPosts(resultList);
  }, [postList]);
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

  // ***** handler filter on change dropdown *****
  const onStatusChange = (e) => {
    dt.current.filter(e.value, "status", "equals");
    setSelectedStatus(e.value);
  };
  const onCategoryChange = (e) => {
    dt.current.filter(e.value, "category", "equals");
    setSelectedCategory(e.value);
  };
  const onRatingChange = (e) => {
    dt.current.filter(e.value, "rating", "equals");
    setSelectedRating(e.value);
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
  const ratingFilter = (
    <Dropdown
      value={selectedRating}
      options={TEMPLATE_TABLE.RATING_LIST}
      onChange={onRatingChange}
      placeholder="Select rating"
      itemTemplate={RatingItemTemplate}
      valueTemplate={RatingSelectedItemTemplate}
      className="p-column-filter"
      showClear
    />
  );
  const categoryFilter = (
    <Dropdown
      value={selectedCategory}
      options={TEMPLATE_TABLE.CATEGORY_LIST}
      onChange={onCategoryChange}
      itemTemplate={CategoryItemTemplate}
      valueTemplate={CategoryItemSelectedTemplate}
      placeholder="Select a Category"
      className="p-column-filter"
      showClear
    />
  );
  // ***** Load column dynamic *****
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
            : col.field === "rating"
            ? RatingBodyTemplate
            : col.field === "category"
            ? categoryBodyTemplate
            : col.field === "created_on"
            ? DateBodyTemplate
            : col.field === "owner.displayName"
            ? (rowData) => DisplayNameBodyTemplate(rowData, displayNameClick)
            : null
        }
        filterElement={
          col.field === "status"
            ? statusFilter
            : col.field === "category"
            ? categoryFilter
            : col.field === "rating"
            ? ratingFilter
            : null
        }
        sortable
        header={col.header}
      />
    );
  });
  return (
    <div className="table-post-container">
      <TableComponent
        dt={dt}
        data={posts}
        dataType="posts"
        header={header}
        columns={dynamicColumns}
        actionBodyTemplate={actionBodyTemplate}
      />
    </div>
  );
}

export default TablePostComponent;
