package Exercicios.Java.Circuito;

import java.util.ArrayList;

public class Serie extends Circuit {
    private ArrayList<Circuit> circuits;

    public Serie() {
        circuits = new ArrayList<>();
    }

    public void addCircuit(Circuit c) {
        circuits.add(c);
    }

    public double getResistancia() {
        double total = 0.0;
        for (Circuit c : circuits) {
            total += c.getResistancia();
        }
        return total;
    }

    public String toString() {
        return "Serie " + circuits.toString();
    }
}
