import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import { actionLogout } from "redux/slices/userSlice";

function MainPage() {
  const user = useSelector((state) => state.user.user);
  const history = useHistory();
  const dispatch = useDispatch();
  const logOut = () => {
    dispatch(actionLogout());
  };
  useEffect(() => {
    if (!user) {
      history.push("/login");
    }
  }, [user]);
  return (
    <div>
      <button onClick={logOut}>Logout</button>
    </div>
  );
}

export default MainPage;
