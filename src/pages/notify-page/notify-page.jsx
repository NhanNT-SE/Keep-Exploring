import { MultiSelect } from "primereact/multiselect";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import GLOBAL_VARIABLE from "utils/global_variable";
import { actionGetListUser } from "redux/slices/userSlice";
import TableComponent from "common-components/table/table";
import { Column } from "primereact/column";
import { Chip } from "primereact/chip";
import "./notify-page.scss";
import { Button } from "primereact/button";
import DialogNotify from "common-components/dialog/dialog-notify/dialog-notify";
import { actionShowDialog } from "redux/slices/commonSlice";

function NotifyPage() {
  const dt = useRef(null);
  const columns = GLOBAL_VARIABLE.COLUMNS_NOTIFY;
  const dispatch = useDispatch();
  const [selectedData, setSelectedData] = useState(null);
  const [selectedUser, setSelectedUser] = useState([]);
  const userList = useSelector((state) => state.user.userList);

  const dynamicColumns = columns.map((col, i) => {
    return (
      <Column
        key={col.field}
        field={col.field}
        filter
        filterMatchMode="contains"
        sortable
        header={col.header}
      />
    );
  });
  const sendNotify = () => {
    console.log(selectedUser);
    dispatch(actionShowDialog(GLOBAL_VARIABLE.DIALOG_NOTIFY));
  };
  useEffect(() => {
    dispatch(actionGetListUser());
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
