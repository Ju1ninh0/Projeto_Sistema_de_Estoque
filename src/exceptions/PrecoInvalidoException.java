package exceptions;

public class PrecoInvalidoException extends EstoqueException {
    public PrecoInvalidoException(double preco) {
        super("Preço inválido: " + preco);
    }
}
