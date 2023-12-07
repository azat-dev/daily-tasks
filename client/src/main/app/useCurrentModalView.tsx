import React, { useMemo } from "react";
import { CurrentModalState } from "./useAppModel";
import AddTaskModalView from "../../presentation/modals/AddTaskModal/AddTaskModalView";
import useViewModelHookFromFactory from "./useViewModelHookFromFactory";

const useCurrentModalView = (
    currentModalState: CurrentModalState
): React.FC | null => {
    return useMemo(() => {
        if (!currentModalState) {
            return null;
        }

        switch (currentModalState.type) {
            case "addTask": {
                const CurrentModal = () => {
                    const viewModel = useViewModelHookFromFactory(
                        currentModalState.viewModelFactory
                    );
                    return <AddTaskModalView viewModel={viewModel} />;
                };

                return CurrentModal;
            }
        }
    }, [currentModalState]);
};

export default useCurrentModalView;
