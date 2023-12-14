import { Cancellable } from "./Cancellable";
import { SubjectCallback } from "./SubjectCallback";

import Value from "./Value";

export default class ValueImpl<T> implements Value<T> {
    private currentValue: T;
    private subscribers: Set<SubjectCallback<T>> = new Set();

    constructor(initialValue: T) {
        this.currentValue = initialValue;
    }

    get value(): T {
        return this.currentValue;
    }

    set = (newValue: T): void => {
        this.currentValue = newValue;
        this.notifySubscribers();
    };

    listen(callback: SubjectCallback<T>): Cancellable {
        this.subscribers.add(callback);
        return {
            cancel: () => {
                this.subscribers.delete(callback);
            },
        };
    }

    private notifySubscribers(): void {
        const value = this.currentValue;
        this.subscribers.forEach((subscriber) => {
            subscriber(value);
        });
    }
}

export const value = <T>(initialValue: T): Value<T> => {
    return new ValueImpl(initialValue);
};
