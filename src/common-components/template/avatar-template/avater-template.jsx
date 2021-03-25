import { Avatar } from "primereact/avatar";
import GLOBAL_VARIABLE from "utils/global_variable";

export const AvatarBodyTemplate = (rowData) => {
  return (
    <Avatar
      image={`${GLOBAL_VARIABLE.BASE_URL_IMAGE}/user/${rowData.imgUser}`}
      imageAlt="avatar"
      className="p-mr-2"
      shape="circle"
    />
  );
};
