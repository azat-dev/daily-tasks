import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import AppSettings from "./application/AppSettings";
import DataDIContainerImpl from "./application/model/DI/data/DataDIContainerImpl";
import DomainDIContainer from "./application/model/DI/domain/DomainDIContainer";
import PresentationDiContainerImpl from "./application/model/DI/presentation/PresentationDiContainerImpl";
import AppCoordinatorImpl from "./application/model/DI/coordinator/AppCoordinatorImpl";
import AppView from "./application/presentation/AppView/AppView";

const root = ReactDOM.createRoot(
    document.getElementById("root") as HTMLElement
);

const settings: AppSettings = {
    api: {
        basePath: "http://localhost:8080",
    },
};

const makeAppModel = (settings: AppSettings) => {
    const data = new DataDIContainerImpl(settings.api.basePath);
    const domain = new DomainDIContainer(data);
    const presentationModel = new PresentationDiContainerImpl(domain);

    const appModel = new AppCoordinatorImpl(presentationModel);
    (window as any).appModel = appModel;

    return appModel;
};

const appModel = makeAppModel(settings);

root.render(
    <React.StrictMode>
        <AppView model={appModel} />
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
