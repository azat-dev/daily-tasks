import { ICancellable } from "./ICancellable";
import { ISubjectCallback } from "./ISubjectCallback";

import ISubject from "./ISubject";

export default class Subject<Value> implements ISubject<Value> {
    private currentValue: Value;
    private subscribers: Set<ISubjectCallback<Value>> = new Set();

    constructor(initialValue: Value) {
        this.currentValue = initialValue;
    }

    get value(): Value {
        return this.currentValue;
    }

    public set = (newValue: Value): void => {
        if (this.currentValue === newValue) {
            return;
        }

        this.currentValue = newValue;
        this.notifySubscribers();
    };

    public listen = (callback: ISubjectCallback<Value>): ICancellable => {
        this.subscribers.add(callback);
        return {
            cancel: () => {
                this.subscribers.delete(callback);
            },
        };
    };

    private notifySubscribers = (): void => {
        const value = this.currentValue;
        this.subscribers.forEach((subscriber) => {
            subscriber(value);
        });
    };
}

export const value = <T>(initialValue: T): ISubject<T> => {
    return new Subject(initialValue);
};
