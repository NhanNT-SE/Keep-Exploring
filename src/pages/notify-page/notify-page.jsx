import { MultiSelect } from "primereact/multiselect";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import GLOBAL_VARIABLE from "utils/global_variable";
import { actionGetUserList } from "redux/slices/user.slice";
import TableComponent from "common-components/table/table";
import { Column } from "primereact/column";
import { Chip } from "primereact/chip";
import "./notify-page.scss";
import { Button } from "primereact/button";
import DialogNotify from "common-components/dialog/dialog-notify/dialog-notify";
import { actionHideDialog, actionShowDialog } from "redux/slices/common.slice";
import { Dropdown } from "primereact/dropdown";
import { GenderItemTemplate } from "common-components/template/gender-template/gender-template";
import { SelectedGenderTemplate } from "common-components/template/gender-template/gender-template";
import DateBodyTemplate from "common-components/template/date-template/date-template";
import { GenderBodyTemplate } from "common-components/template/gender-template/gender-template";

function NotifyPage() {
  const dt = useRef(null);
  const columns = GLOBAL_VARIABLE.COLUMNS_NOTIFY;
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
    dispatch(actionShowDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
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
      dispatch(actionHideDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
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
              image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${e.imgUser}`}
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
