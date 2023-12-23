import { ISubjectCallback } from "./ISubjectCallback";
import { ICancellable } from "./ICancellable";

export default interface ISubject<Value> {
    value: Value;
    set(newValue: Value): void;
    listen(callback: ISubjectCallback<Value>): ICancellable;
}
