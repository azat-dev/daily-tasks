import React from "react";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AppRouter from "../../presentation/router";
import useAppModel from "./useAppModel";
import usePageViews from "./usePageViews";
import AddTaskModalView from "../../presentation/modals/AddTaskModal/AddTaskModalView";
import useCurrentModalView from "./useCurrentModalView";

const App = () => {
    const { authState, currentModal, viewModelFactories } = useAppModel();
    const views = usePageViews(viewModelFactories);

    const CurrentModalView = useCurrentModalView(currentModal);

    return (
        <div className="appRoot">
            <AppRouter
                authState={authState}
                signInPagePath="/sign-in"
                views={views}
            />
            {CurrentModalView && <CurrentModalView />}
        </div>
    );
};

export default App;
