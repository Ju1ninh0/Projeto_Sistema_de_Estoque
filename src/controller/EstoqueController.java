package controller;

import exceptions.*;
import interfaces.IEstoqueService;
import java.util.*;
import java.util.stream.Collectors;
import model.Produto;
import util.StorageService;

public class EstoqueController implements IEstoqueService {
    private final List<Produto> produtos;

    public EstoqueController() {
        this.produtos = StorageService.carregar();
    }

    public void adicionarProduto(Produto produto) {
        if (produto == null)
            throw new EstoqueException("Produto nulo.");
        if (produto.getPreco() < 0)
            throw new PrecoInvalidoException(produto.getPreco());
        if (produto.getQuantidade() < 0)
            throw new QuantidadeInvalidaException(produto.getQuantidade());
        if (buscarPorNome(produto.getNome()) != null)
            throw new ProdutoJaExisteException(produto.getNome());
        produtos.add(produto);
        System.out.println("[LOG] Adicionado: " + produto);
        StorageService.salvar(produtos);
    }

    public void removerProduto(String nome) {
        Produto p = buscarPorNome(nome);
        if (p == null)
            throw new ProdutoNaoEncontradoException(nome);
        produtos.remove(p);
        System.out.println("[LOG] Removido: " + p);
        StorageService.salvar(produtos);
    }

    public void atualizarQuantidade(String nome, int novaQuantidade) {
        if (novaQuantidade < 0)
            throw new QuantidadeInvalidaException(novaQuantidade);
        Produto p = buscarPorNome(nome);
        if (p == null)
            throw new ProdutoNaoEncontradoException(nome);
        p.setQuantidade(novaQuantidade);
        System.out.println("[LOG] Quantidade atualizada: " + p.getNome() + " -> " + novaQuantidade);
        StorageService.salvar(produtos);
    }

    public Produto buscarPorNome(String nome) {
        if (nome == null)
            return null;
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome))
                return p;
        }
        return null;
    }

    public List<Produto> listarProdutos() {
        return produtos.stream()
                .sorted(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

public
