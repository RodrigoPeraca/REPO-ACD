package Exercicios.Java.SuperM;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Supermercado {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Criando alguns produtos no estoque
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("ARROZ", 20.0, 10));
        produtos.add(new Produto("FEIJÃO", 8.0, 15));
        produtos.add(new Produto("FARINHA", 5.0, 20));
        produtos.add(new Produto("LEITE", 6.5, 30));

        Pedido pedidoAtual = null;

        int opcao;
        do {
            System.out.println("\n===== MENU SUPERMERCADO =====");
            System.out.println("1) Novo Pedido");
            System.out.println("2) Realizar Pagamento");
            System.out.println("0) Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine(); // consumir quebra de linha

            switch (opcao) {
                case 1:
                    // Criar cliente
                    System.out.print("Nome do cliente: ");
                    String nome = sc.nextLine();
                    System.out.print("CPF do cliente: ");
                    String cpf = sc.nextLine();
                    Cliente cliente = new Cliente(nome, cpf);

                    pedidoAtual = new Pedido(cliente);

                    while (true) {
                        System.out.println("\nProdutos disponíveis:");
                        for (int i = 0; i < produtos.size(); i++) {
                            System.out.println((i + 1) + ") " + produtos.get(i));
                        }
                        System.out.print("Escolha o número do produto (0 para finalizar): ");
                        int escolhaProduto = sc.nextInt();
                        if (escolhaProduto == 0) break;

                        Produto prod = produtos.get(escolhaProduto - 1);
                        System.out.print("Quantidade: ");
                        int qtd = sc.nextInt();

                        pedidoAtual.adicionarItem(prod, qtd);
                    }

                    System.out.println("\nResumo do pedido:");
                    System.out.println(pedidoAtual);
                    break;

                case 2:
                    if (pedidoAtual == null) {
                        System.out.println("Nenhum pedido em aberto.");
                    } else {
                        System.out.println(pedidoAtual);
                        System.out.println("Formas de pagamento: 1) DINHEIRO  2) CHEQUE  3) CARTAO");
                        System.out.print("Escolha a forma de pagamento: ");
                        int forma = sc.nextInt();
                        FormaPagamento pagamento = switch (forma) {
                            case 1 -> FormaPagamento.DINHEIRO;
                            case 2 -> FormaPagamento.CHEQUE;
                            case 3 -> FormaPagamento.CARTAO;
                            default -> null;
                        };
                        if (pagamento != null) {
                            pedidoAtual.realizarPagamento(pagamento);
                        } else {
                            System.out.println("Forma de pagamento inválida.");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        sc.close();
    }
}
