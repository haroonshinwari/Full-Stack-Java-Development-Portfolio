import React from "react";
import "./App.css";
import Container from "./components/Container.js";

function App() {
    return (
        <div className="App">
            <header className="header">
                Vending Machine <hr style={{ color: "red", width: "70%" }} />
                <br />
            </header>

            <Container />
        </div>
    );
}

export default App;
