package exceptions;

public class ProdutoNaoEncontradoException extends EstoqueException {
    public ProdutoNaoEncontradoException(String nome) {
        super("Produto não encontrado: " + nome);
    }
}
