import { useMemo } from "react";
import { ViewModelHookFactory } from "./useAppModel";

const useViewModelHookFromFactory = (factory: ViewModelHookFactory<any>) => {
    const useViewModel = useMemo(() => factory.make(), [factory]);
    return useViewModel();
};

export default useViewModelHookFromFactory;
