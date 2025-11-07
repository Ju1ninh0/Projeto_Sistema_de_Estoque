package model;

import java.time.LocalDate;

public class Perecivel extends Produto {
    private LocalDate validade;

    public Perecivel(String nome, int quantidade, double preco, LocalDate validade) {
        super(nome, quantidade, preco);
        this.validade = validade;
    }

    public LocalDate getValidade() { return validade; }

    public String getCategoria() { return "Perecível"; }

    public double precoComImposto(double aliquota) {
        double reduzida = Math.max(0, aliquota - 0.02);
        return getPreco() * (1.0 + reduzida);
    }
}
