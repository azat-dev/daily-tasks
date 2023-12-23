import Subject from "./Subject";
import SubjectImpl from "./SubjectImpl";

const value = <T>(initialValue: T): Subject<T> => {
    return new SubjectImpl(initialValue);
};

export default value;
