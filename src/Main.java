import java.io.*;

public class Main {

    public static void main(String[] args) {
        String paramFile = "/Users/zw/code/Java_Projects/COSC420_Assignment01/src/param.txt";
        String inFile = "/Users/zw/code/Java_Projects/COSC420_Assignment01/src/in.txt";
        String teachFile = "/Users/zw/code/Java_Projects/COSC420_Assignment01/src/teach.txt";


        NeuralNetwork neuralNetwork =
                new NeuralNetwork(paramFile);
        System.out.println(neuralNetwork);

        DataSet dataSet = new DataSet(inFile, teachFile);
        System.out.println(dataSet);

    }
}
