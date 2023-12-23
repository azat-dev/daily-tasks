import { Cancellable } from "./Cancellable";
import { SubjectCallback } from "./SubjectCallback";
import Subject from "./Subject";

export default class SubjectImpl<Value> implements Subject<Value> {
    private currentValue: Value;
    private subscribers: Set<SubjectCallback<Value>> = new Set();

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

    public listen = (callback: SubjectCallback<Value>): Cancellable => {
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
