import React, { useEffect, useMemo } from "react";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AppRouter from "../../presentation/router";
import AppModelImpl from "./model/AppModelImpl";
import { useViewModelBinding } from "../../presentation/pages/LogInPage/useBinding";
import ModalPresenter from "./ModalPresenter";
import useAppPages from "./useAppPages";
import AppSettings from "./AppSettings";
import AppModel from "./model/AppModel";

const AppRoot = (props: { settings: AppSettings }) => {
    const model = new AppModelImpl(props.settings);

    useEffect(() => {
        model.start();
    }, [model]);
    return <AppView model={model} />;
};

const AppView = (props: { model: AppModel }) => {
    const model = useViewModelBinding(props.model);
    const pages = useAppPages(model.getPages());

    return (
        <div className="appRoot">
            <AppRouter
                authState={model.authState}
                signInPagePath="/sign-in"
                pages={pages}
            />
            {model.currentModal && (
                <ModalPresenter currentModal={model.currentModal} />
            )}
        </div>
    );
};

export default AppRoot;
