import React, { useMemo } from "react";

import LogInPageView from "../../presentation/pages/LogInPage/LogInPageView";
import { ViewModelFactories } from "./useAppModel";
import CurrentBacklogPageView from "../../presentation/pages/CurrentBacklogPage/CurrentBacklogPageView";
import DayPageView from "../../presentation/pages/DayPage/DayPageView";
import useViewModelHookFromFactory from "./useViewModelHookFromFactory";

const usePageViews = (viewModelFactories: ViewModelFactories) => {
    return useMemo(() => {
        return {
            SignInPage: () => {
                const viewModel = useViewModelHookFromFactory(
                    viewModelFactories.logInPage
                );
                return <LogInPageView viewModel={viewModel} />;
            },
            BacklogPage: () => {
                const viewModel = useViewModelHookFromFactory(
                    viewModelFactories.backlogPage
                );
                return <CurrentBacklogPageView viewModel={viewModel} />;
            },
            DayPage: () => {
                const viewModel = useViewModelHookFromFactory(
                    viewModelFactories.currentDayBacklogPage
                );

                return <DayPageView viewModel={viewModel} />;
                // return <CurrentBacklogPageView viewModel={viewModel} />;
            },
            WeekPage: () => {
                return <h1>Week Page</h1>;
            },
        };
    }, [viewModelFactories]);
};

export default usePageViews;
