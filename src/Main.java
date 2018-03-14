import java.io.*;

public class Main {

    public static void main(String[] args) {
        String paramFile = "/Users/zw/code/Java_Projects/COSC420_Assignment01/src/param.txt";

        NeuralNetwork neuralNetwork =
                new NeuralNetwork(paramFile);

        System.out.println(neuralNetwork);
    }


    private static void process(String filename) {
        try {
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // process the line
                String[] strings = line.split("\\s+");
                for (int i = 0; i < strings.length; i++) {
                    System.out.print(strings[i] + " ");
                }
                System.out.println();
            }
            fileReader.close();
//            System.out.println("Contents of file:");
//            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
