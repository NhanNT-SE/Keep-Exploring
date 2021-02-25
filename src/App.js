import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import "./App.scss";
import LoginPage from "./pages/login-page/login-page";
import MainPage from "./pages/main-page/main-page";

function App() {
  return (
    <div className="app">
      <Router>
        <Switch>
          <Route exact path="/">
            <LoginPage />
          </Route>
          <Route path="/home">
            <MainPage />
          </Route>
        </Switch>
      </Router>
    </div>
  );
}

export default App;
