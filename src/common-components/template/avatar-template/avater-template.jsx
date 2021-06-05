import { Avatar } from "primereact/avatar";
import {CONFIG_URL} from "utils/global_variable";

export const AvatarBodyTemplate = (rowData) => {
  return (
    <Avatar
      image={`${CONFIG_URL.BASE_URL_IMAGE}/user/${rowData.avatar}`}
      imageAlt="avatar"
      className="p-mr-2"
      shape="circle"
    />
  );
};
