package Exercicios.Java.SuperM;
public class ItemPedido {
    private Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }

    public String toString() {
        return produto.getDescricao() + " x" + quantidade + " = R$ " + getSubtotal();
    }
}
