import { useMemo } from "react";
import { AppModelPageFactories } from "./model/AppModel";
import LogInPageView from "../../presentation/pages/LogInPage/LogInPageView";
import DayPageView from "../../presentation/pages/DayPage/DayPageView";

const useAppPages = (getViewModelFactories: () => AppModelPageFactories) => {
    const pages = useMemo(() => {
        const appModel = getViewModelFactories();
        return {
            SignInPage: () => {
                return (
                    <LogInPageView
                        viewModel={appModel.makeLogInPageViewModel()}
                    />
                );
            },
            DayPage: () => {
                const backlogDay = new Date().toISOString().split("T")[0];
                const viewModel =
                    appModel.makeBacklogDayPageViewModel(backlogDay);

                return <DayPageView viewModel={viewModel} />;
            },
            WeekPage: () => {
                return <h1>Week Page</h1>;
            },
        };
    }, [getViewModelFactories]);

    return pages;
};

export default useAppPages;
