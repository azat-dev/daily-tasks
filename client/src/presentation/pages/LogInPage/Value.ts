import { SubjectCallback } from "./SubjectCallback";
import { Cancellable } from "./Cancellable";

export default interface Value<T> {
    value: T;
    set(newValue: T): void;
    listen(callback: SubjectCallback<T>): Cancellable;
}
