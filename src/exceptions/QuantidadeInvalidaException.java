package exceptions;

public class QuantidadeInvalidaException extends EstoqueException {
    public QuantidadeInvalidaException(int qtd) {
        super("Quantidade inválida: " + qtd);
    }
}
