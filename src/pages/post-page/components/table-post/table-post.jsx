import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { Dialog } from "primereact/dialog";
import { Rating } from "primereact/rating";
import { Toast } from "primereact/toast";
import { Dropdown } from "primereact/dropdown";
import { MultiSelect } from "primereact/multiselect";
import React, { useRef, useState } from "react";
import "./table-post.scss";
import postList from "../../post-list.json";
import { Fragment } from "react";
import { useDispatch } from "react-redux";
import {
  actionSetSelectedPost,
  actionSetSelectedPostList,
} from "redux/slices/postSlice";
import { useHistory } from "react-router";
function TablePostComponent() {
  const toast = useRef(null);
  const dt = useRef(null);
  const statuses = ["pending", "done", "need_update"];
  const categories = ["hotel", "food", "check_in"];
  const ratings = [0, 1, 2, 3, 4, 5];
  const columns = [
    { field: "owner", header: "Owner" },
    { field: "title", header: "Title" },
    { field: "category", header: "Category" },
    { field: "status", header: "Status" },
    { field: "desc", header: "Description" },
    { field: "address", header: "Address" },
    { field: "rating", header: "Rating" },
    { field: "created_on", header: "Date" },
  ];
  const [posts, setPosts] = useState(postList);
  const [deletePostDialog, setDeletePostDialog] = useState(false);
  const [deletePostsDialog, setDeletePostsDialog] = useState(false);
  const [post, setPost] = useState(null);
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
  const deletePost = () => {
    let _posts = posts.filter((val) => val.id !== post.id);
    setPosts(_posts);
    setDeletePostDialog(false);
    setPost(null);
    dispatch(actionSetSelectedPost(null));
    toast.current.show({
      severity: "success",
      summary: "Successful",
      detail: "Product Deleted",
      life: 3000,
    });
  };
  const hideDeletePostDialog = () => {
    dispatch(actionSetSelectedPost(null));
    setDeletePostDialog(false);
  };

  const hideDeletePostsDialog = () => {
    setDeletePostsDialog(false);
  };
  const confirmDeletePost = (post) => {
    dispatch(actionSetSelectedPost(post));
    setPost(post);
    setDeletePostDialog(true);
  };

  const confirmDeleteSelected = () => {
    setDeletePostsDialog(true);
  };
  // ***** Set column to display *****
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
  // ***** Custom body do display data for each colum *****
  const ratingBodyTemplate = (rowData) => {
    return <Rating value={rowData.rating} readOnly cancel={false} />;
  };

  const statusBodyTemplate = (rowData) => {
    return (
      <React.Fragment>
        <span className={`post-status status-${rowData.status.toLowerCase()}`}>
          {rowData.status.toUpperCase().replace("_", " ")}
        </span>
      </React.Fragment>
    );
  };

  const categoryBodyTemplate = (rowData) => {
    return <span>{rowData.category.toUpperCase().replace("_", " ")}</span>;
  };
  const dateBodyTemplate = (rowData) => {
    const date = new Date(rowData.created_on);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const year = date.getFullYear();
    let dayString = "";
    let monthString = "";
    if (day > 10) {
      dayString = day;
    } else {
      dayString = "0" + day;
    }
    if (month > 10) {
      monthString = month;
    } else {
      monthString = "0" + month;
    }
    const stringDate = dayString + "-" + monthString + "-" + year;
    return <span>{stringDate}</span>;
  };

  const actionBodyTemplate = (rowData) => {
    return (
      <div className="actions-button">
        <Button
          icon="pi pi-pencil"
          className="p-button-rounded p-button-success p-mr-2"
          onClick={() => editPost(rowData)}
        />
        <Button
          icon="pi pi-trash"
          className="p-button-danger p-button-rounded p-mr-2"
          onClick={() => confirmDeletePost(rowData)}
        />
      </div>
    );
  };
  // ***** Custom header table *****
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
  // ***** Custom layout for Dialog *****
  const deletePostDialogFooter = (
    <Fragment>
      <Button
        label="No"
        icon="pi pi-times"
        className="p-button-text"
        onClick={hideDeletePostDialog}
      />
      <Button
        label="Yes"
        icon="pi pi-check"
        className="p-button-text"
        onClick={deletePost}
      />
    </Fragment>
  );
  const deletePostsDialogFooter = (
    <Fragment>
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
    </Fragment>
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
  // ***** Custom layout for custom filter column *****
  const statusItemTemplate = (option) => {
    return (
      <span className={`post-status status-${option}`}>
        {option.toUpperCase().replace("_", " ")}
      </span>
    );
  };
  const categoryItemTemplate = (option) => {
    return <span>{option.toUpperCase().replace("_", " ")}</span>;
  };
  const ratingItemTemplate = (option) => {
    return <span>{option}</span>;
  };
  // ***** Custom filter column *****
  const statusFilter = (
    <Dropdown
      value={selectedStatus}
      options={statuses}
      onChange={onStatusChange}
      itemTemplate={statusItemTemplate}
      placeholder="Select a Status"
      className="p-column-filter"
      showClear
    />
  );
  const ratingFilter = (
    <Dropdown
      value={selectedRating}
      options={ratings}
      onChange={onRatingChange}
      itemTemplate={ratingItemTemplate}
      placeholder="Select rating"
      className="p-column-filter"
      showClear
    />
  );
  const categoryFilter = (
    <Dropdown
      value={selectedCategory}
      options={categories}
      onChange={onCategoryChange}
      itemTemplate={categoryItemTemplate}
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
            ? statusBodyTemplate
            : col.field === "rating"
            ? ratingBodyTemplate
            : col.field === "category"
            ? categoryBodyTemplate
            : col.field === "created_on"
            ? dateBodyTemplate
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

        <Column body={actionBodyTemplate} style={{ width: "115px" }}></Column>
        {dynamicColumns}
      </DataTable>

      <Dialog
        visible={deletePostDialog}
        style={{ width: "450px" }}
        header="Confirm"
        modal
        footer={deletePostDialogFooter}
        onHide={hideDeletePostDialog}
      >
        <div className="confirmation-content">
          <i
            className="pi pi-exclamation-triangle p-mr-3"
            style={{ fontSize: "2rem" }}
          />
          {post && (
            <span>
              Are you sure you want to delete <b>{post.name}</b>?
            </span>
          )}
        </div>
      </Dialog>
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
