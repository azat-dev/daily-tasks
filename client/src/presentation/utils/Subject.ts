import { SubjectCallback } from "./SubjectCallback";
import { Cancellable } from "./Cancellable";

export default interface Subject<Value> {
    value: Value;
    set(newValue: Value): void;
    listen(callback: SubjectCallback<Value>): Cancellable;
}
