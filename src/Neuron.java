import java.util.ArrayList;
import java.util.Random;

public class Neuron {
    private int level;
    private int index;
    private double input;

    public Neuron(int level, int index) {
        this.level = level;
        this.index = index;

        Random rand = new Random();
        this.input = rand.nextDouble();
    }


    public double getOutput() {

        return  0;
    }
}
