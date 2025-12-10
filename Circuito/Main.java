package Exercicios.Java.Circuito;

public class Main {
    public static void main(String[] args) {
        Serie serie = new Serie();
        serie.addCircuit(new Resistencia(300));
        serie.addCircuit(new Resistencia(500));
        serie.addCircuit(new Resistencia(1200));
        System.out.println("Circuito em série: " + serie);
        System.out.println("Resistência equivalente: " + serie.getResistancia() + "\n");

        Paralelo paralelo = new Paralelo();
        paralelo.addCircuit(new Resistencia(50));
        paralelo.addCircuit(new Resistencia(100));
        paralelo.addCircuit(new Resistencia(300));
        System.out.println("Circuito em paralelo: " + paralelo);
        System.out.println("Resistência equivalente: " + paralelo.getResistancia() + "\n");

        Paralelo complexo = new Paralelo();
        complexo.addCircuit(new Resistencia(100));

        Serie subSerie = new Serie();
        subSerie.addCircuit(new Resistencia(200));
        subSerie.addCircuit(new Resistencia(300));

        complexo.addCircuit(subSerie);

        System.out.println("Circuito complexo: " + complexo);
        System.out.println("Resistência equivalente: " + complexo.getResistancia() + "\n");
    }
}
