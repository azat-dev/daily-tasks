import { useEffect, useReducer } from "react";
import Value from "./Value";

const useUpdatesFrom = (...dependencies: Value<any | null>[]) => {
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
