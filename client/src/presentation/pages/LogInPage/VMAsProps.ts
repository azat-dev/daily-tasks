import Value from "./Value";

type VMAsProps<T> = {
    [P in keyof T]: T[P] extends Value<infer X> ? X : T[P];
};

export default VMAsProps;
