import React, { useEffect } from "react";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AppRouter from "../../presentation/router";
import AppModelImpl from "./model/AppModelImpl";
import useUpdatesFrom from "../../presentation/pages/LogInPage/useUpdatesFrom";
import ModalPresenter from "./ModalPresenter";
import AppSettings from "./AppSettings";
import AppModel from "./model/AppModel";
import useAppPages from "./useAppPages";

const AppRoot = (props: { settings: AppSettings }) => {
    const model = new AppModelImpl(props.settings);
    (window as any).appModel = model;

    useEffect(() => {
        model.start();
    }, [model]);
    return <AppView model={model} />;
};

const AppView = ({ model }: { model: AppModel }) => {
    useUpdatesFrom(model.authState, model.currentModal);
    const pages = useAppPages(model.getPages);

    return (
        <div className="appRoot">
            <AppRouter
                authState={model.authState.value}
                signInPagePath="/sign-in"
                pages={pages}
            />
            <ModalPresenter currentModal={model.currentModal.value} />
        </div>
    );
};

export default AppRoot;
