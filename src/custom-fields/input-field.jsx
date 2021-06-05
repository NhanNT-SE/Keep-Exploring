import { TextField } from "@material-ui/core";
import React from "react";
import { useController } from "react-hook-form";
function InputField(props) {
  const { field } = useController(props);
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
