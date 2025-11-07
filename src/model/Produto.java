package model;

import interfaces.Tributavel;

public abstract class Produto implements Tributavel {
    private String nome;
    private int quantidade;
    private double preco;

    protected Produto(String nome, int quantidade, double preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public double getPreco() { return preco; }

    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setPreco(double preco) { this.preco = preco; }

    public abstract String getCategoria();

    public double precoComImposto(double aliquota) {
        return preco * (1.0 + aliquota);
    }

    public String toString() {
        return getCategoria() + "{nome='" + nome + "', quantidade=" + quantidade + ", preco=" + preco + "}";
    }
}
