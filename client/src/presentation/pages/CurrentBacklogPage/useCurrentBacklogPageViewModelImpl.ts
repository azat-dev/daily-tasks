import { useMemo, useState } from "react";
import CurrentBacklogPageViewModel from "./CurrentBacklogPageViewModel";

const useCurrentBacklogPageViewModelImpl = (
    runAddTaskFlow: () => void
): CurrentBacklogPageViewModel => {
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const onClickAddTask = (e: any) => {
        e.preventDefault();
        e.stopPropagation();
        runAddTaskFlow();
    };

    return {
        isLoading,
        tasks: [],
        onClickAddTask,
    };
};

export default useCurrentBacklogPageViewModelImpl;
