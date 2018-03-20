public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Please provide param, input and teaching input file + epoches");
            System.exit(1);
        }

        String paramFile = args[0];
        String inFile = args[1];
        String teachFile = args[2];
        int epoches = 200000;

        NeuralNetwork neuralNetwork = new NeuralNetwork(paramFile, epoches);
        DataSet dataSet = new DataSet(inFile, teachFile);

        System.out.println(neuralNetwork);
        System.out.println(dataSet);

        neuralNetwork.train(dataSet);
    }
}
