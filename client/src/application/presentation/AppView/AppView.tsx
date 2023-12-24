import React, { useEffect } from "react";

import AppRouter from "../../../presentation/router";
import useUpdatesFrom from "../../../presentation/utils/useUpdatesFrom";
import AppModel from "../../model/DI/coordinator/AppCoordinator";
import ModalPresenter from "../ModalPresenter";
import useAppPages from "../useAppPages";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

export interface AppViewProps {
    model: AppModel;
}

const AppView = ({ model }: AppViewProps) => {
    useUpdatesFrom(model.authState, model.currentModal);
    const pages = useAppPages(model.getPages);

    useEffect(() => {
        model.start();
    }, [model]);

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

export default React.memo(AppView);
