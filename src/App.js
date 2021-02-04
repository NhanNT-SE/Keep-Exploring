import logo from "./logo.svg";
import "./App.css";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>Keep Exploring Web Admin</p>
        <p>Server web: https://web-app.duuk7ot2ayt8v.amplifyapp.com/</p>
        <p>Server backend: https://web-app.duuk7ot2ayt8v.amplifyapp.com/</p>
        <p>Server db: https://web-app.duuk7ot2ayt8v.amplifyapp.com/</p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
