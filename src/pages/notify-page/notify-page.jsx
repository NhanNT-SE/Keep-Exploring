import DialogNotify from "common-components/dialog/dialog-notify/dialog-notify";
import TableComponent from "common-components/table/table";
import DateBodyTemplate from "common-components/template/date-template/date-template";
import { GenderBodyTemplate, GenderItemTemplate, SelectedGenderTemplate } from "common-components/template/gender-template/gender-template";
import { Button } from "primereact/button";
import { Chip } from "primereact/chip";
import { Column } from "primereact/column";
import { Dropdown } from "primereact/dropdown";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionHideDialog, actionShowDialog } from "redux/slices/dialog.slice";
import { actionGetUserList } from "redux/slices/user.slice";
import {COL_TABLE,TEMPLATE_TABLE,CONFIG_URL,DIALOG} from "utils/global_variable";
import "./notify-page.scss";

function NotifyPage() {
  const dt = useRef(null);
  const columns = COL_TABLE.COLUMNS_NOTIFY;
  const dispatch = useDispatch();
  const [selectedData, setSelectedData] = useState(null);
  const [selectedUser, setSelectedUser] = useState([]);
  const userList = useSelector((state) => state.user.userList);
  const [selectedGender, setSelectedGender] = useState(null);

  const onGenderChange = (e) => {
    dt.current.filter(e.value, "gender", "equals");
    setSelectedGender(e.value);
  };
  const sendNotify = () => {
    console.log(selectedUser);
    dispatch(actionShowDialog(DIALOG.DIALOG_NOTIFY));
  };
  const genderFilter = (
    <Dropdown
      value={selectedGender}
      options={TEMPLATE_TABLE.GENDER_LIST}
      onChange={onGenderChange}
      itemTemplate={GenderItemTemplate}
      valueTemplate={SelectedGenderTemplate}
      placeholder="Select a gender"
      className="p-column-filter"
      showClear
    />
  );
  const dynamicColumns = columns.map((col, i) => {
    return (
      <Column
        key={col.field}
        field={col.field}
        filter
        filterMatchMode="contains"
        sortable
        filterElement={col.field === "gender" ? genderFilter : null}
        body={
          col.field === "gender"
            ? GenderBodyTemplate
            : col.field === "created_on"
            ? DateBodyTemplate
            : null
        }
        header={col.header}
      />
    );
  });
  useEffect(() => {
    dispatch(actionGetUserList());
    return () => {
      dispatch(actionHideDialog(DIALOG.DIALOG_NOTIFY));
    };
  }, []);
  useEffect(() => {
    if (selectedData && setSelectedData.length > 0) {
      const listUser = selectedData.map((e) => e._id);
      setSelectedUser(listUser);
    }
  }, [selectedData]);
  return (
    <div className="notify-page-container">
      <DialogNotify type="multi" userList={selectedUser} />
      <div className="header">
        <h1>NOTIFICATION</h1>

        <Button
          label="Send Notify"
          icon="pi pi-send"
          disabled={selectedData && selectedData.length > 0 ? false : true}
          onClick={sendNotify}
        />
      </div>
      {selectedData && selectedData.length > 0 ? (
        <div className="p-d-flex p-ai-center p-flex-wrap chip-container">
          {selectedData.map((e) => (
            <Chip
              key={e._id}
              label={e.email}
              image={`${CONFIG_URL.BASE_URL_IMAGE}/user/${e.imgUser}`}
              className="custom-chip p-mb-2"
              removable
              onRemove={() => {
                const listUser = selectedData.filter(
                  (item) => item._id !== e._id
                );
                setSelectedData(listUser);
              }}
            />
          ))}
        </div>
      ) : null}
      <div className="table-notify-container">
        <TableComponent
          dt={dt}
          data={userList}
          dataType="users"
          columns={dynamicColumns}
          selectionMode="multiple"
          selectedData={selectedData}
          setSelectedData={setSelectedData}
        />
      </div>
    </div>
  );
}

export default NotifyPage;
