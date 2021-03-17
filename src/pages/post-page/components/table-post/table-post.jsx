import {
  CategoryItemSelectedTemplate,
  CategoryItemTemplate,
} from "common-components/template/category-template/category-template";
import DateBodyTemplate from "common-components/template/date-template/date-template";
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
import GLOBAL_VARIABLE from "utils/global_variable";
import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { Dialog } from "primereact/dialog";
import { Dropdown } from "primereact/dropdown";
import { MultiSelect } from "primereact/multiselect";
import { Toast } from "primereact/toast";
import React, { useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useHistory } from "react-router";
import {
  actionSetSelectedPost,
  actionSetSelectedPostList,
} from "redux/slices/postSlice";
import postList from "../../post-list.json";
import "./table-post.scss";
function TablePostComponent() {
  const toast = useRef(null);
  const dt = useRef(null);
  const columns = GLOBAL_VARIABLE.COLUMNS_POST;
  const [posts, setPosts] = useState(postList);
  const [deletePostsDialog, setDeletePostsDialog] = useState(false);
  const [selectedPosts, setSelectedPosts] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [selectedRating, setSelectedRating] = useState(null);
  const [selectedColumns, setSelectedColumns] = useState(columns);
  const dispatch = useDispatch();
  const history = useHistory();
  const editPost = (post) => {
    dispatch(actionSetSelectedPost(post));
    history.push(`post/${post.id}`);
  };

  const hideDeletePostsDialog = () => {
    setDeletePostsDialog(false);
  };

  const confirmDeleteSelected = () => {
    setDeletePostsDialog(true);
  };
  const onColumnToggle = (event) => {
    let selectedColumns = event.value;
    let orderedSelectedColumns = columns.filter((col) =>
      selectedColumns.some((sCol) => sCol.field === col.field)
    );
    setSelectedColumns(orderedSelectedColumns);
  };
  const deleteSelectedProducts = () => {
    let _posts = posts.filter((val) => !selectedPosts.includes(val));
    setPosts(_posts);
    setDeletePostsDialog(false);
    setSelectedPosts(null);
    dispatch(actionSetSelectedPostList(null));
    toast.current.show({
      severity: "success",
      summary: "Successful",
      detail: "Products Deleted",
      life: 3000,
    });
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

  const header = (
    <div className="table-header">
      <MultiSelect
        value={selectedColumns}
        options={columns}
        optionLabel="header"
        onChange={onColumnToggle}
        style={{ width: "20em" }}
      />

      <Button
        icon="pi pi-trash"
        className="p-button-danger p-button-rounded p-button-text"
        onClick={confirmDeleteSelected}
        disabled={!selectedPosts || !selectedPosts.length}
      />
    </div>
  );

  const deletePostsDialogFooter = (
    <div>
      <Button
        label="No"
        icon="pi pi-times"
        className="p-button-text"
        onClick={hideDeletePostsDialog}
      />
      <Button
        label="Yes"
        icon="pi pi-check"
        className="p-button-text"
        onClick={deleteSelectedProducts}
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
      options={GLOBAL_VARIABLE.STATUS_LIST}
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
      options={GLOBAL_VARIABLE.RATING_LIST}
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
      options={GLOBAL_VARIABLE.CATEGORY_LIST}
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
      <Toast ref={toast} />
      <DataTable
        ref={dt}
        value={posts}
        selection={selectedPosts}
        onSelectionChange={(e) => {
          dispatch(actionSetSelectedPostList(e.value));
          setSelectedPosts(e.value);
        }}
        dataKey="id"
        paginator
        resizableColumns={true}
        rows={10}
        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport"
        currentPageReportTemplate="Showing {first} to {last} of {totalRecords} posts"
        header={header}
        scrollable
        scrollHeight="calc(100% - 200px)"
        className="p-datatable-gridlines"
      >
        <Column
          selectionMode="multiple"
          headerStyle={{ width: "3rem" }}
        ></Column>

        <Column
          body={actionBodyTemplate}
          header="Edit"
          style={{ width: "4rem" }}
        ></Column>
        {dynamicColumns}
      </DataTable>

      <Dialog
        visible={deletePostsDialog}
        style={{ width: "450px" }}
        header="Confirm"
        modal
        footer={deletePostsDialogFooter}
        onHide={hideDeletePostsDialog}
      >
        <div className="confirmation-content">
          <i
            className="pi pi-exclamation-triangle p-mr-3"
            style={{ fontSize: "2rem" }}
          />
          {posts && (
            <span>Are you sure you want to delete the selected posts?</span>
          )}
        </div>
      </Dialog>
    </div>
  );
}

export default TablePostComponent;
