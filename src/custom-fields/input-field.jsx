import React from "react";
import PropTypes from "prop-types";
import { useController } from "react-hook-form";
import { TextField } from "@material-ui/core";
function InputField(props) {
  const { field, meta } = useController(props);
  const { message } = props;
  return (
    <TextField
      {...field}
      {...props}
      fullWidth
      margin="normal"
      variant="outlined"
      error={message ? true : false}
      helperText={message}
    />
  );
}

export default InputField;
