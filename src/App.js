import LoadingComponent from "common-components/loading/loading";
import NotFound from "pages/not-found/not-found";
import { Suspense } from "react";
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
            <Route path="/" component={MainPage} />
            <Route path="*" component={NotFound} />
          </Switch>
        </BrowserRouter>
      </Suspense>
    </div>
  );
}

export default App;
