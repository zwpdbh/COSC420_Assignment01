import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {

    private int numOfInput;
    private int numOfHidden;
    private int numOfOutPut;
    private double learningRate;
    private double momentum;
    private double criterion;

    private int epoch = 0;

    private double[][] firstLayerWeights;
    private double[][] secondLayerWeights;

    private double[] biasesForHiddenLayer;
    private double[] biasesForOutputLayer;

    private ArrayList<Double> inputLayer = new ArrayList<>();
    private ArrayList<Double> hiddenLayer = new ArrayList<>();
    private ArrayList<Double> outputLayer = new ArrayList<>();


    public void computeForward(ArrayList<Double> inputs) {

        // the first layer's state = input
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.set(i, inputs.get(i));
        }


        // the second/third layer's input = (sum (weight * output of previous layer + bias)) go through activation function
        for (int j = 0; j < hiddenLayer.size(); j++) {
            double sum = 0.0;
            for (int i = 0; i < inputLayer.size(); i++) {
                sum += inputLayer.get(i) * firstLayerWeights[j][i];
            }
            sum += biasesForHiddenLayer[j];

            hiddenLayer.set(j, applyHiddenLayerActivationFunction(sum));
        }


        for (int k = 0; k < outputLayer.size(); k++) {
            double sum = 0.0;
            for (int j = 0; j < hiddenLayer.size(); j++) {
                sum += hiddenLayer.get(j) * secondLayerWeights[k][j];
            }

            sum += biasesForOutputLayer[k];

            outputLayer.set(k, applyOutputLayerActivationFunction(sum));
        }

    }

    /**This method is used to do back propagation for a given array of differences between output and teaching input*/
    private void computeBackPropagation(double[] errorsForEachPattern) {
//        System.out.println("Back-propagation error = " + errorsForEachPattern[0]);
        // Back propagation of secondLayerWeights
        double[][] savedAdjustedSecondLayerWeights = new double[this.numOfOutPut][this.numOfHidden];


        // for the second layer weights
        ArrayList<Double> deltaPKs = new ArrayList<>();
        for (int k = 0; k < outputLayer.size(); k++) {
            deltaPKs.add(0.0);
        }
        for (int k = 0; k < outputLayer.size(); k++) {
            Double oPK = outputLayer.get(k);
            Double deltaPk = errorsForEachPattern[k] * oPK * (1.0 - oPK);
            deltaPKs.set(k, deltaPk);

            for (int j = 0; j < numOfHidden; j++) {
                savedAdjustedSecondLayerWeights[k][j] = secondLayerWeights[k][j] + (this.learningRate * deltaPk * this.hiddenLayer.get(j));
            }
            biasesForOutputLayer[k] += (this.learningRate * deltaPk * 1.0);
        }


        // for the first layer weights

        for (int j = 0; j < numOfHidden; j++) {
            double sumFromOutPutLayer = 0.0;
            Double oPJ = hiddenLayer.get(j);

            for (int k = 0; k < numOfOutPut; k++) {
                sumFromOutPutLayer += deltaPKs.get(k) * secondLayerWeights[k][j];
            }

            Double deltaPJ = oPJ * (1 - oPJ) * sumFromOutPutLayer;

            for (int i = 0; i < numOfInput; i++) {
                firstLayerWeights[j][i] += (this.learningRate * deltaPJ * this.inputLayer.get(i));
            }
            this.biasesForHiddenLayer[j] += (this.learningRate * deltaPJ * 1.0);
        }

        // set the weights
        for (int k = 0; k < outputLayer.size(); k++) {
            for (int j = 0; j < hiddenLayer.size(); j++) {
                secondLayerWeights[k][j] = savedAdjustedSecondLayerWeights[k][j];
            }
        }

    }


    /**Train the neural network in batch mode*/
    public void train(DataSet dataSet) {
        if (this.epoch == 1) {
            setWeightsForTest();
        } else {
            resetWeights(0.1, 0.9);
        }


        int trainning_epoch = 1;

        do {
            dataSet.shuffle();
            double populationError = 0.0;
            double sum = 0.0;

            double[] errorsForOutputNeurons = new double[numOfOutPut];
            for (int k = 0; k < numOfOutPut; k++) {
                errorsForOutputNeurons[k] = 0.0;
            }

            // for each pattern in the batch
            for (int index = 0; index < dataSet.getInputRecords().size(); index++) {
                this.computeForward(dataSet.getInputRecords().get(index));

                // for each output neuron
                for (int k = 0; k < numOfOutPut; k++) {
                    Double errOutput = dataSet.getTeachingInputRecords().get(index).get(k) - this.outputLayer.get(k);
                    sum += (errOutput * errOutput);
                    errorsForOutputNeurons[k] = errOutput;
                }

                this.computeBackPropagation(errorsForOutputNeurons);
            }



            // compute population error;
            populationError = sum / (numOfOutPut * dataSet.getTeachingInputRecords().size());

            this.epoch--;
            trainning_epoch++;

            if (trainning_epoch % 100 == 0) {
//                this.learningRate = this.learningRate - (this.learningRate / 100.0);
                System.out.println("Current epoches: " + trainning_epoch + " population error = " + populationError + " , learning_rate = " + this.learningRate);
            }

            if (populationError < criterion) {
                break;
            }
        } while (true);
    }



    // using sigmoid function
    private double applyHiddenLayerActivationFunction(double sum) {
        return 1 / (1 + Math.exp(-(sum)));
    }

    // using threshold function
    private double applyOutputLayerActivationFunction(double sum) {
        return 1 / (1 + Math.exp(-(sum)));
    }


    public void checkCurrentStatus() {
        StringBuilder s = new StringBuilder();
        s.append("|");
        s.append(inputLayer.get(0));
        s.append("|");
        s.append(" == ");
        s.append(firstLayerWeights[0][0]);
        s.append(" ==> ");
        s.append("|");
        s.append(hiddenLayer.get(0));
        s.append("|");
        s.append(" == ");
        s.append(secondLayerWeights[0][0]);
        s.append(" ==> ");
        s.append("|");
        s.append(outputLayer.get(0));
        s.append("|");
        System.out.println(s.toString());

//        for (int k = 0; k < numOfOutPut; k++) {
//            for (int j = 0; j < numOfHidden; j++) {
//                System.out.println(secondLayerWeights[k][j]);
//            }
//        }
//
//        for (int j = 0; j < numOfHidden; j++) {
//            for (int i = 0; i < numOfInput; i++) {
//                System.out.println(firstLayerWeights[j][i]);
//            }
//        }
    }


    private void resetWeights(double min, double max) {
        Random random = new Random();
        for (int k = 0; k < numOfOutPut; k++) {
            biasesForOutputLayer[k] =  min + Math.random() * (max - min);;
            for (int j = 0; j < numOfHidden; j++) {
                secondLayerWeights[k][j] = min + Math.random() * (max - min);;
            }
        }

        for (int j = 0; j < numOfHidden; j++) {
            biasesForHiddenLayer[j] = min + Math.random() * (max - min);;
            for (int i = 0; i < numOfInput; i++) {
                firstLayerWeights[j][i] = min + Math.random() * (max - min);;
            }
        }

    }

    private void setWeightsForTest() {
        biasesForOutputLayer[0] = -0.3;
        biasesForOutputLayer[1] = 0.4;

        secondLayerWeights[0][0] = -0.8;
        secondLayerWeights[0][1] = 0.7;
        secondLayerWeights[1][0] = 0.6;
        secondLayerWeights[1][1] = -0.5;

        biasesForHiddenLayer[0] = 0.2;
        biasesForHiddenLayer[1] = -0.2;

        firstLayerWeights[0][0] = 1.1;
        firstLayerWeights[0][1] = 1.2;
        firstLayerWeights[1][0] = -1.3;
        firstLayerWeights[1][1] = -1.4;
    }


    private void processParamFile(String paraFileName) {
        try {
            File file = new File(paraFileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null && i < 6) {
                switch (i) {
                    case 0:
                        this.numOfInput = Integer.parseInt(line);
                        break;
                    case 1:
                        this.numOfHidden = Integer.parseInt(line);
                        break;
                    case 2:
                        this.numOfOutPut = Integer.parseInt(line);
                        break;
                    case 3:
                        this.learningRate = Double.parseDouble(line);
                        break;
                    case 4:
                        this.momentum = Double.parseDouble(line);
                        break;
                    case 5:
                        this.criterion = Double.parseDouble(line);
                        break;
                    default:
                        System.out.println("This file should only contain 6 lines of setting parameters.\n");
                        System.exit(1);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NeuralNetwork(String paraFileName, int epoches) {
        processParamFile(paraFileName);
        this.epoch = epoches;

        // be careful about the convention of index: W_i_j is a weight on
        // a link connecting from neuron j to current neuron i.
        this.secondLayerWeights = new double[this.numOfOutPut][this.numOfHidden];
        this.firstLayerWeights = new double[this.numOfHidden][this.numOfInput];

        this.biasesForHiddenLayer = new double[this.getNumOfHidden()];
        this.biasesForOutputLayer = new double[this.getNumOfOutPut()];

        for (int i = 0; i < this.getNumOfInput(); i++) {
            this.inputLayer.add(0.0);
        }

        for (int i = 0; i < this.getNumOfHidden(); i++){
            this.hiddenLayer.add(0.0);
        }

        for (int i = 0; i < this.getNumOfOutPut(); i++) {
            this.outputLayer.add(0.0);
        }
    }

    public int getNumOfInput() {
        return numOfInput;
    }


    public int getNumOfHidden() {
        return numOfHidden;
    }


    public int getNumOfOutPut() {
        return numOfOutPut;
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("NeuralNetwork:\n");
        s.append("num of input neurons = ");
        s.append(numOfInput);
        s.append("\nnum of hidden neurons = ");
        s.append(numOfHidden);
        s.append("\nnum of output neurons = ");
        s.append(numOfOutPut);
        s.append("\nlearningRate = ");
        s.append(learningRate);
        s.append("\nmomentum = ");
        s.append(momentum);
        s.append("\ncriterion = ");
        s.append(criterion);

        return s.toString();
    }

}
