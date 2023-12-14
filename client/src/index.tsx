import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import AppRoot from "./main/app/App";
import AppSettings from "./main/app/AppSettings";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

const settings: AppSettings = {
    api: {
        basePath: "http://localhost:8080",
    },
};

root.render(
    <React.StrictMode>
        <AppRoot settings={settings} />
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
