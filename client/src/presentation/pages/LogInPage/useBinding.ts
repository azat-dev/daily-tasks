import { useCallback, useEffect, useState } from "react";
import { Cancellable } from "./Cancellable";
import VMAsProps from "./VMAsProps";

const isSubject = (subject: any): boolean => subject.set && subject.listen;

const mapToProps = (data: any) => {
    const result: any = {};

    Object.getOwnPropertyNames(data).forEach((key) => {
        const item = data[key];

        if (!isSubject(item)) {
            result[key] = item;
            return;
        }

        result[key] = item.value;
    });

    return result;
};

export const useViewModelBinding = <ViewModelOutput>(
    viewModelOutput: ViewModelOutput
): VMAsProps<ViewModelOutput> => {
    const [state, setState] = useState<VMAsProps<ViewModelOutput>>(() =>
        mapToProps(viewModelOutput)
    );

    useEffect(() => {
        const subscriptions: Cancellable[] = [];

        Object.getOwnPropertyNames(viewModelOutput).forEach((key) => {
            const item = (viewModelOutput as any)[key];
            if (!isSubject(item)) {
                return;
            }

            const subscription = item.listen(() => {
                setState(mapToProps(viewModelOutput));
            });

            subscriptions.push(subscription);
        });

        return () => {
            subscriptions.forEach((s) => s.cancel());
        };
    }, [viewModelOutput]);

    return state;
};
