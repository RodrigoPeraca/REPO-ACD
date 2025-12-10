package Exercicios.Java.Circuito;

import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("=== Menu de Circuitos ===");
            System.out.println("1 - Criar circuito em serie");
            System.out.println("2 - Criar circuito em paralelo");
            System.out.println("3 - Criar circuito complexo (paralelo com sub-série)");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    Serie serie = new Serie();
                    System.out.print("Quantos resistores na série? ");
                    int nSerie = sc.nextInt();
                    for (int i = 0; i < nSerie; i++) {
                        System.out.print("Valor do resistor " + (i + 1) + " (ohms): ");
                        double valor = sc.nextDouble();
                        serie.addCircuit(new Resistencia(valor));
                    }
                    System.out.println("Circuito em série: " + serie);
                    System.out.println("Resistência equivalente: " + serie.getResistancia() + " ohms\n");
                    break;

                case 2:
                    Paralelo paralelo = new Paralelo();
                    System.out.print("Quantos resistores no paralelo? ");
                    int nParalelo = sc.nextInt();
                    for (int i = 0; i < nParalelo; i++) {
                        System.out.print("Valor do resistor " + (i + 1) + " (ohms): ");
                        double valor = sc.nextDouble();
                        paralelo.addCircuit(new Resistencia(valor));
                    }
                    System.out.println("Circuito em paralelo: " + paralelo);
                    System.out.println("Resistência equivalente: " + paralelo.getResistancia() + " ohms\n");
                    break;

                case 3:
                    Paralelo complexo = new Paralelo();
                    System.out.print("Valor do resistor único do paralelo (ohms): ");
                    complexo.addCircuit(new Resistencia(sc.nextDouble()));

                    Serie subSerie = new Serie();
                    System.out.print("Quantos resistores na sub-série? ");
                    int nSub = sc.nextInt();
                    for (int i = 0; i < nSub; i++) {
                        System.out.print("Valor do resistor " + (i + 1) + " (ohms): ");
                        subSerie.addCircuit(new Resistencia(sc.nextDouble()));
                    }
                    complexo.addCircuit(subSerie);

                    System.out.println("Circuito complexo: " + complexo);
                    System.out.println("Resistência equivalente: " + complexo.getResistancia() + " ohms\n");
                    break;

                case 0:
                    continuar = false;
                    System.out.println("Encerrando o programa...");
                    break;

                default:
                    System.out.println("Opção inválida!\n");
            }
        }

        sc.close();
    }
}
