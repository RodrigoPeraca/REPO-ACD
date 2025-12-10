package Exercicios.Java.SuperM;
public class Produto {
    private String descricao;
    private double preco;
    private int quantidadeEstoque;

    public Produto(String descricao, double preco, int quantidadeEstoque) {
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void reduzirEstoque(int qtd) {
        if (qtd <= quantidadeEstoque) {
            quantidadeEstoque -= qtd;
        } else {
            System.out.println("Estoque insuficiente para " + descricao);
        }
    }

    public String toString() {
        return descricao + " - R$ " + preco + " (Estoque: " + quantidadeEstoque + ")";
    }
}
