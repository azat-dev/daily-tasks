import { useMemo } from "react";
import { AppModelPageFactories } from "./model/AppModel";
import CurrentBacklogPageView from "../../presentation/pages/CurrentBacklogPage/CurrentBacklogPageView";
import LogInPageView from "../../presentation/pages/LogInPage/LogInPageView";
import BacklogType from "../../domain/models/BacklogType";
import DayPageView from "../../presentation/pages/DayPage/DayPageView";

const useAppPages = (appModel: AppModelPageFactories) => {
    const pages = useMemo(() => {
        return {
            SignInPage: () => {
                return (
                    <LogInPageView
                        viewModel={appModel.makeLogInPageViewModel()}
                    />
                );
            },
            BacklogPage: () => {
                const backlogDay = new Date().toISOString().split("T")[0];

                const viewModel = appModel.makeBacklogPageViewModel(
                    BacklogType.Day,
                    backlogDay
                );

                return <CurrentBacklogPageView viewModel={viewModel} />;
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
    }, [appModel]);

    return pages;
};

export default useAppPages;
