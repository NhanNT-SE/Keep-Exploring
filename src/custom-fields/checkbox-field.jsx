import { Checkbox, FormControlLabel } from "@material-ui/core";
import React from "react";

function CheckBoxField(props) {
  return (
    <FormControlLabel
      control={<Checkbox {...props} color="primary" />}
      label={props.label}
    />
  );
}

export default CheckBoxField;
