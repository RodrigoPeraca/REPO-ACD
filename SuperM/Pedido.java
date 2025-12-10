package Exercicios.Java.SuperM;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Cliente cliente;
    private List<ItemPedido> itens;
    private boolean pago;
    private FormaPagamento formaPagamento;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.pago = false;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (quantidade <= produto.getQuantidadeEstoque()) {
            itens.add(new ItemPedido(produto, quantidade));
            produto.reduzirEstoque(quantidade);
        } else {
            System.out.println("Quantidade solicitada maior que o estoque disponível.");
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void realizarPagamento(FormaPagamento forma) {
        if (!pago) {
            this.formaPagamento = forma;
            this.pago = true;
            System.out.println("Pagamento realizado com sucesso!");
            System.out.println("Forma de pagamento: " + forma);
            System.out.println("Total pago: R$ " + calcularTotal());
        } else {
            System.out.println("O pedido já foi pago.");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cliente: ").append(cliente).append("\n");
        sb.append("Itens:\n");
        for (ItemPedido item : itens) {
            sb.append("  - ").append(item).append("\n");
        }
        sb.append("Total: R$ ").append(calcularTotal()).append("\n");
        return sb.toString();
    }
}
