import React, { useCallback, useEffect, useMemo } from "react";

import useLoginPageViewModel from "../../presentation/pages/LogInPage/useLogInPageViewModel";
import LogInPageViewModel from "../../presentation/pages/LogInPage/LogInPageViewModel";
import LogInByUserNameAndPasswordUseCaseImpl from "../../domain/usecases/LogInByUserNameAndPasswordUseCase/LogInByUserNameAndPasswordUseCaseImpl";
import DefaultAuthenticationService from "../../data/services/DefaultAuthenticationService";
import { Configuration, DefaultApi } from "../../data/API";
import ListenAuthenticationStateUseCaseImpl from "../../domain/usecases/ListenAuthenticationStateUseCase/ListenAuthenticationStateUseCaseImpl";
import AuthTokensRepositoryImpl from "../../data/repositories/AuthTokensRepositoryImpl";
import StartNewSessionUseCaseImpl from "../../domain/usecases/StartNewSessionUseCase/StartNewSessionUseCaseImpl";
import AuthStateRepositoryImpl from "../../data/repositories/AuthStateRepositoryImpl";
import AuthState from "../../domain/models/AuthState";
import CurrentBacklogPageViewModel from "../../presentation/pages/CurrentBacklogPage/CurrentBacklogPageViewModel";
import useCurrentBacklogPageViewModelImpl from "../../presentation/pages/CurrentBacklogPage/useCurrentBacklogPageViewModelImpl";
import DayPageViewViewModel from "../../presentation/pages/DayPage/DayPageViewModel";
import useDayPageViewModel from "../../presentation/pages/DayPage/useDayPageViewModelImpl";
import ListTasksInBacklogUseCaseImpl from "../../domain/usecases/ListTasksInBacklogUseCase/ListTasksInBacklogUseCaseImpl";
import TasksRepositoryImpl from "../../data/repositories/TasksRepositoryImpl";
import TaskMapperImpl from "../../data/repositories/TaskMapper/TaskMapperImpl";
import StartTaskUseCaseImpl from "../../domain/usecases/StartTaskUseCase/StartTaskUseCaseImpl";
import StopTaskUseCaseImpl from "../../domain/usecases/StopTaskUseCase/StopTaskUseCaseImpl";
import DeleteTaskUseCaseImpl from "../../domain/usecases/DeleteTaskUseCase/DeleteTaskUseCaseImpl";
import AddTaskViewModel from "../../presentation/modals/AddTaskModal/AddTaskModalViewModel";
import useAddTaskViewModel from "../../presentation/modals/AddTaskModal/useAddTaskModalViewModel";
import BacklogType from "../../domain/models/BacklogType";
import AddNewTaskUseCaseImpl from "../../domain/usecases/AddNewTaskUseCase/AddNewTaskUseCaseImpl";
import { useParams } from "react-router-dom";

export interface ViewModelHook<ViewModel> {
    (): ViewModel;
}

export interface ViewModelHookFactory<ViewModel> {
    make(): ViewModelHook<ViewModel>;
}

export interface ViewModelFactories {
    logInPage: ViewModelHookFactory<LogInPageViewModel>;
    backlogPage: ViewModelHookFactory<CurrentBacklogPageViewModel>;
    currentDayBacklogPage: ViewModelHookFactory<DayPageViewViewModel>;
}

export type CurrentModalState = null | {
    type: "addTask";
    viewModelFactory: ViewModelHookFactory<AddTaskViewModel>;
};

export interface AppModel {
    currentModal: CurrentModalState;
    authState: AuthState;
    viewModelFactories: ViewModelFactories;
}

const useAppModel = (): AppModel => {
    const [currentModal, setCurrentModal] =
        React.useState<CurrentModalState>(null);

    const { viewModelFactories, useAuthState } = useMemo(() => {
        const localTokensRepository = new AuthTokensRepositoryImpl();

        const apiClient = new DefaultApi(
            new Configuration({
                basePath: "http://localhost:8080",
                accessToken: async () => {
                    const tokens = await localTokensRepository.getTokens();
                    return tokens?.accessToken ?? "";
                },
            })
        );

        const authService = new DefaultAuthenticationService(apiClient);

        const authStateRepository = new AuthStateRepositoryImpl();

        const listenAuthStateUseCase = new ListenAuthenticationStateUseCaseImpl(
            authStateRepository
        );

        const startNewSessionUseCase = new StartNewSessionUseCaseImpl(
            authService,
            localTokensRepository,
            authStateRepository
        );

        const tasksRepository = new TasksRepositoryImpl(
            apiClient,
            new TaskMapperImpl()
        );

        const startTaskUseCase = new StartTaskUseCaseImpl(tasksRepository);

        const stopTaskUseCase = new StopTaskUseCaseImpl(tasksRepository);

        const deleteTaskUseCase = new DeleteTaskUseCaseImpl(tasksRepository);

        const runAddTaskFlow = (
            backlogType: BacklogType,
            backlogDay: string
        ) => {
            const viewModelFactory = {
                make: () => {
                    const addNewTaskUseCase = new AddNewTaskUseCaseImpl(
                        tasksRepository
                    );

                    const useHook = () => {
                        return useAddTaskViewModel(
                            backlogType,
                            backlogDay,
                            addNewTaskUseCase
                        );
                    };

                    return useHook;
                },
            };

            setCurrentModal({
                type: "addTask",
                viewModelFactory,
            });
        };

        const useAuthState = () => {
            const [loginState, setLoginState] = React.useState(
                AuthState.PROCESSING
            );

            useEffect(() => {
                const subscripbtion = listenAuthStateUseCase.listen(
                    (authState) => {
                        setLoginState(authState);
                    }
                );

                startNewSessionUseCase.startNewSession();
                return () => {
                    subscripbtion.cancel();
                };
            }, []);

            return loginState;
        };

        const viewModelFactories: ViewModelFactories = {
            logInPage: {
                make: () => {
                    const logInUseCase =
                        new LogInByUserNameAndPasswordUseCaseImpl(
                            authService,
                            localTokensRepository,
                            authStateRepository
                        );

                    const useHook = () => {
                        return useLoginPageViewModel(logInUseCase);
                    };

                    return useHook;
                },
            },
            backlogPage: {
                make: () => {
                    const useHook = () => {
                        const params = useParams();
                        const backlogType = params.backlogType;

                        const onClickAddTaskFlow = useCallback(() => {
                            runAddTaskFlow(
                                backlogType as BacklogType,
                                new Date().toISOString().split("T")[0]
                            );
                        }, [backlogType]);

                        return useCurrentBacklogPageViewModelImpl(
                            onClickAddTaskFlow
                        );
                    };

                    return useHook;
                },
            },
            currentDayBacklogPage: {
                make: () => {
                    const listCurrentDayTasksUseCase =
                        new ListTasksInBacklogUseCaseImpl(
                            BacklogType.Day,
                            tasksRepository
                        );
                    const useHook = () => {
                        const backlogDay = new Date()
                            .toISOString()
                            .split("T")[0];
                        return useDayPageViewModel(
                            backlogDay,
                            listCurrentDayTasksUseCase,
                            startTaskUseCase,
                            stopTaskUseCase,
                            deleteTaskUseCase
                        );
                    };

                    return useHook;
                },
            },
        };

        return {
            useAuthState,
            viewModelFactories,
        };
    }, []);

    const authState = useAuthState();

    return {
        currentModal,
        authState,
        viewModelFactories,
    };
};

export default useAppModel;
