package interfaces;

public interface IFactoryWithArgument<TOut, TIn> {
    TOut create(TIn in);
}
