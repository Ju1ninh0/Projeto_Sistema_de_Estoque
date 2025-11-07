package interfaces;

import model.Produto;
import java.util.List;

public interface IEstoqueService {
    void adicionarProduto(Produto produto);
    void removerProduto(String nome);
    void atualizarQuantidade(String nome, int novaQuantidade);
    Produto buscarPorNome(String nome);
    List<Produto> listarProdutos();
    int quantidadeTotalItens();
    double somaPrecos();
}
