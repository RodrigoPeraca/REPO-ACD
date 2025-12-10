package Exercicios.Java.Circuito;

public class Resistencia extends Circuit {
    private double resistencia;

    public Resistencia(double resistencia) {
        this.resistencia = resistencia;
    }

    public double getResistancia() {
        return resistencia;
    }

    public String toString() {
        return resistencia + " ohms";
    }
}
