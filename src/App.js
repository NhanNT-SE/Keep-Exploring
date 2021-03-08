import LoadingComponent from "common-components/loading/loading";
import NotFound from "pages/not-found/not-found";
import { Suspense } from "react";
import { useSelector } from "react-redux";
import { BrowserRouter, Redirect, Route, Switch } from "react-router-dom";
import "./App.scss";
import LoginPage from "./pages/login-page/login-page";
import MainPage from "./pages/main-page/main-page";

function App() {
  return (
    <div className="app">
      <Suspense fallback={LoadingComponent}>
        <BrowserRouter>
          <Switch>
            <Redirect exact from="/" to="/login" />
            <Route exact path="/login" component={LoginPage} />
            {/* <ProtectedRoute exact path="/home" component={MainPage} /> */}
            <Route exact path="/home" component={MainPage} />
            <Route path="*" component={NotFound} />
          </Switch>
        </BrowserRouter>
      </Suspense>
    </div>
  );
}

export const ProtectedRoute = ({ component: Component, ...rest }) => {
  const user = useSelector((state) => state.user.user);
  return (
    <Route
      {...rest}
      render={(props) => {
        if (user) {
          return <Component {...props} />;
        } else {
          return (
            <Redirect
              to={{
                pathname: "/login",
                state: {
                  from: props.location,
                },
              }}
            />
          );
        }
      }}
    />
  );
};
export default App;
