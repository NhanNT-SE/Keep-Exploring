import { SelectedStatusTemplate } from "common-components/template/status-template/status-template";
import { StatusItemTemplate } from "common-components/template/status-template/status-template";
import {TEMPLATE_TABLE} from "utils/global_variable";
import { Dropdown } from "primereact/dropdown";
import React, { useEffect, useState } from "react";

function DropdownStatus(props) {
  const [selectedStatus, setSelectedStatus] = useState(null);
  const { getValueStatus } = props;
  const onStatusChange = (e) => {
    if (e.value) {
      setSelectedStatus(e.value);
    }
  };
  useEffect(() => {
    if (selectedStatus && getValueStatus) {
      getValueStatus(selectedStatus);
    }
  }, [selectedStatus]);
  return (
    <Dropdown
      {...props}
      value={selectedStatus}
      options={TEMPLATE_TABLE.STATUS_LIST}
      onChange={onStatusChange}
      placeholder="Select a Status"
      itemTemplate={(option) => <StatusItemTemplate option={option} />}
      valueTemplate={(option) => (
        <SelectedStatusTemplate option={option} placeholder="Select a Status" />
      )}
    />
  );
}

export default DropdownStatus;
