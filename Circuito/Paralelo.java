package Exercicios.Java.Circuito;

import java.util.ArrayList;

public class Paralelo extends Circuit {
    private ArrayList<Circuit> circuits;

    public Paralelo() {
        circuits = new ArrayList<>();
    }

    public void addCircuit(Circuit c) {
        circuits.add(c);
    }

    public double getResistancia() {
        double sum = 0.0;
        for (Circuit c : circuits) {
            sum += 1.0 / c.getResistancia();
        }
        return 1.0 / sum;
    }

    public String toString() {
        return "Paralelo " + circuits.toString();
    }
}
