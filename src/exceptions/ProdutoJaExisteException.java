package exceptions;

public class ProdutoJaExisteException extends EstoqueException {
    public ProdutoJaExisteException(String nome) {
        super("Já existe um produto com esse nome: " + nome);
    }
}
