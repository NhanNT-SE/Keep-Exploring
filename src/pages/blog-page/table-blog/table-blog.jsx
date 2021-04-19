import React, { useEffect, useRef, useState } from "react";
import GLOBAL_VARIABLE from "utils/global_variable";
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
import { actionGetAllBlog } from "redux/slices/blogSlice";
import DisplayNameBodyTemplate from "common-components/template/display-name-template/display-name-template";
import TableComponent from "common-components/table/table";
function TableBlogComponent() {
  const columns = GLOBAL_VARIABLE.COLUMNS_BLOG;
  const dt = useRef(null);
  const history = useHistory();
  const dispatch = useDispatch();

  const blogs = useSelector((state) => state.blog.blogList);
  // const [blogs, setBlogs] = useState([
  //   {
  //     _id: "77185666-0d65-4225-88f7-68f5bfeef75e",
  //     status: "need_update",
  //     owner: "Gabriella Flewin",
  //     created_on: "2020-08-27T10:43:15Z",
  //     title:
  //       "In hac habitasse platea dictumst. Etiam faucibus cursus urna. Ut tellus.",
  //   },
  //   {
  //     _id: "7cd3e630-a5ca-4c1a-93f0-67560b84eac5",
  //     status: "pending",
  //     owner: "Luther Geraldini",
  //     created_on: "2021-02-02T18:31:30Z",
  //     title:
  //       "Quisque id justo sit amet sapien dignissim vestibulum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est. Donec odio justo, sollicitudin ut, suscipit a, feugiat et, eros.",
  //   },
  //   {
  //     _id: "9a63410e-a2a8-4242-9082-4f23e48445b7",
  //     status: "done",
  //     owner: "Lia Ettery",
  //     created_on: "2020-12-21T07:00:54Z",
  //     title:
  //       "Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.",
  //   },
  //   {
  //     _id: "6ef25f92-5cdf-4ac3-bdc4-24f2c0b0c88d",
  //     status: "done",
  //     owner: "Emelyne Chartres",
  //     created_on: "2020-04-04T08:11:44Z",
  //     title:
  //       "Phasellus sit amet erat. Nulla tempus. Vivamus in felis eu sapien cursus vestibulum.",
  //   },
  //   {
  //     _id: "f16c3166-d890-4562-a969-2323109b25cd",
  //     status: "done",
  //     owner: "Dyane Beirne",
  //     created_on: "2021-02-07T07:43:23Z",
  //     title:
  //       "Proin leo odio, porttitor id, consequat in, consequat ut, nulla. Sed accumsan felis. Ut at dolor quis odio consequat varius.",
  //   },
  //   {
  //     _id: "7edbedae-ba73-461f-b73e-224de52f13c2",
  //     status: "pending",
  //     owner: "Terri Groveham",
  //     created_on: "2020-10-31T19:28:27Z",
  //     title:
  //       "Donec diam neque, vestibulum eget, vulputate ut, ultrices vel, augue. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec pharetra, magna vestibulum aliquet ultrices, erat tortor sollicitudin mi, sit amet lobortis sapien sapien non mi. Integer ac neque.",
  //   },
  //   {
  //     _id: "e99aad2f-f122-4a11-9456-774b535e28f8",
  //     status: "pending",
  //     owner: "Carolan Wollrauch",
  //     created_on: "2020-10-28T11:24:05Z",
  //     title:
  //       "Phasellus sit amet erat. Nulla tempus. Vivamus in felis eu sapien cursus vestibulum.",
  //   },
  //   {
  //     _id: "6586dc6b-fdba-4d1c-bac7-a6fa45f81c60",
  //     status: "need_update",
  //     owner: "Gena Andryszczak",
  //     created_on: "2020-09-29T16:39:51Z",
  //     title:
  //       "In hac habitasse platea dictumst. Etiam faucibus cursus urna. Ut tellus.",
  //   },
  //   {
  //     _id: "523adbcc-03c9-45ec-8148-9a91e928e31b",
  //     status: "need_update",
  //     owner: "Hewett Bangham",
  //     created_on: "2020-11-24T05:50:59Z",
  //     title:
  //       "Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat. Vestibulum sed magna at nunc commodo placerat.",
  //   },
  //   {
  //     _id: "9d95dd8a-c658-443d-b88b-cbc94f0aa87a",
  //     status: "pending",
  //     owner: "Brade Lening",
  //     created_on: "2020-04-29T18:50:07Z",
  //     title:
  //       "Curabitur gravida nisi at nibh. In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem.",
  //   },
  //   {
  //     _id: "391d2e71-5efe-4fb5-a03c-4202a3e60486",
  //     status: "done",
  //     owner: "Adria Karslake",
  //     created_on: "2021-01-17T03:13:02Z",
  //     title:
  //       "Fusce posuere felis sed lacus. Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.",
  //   },
  //   {
  //     _id: "cd0ce3b6-ed57-44e6-9ecb-b10c1b8afb78",
  //     status: "pending",
  //     owner: "Nobie Welfare",
  //     created_on: "2020-11-27T13:31:47Z",
  //     title:
  //       "In hac habitasse platea dictumst. Etiam faucibus cursus urna. Ut tellus.",
  //   },
  //   {
  //     _id: "53aa0d54-48a0-458b-8b29-11434f6c56d8",
  //     status: "pending",
  //     owner: "Bud Kleinhaut",
  //     created_on: "2020-10-05T09:11:12Z",
  //     title:
  //       "Proin leo odio, porttitor id, consequat in, consequat ut, nulla. Sed accumsan felis. Ut at dolor quis odio consequat varius.",
  //   },
  //   {
  //     _id: "679409c1-fa39-4a5f-8e2d-a0274d82e4b0",
  //     status: "pending",
  //     owner: "Merrielle Fayerman",
  //     created_on: "2020-11-21T23:18:22Z",
  //     title:
  //       "Aenean fermentum. Donec ut mauris eget massa tempor convallis. Nulla neque libero, convallis eget, eleifend luctus, ultricies eu, nibh.",
  //   },
  //   {
  //     _id: "9df08b9c-1835-45cf-8f30-a8e42fc88441",
  //     status: "need_update",
  //     owner: "Gracie Andrelli",
  //     created_on: "2020-09-21T09:06:44Z",
  //     title:
  //       "Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui.",
  //   },
  //   {
  //     _id: "e072fa1b-cb77-489d-874f-7f8632c45521",
  //     status: "need_update",
  //     owner: "Fabien Whightman",
  //     created_on: "2020-05-02T23:10:49Z",
  //     title:
  //       "Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat.",
  //   },
  //   {
  //     _id: "5b432600-f181-4c24-be1e-328d25ad78a5",
  //     status: "need_update",
  //     owner: "Gail McLachlan",
  //     created_on: "2020-12-29T08:09:19Z",
  //     title:
  //       "Aenean lectus. Pellentesque eget nunc. Donec quis orci eget orci vehicula condimentum.",
  //   },
  //   {
  //     _id: "40948ada-409d-4a65-b0d7-7ce6028fbb62",
  //     status: "need_update",
  //     owner: "Violette Camillo",
  //     created_on: "2020-07-01T21:40:27Z",
  //     title:
  //       "Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.",
  //   },
  //   {
  //     _id: "1313f1d2-69c6-4fa4-87e1-03d9d17805c7",
  //     status: "need_update",
  //     owner: "Annalee Wisedale",
  //     created_on: "2021-02-16T10:30:56Z",
  //     title:
  //       "Donec diam neque, vestibulum eget, vulputate ut, ultrices vel, augue. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec pharetra, magna vestibulum aliquet ultrices, erat tortor sollicitudin mi, sit amet lobortis sapien sapien non mi. Integer ac neque.",
  //   },
  //   {
  //     _id: "3750cca0-bbab-4dba-a62a-779b4ae42426",
  //     status: "need_update",
  //     owner: "Jessika Berzen",
  //     created_on: "2020-08-06T07:36:20Z",
  //     title:
  //       "Phasellus sit amet erat. Nulla tempus. Vivamus in felis eu sapien cursus vestibulum.",
  //   },
  // ]);
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
      options={GLOBAL_VARIABLE.STATUS_LIST}
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
