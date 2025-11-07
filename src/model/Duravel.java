package model;

public class Duravel extends Produto {
    private int garantiaMeses;

    public Duravel(String nome, int quantidade, double preco, int garantiaMeses) {
        super(nome, quantidade, preco);
        this.garantiaMeses = garantiaMeses;
    }

    public int getGarantiaMeses() { return garantiaMeses; }

    public String getCategoria() { return "Durável"; }
}
