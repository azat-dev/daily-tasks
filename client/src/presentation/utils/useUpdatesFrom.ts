import { useEffect, useReducer } from "react";
import Subject from "./Subject";

const useUpdatesFrom = (...dependencies: Subject<any | null>[]) => {
    const [, forceUpdate] = useReducer((x) => x + 1, 0);

    useEffect(() => {
        const listener = () => forceUpdate();
        const subscriptions = dependencies.map((item) => item.listen(listener));

        return () => {
            subscriptions.forEach((s) => s.cancel());
        };
    }, dependencies);
};

export default useUpdatesFrom;
