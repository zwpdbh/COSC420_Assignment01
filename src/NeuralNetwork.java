import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {

    public class Neuron {
        // level indicate the level of this neuron in which layer of the neuron network
        private int level;

        // index indicate the index of this neuron in current layer
        private int index;
        private double state = 0.0;

        private Neuron(int level, int index) {
            this.level = level;
            this.index = index;
        }

        public void setState(double state) {
            this.state = state;
        }

        public double getState() {
            return state;
        }
    }

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

    private ArrayList<Neuron> inputLayer = new ArrayList<>();
    private ArrayList<Neuron> hiddenLayer = new ArrayList<>();
    private ArrayList<Neuron> outputLayer = new ArrayList<>();


    public void computeForward(DataSet dataSet, int populationIndex) {
        ArrayList<Double> currentInput = dataSet.getInputRecords().get(populationIndex);

        // the first layer's state = input
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setState(currentInput.get(i));
        }


        // the second/third layer's input = (sum (weight * output of previous layer + bias)) go through activation function
        for (int i = 0; i < hiddenLayer.size(); i++) {
            double sum = 0.0;

            for (int j = 0; j < inputLayer.size(); j++) {
                sum += inputLayer.get(j).getState() * firstLayerWeights[i][j];
            }
            sum += + biasesForHiddenLayer[i];

            hiddenLayer.get(i).setState(applyHiddenLayerActivationFunction(sum));
        }

        for (int i = 0; i < outputLayer.size(); i++) {
            double sum = 0;

            for (int j = 0; j < hiddenLayer.size(); j++) {
                sum += hiddenLayer.get(j).getState() * secondLayerWeights[i][j];
            }
            sum += biasesForOutputLayer[i];

            outputLayer.get(i).setState(applyOutputLayerActivationFunction(sum));
        }

    }


    public void computeBackPropagation(double[] errorsForEachPattern) {
        // Back propagation of secondLayerWeights
        double[][] tempSecondLayerWeights = new double[this.numOfOutPut][this.numOfHidden];

        ArrayList<Double> deltaPKs = new ArrayList<>();
        for (int k = 0; k < numOfOutPut; k++) {
            Double oPK = outputLayer.get(k).getState();
            Double deltaPk = errorsForEachPattern[k] * oPK * (1 - oPK);
            deltaPKs.add(deltaPk);
            for (int j = 0; j < numOfHidden; j++) {
                tempSecondLayerWeights[k][j] = secondLayerWeights[k][j] + (this.learningRate * deltaPk * this.hiddenLayer.get(j).getState());
            }
            this.biasesForOutputLayer[k] += (this.learningRate * deltaPk * 1.0);
        }

        for (int j = 0; j < numOfHidden; j++) {
            Double oPJ = hiddenLayer.get(j).getState();
            double sumFromOutPutLayer = 0.0;
            for (int k = 0; k < numOfOutPut; k++) {
                sumFromOutPutLayer += deltaPKs.get(k) * secondLayerWeights[k][j];
            }
            Double deltaPJ = oPJ * (1 - oPJ) * sumFromOutPutLayer;
            for (int i = 0; i < numOfInput; i++) {
                firstLayerWeights[j][i] += (this.learningRate * deltaPJ * this.inputLayer.get(i).getState());
            }
            this.biasesForHiddenLayer[j] += (this.learningRate * deltaPJ * 1.0);
        }

        // set the weights
        for (int j = 0; j < this.numOfOutPut; j++) {
            for (int i = 0; i < this.numOfHidden; i++) {
                this.secondLayerWeights[j][i] = tempSecondLayerWeights[j][i];
            }
        }

    }

    // using sigmoid function
    private double applyHiddenLayerActivationFunction(double sum) {
        return 1 / (1 + Math.exp(-(sum)));
    }

    // using threshold function
    private double applyOutputLayerActivationFunction(double sum) {
        return 1 / (1 + Math.exp(-(sum)));
    }


    public void checkCurrentWeight() {
        for (int k = 0; k < numOfOutPut; k++) {
            for (int j = 0; j < numOfHidden; j++) {
                System.out.println(secondLayerWeights[k][j]);
            }
        }

        for (int j = 0; j < numOfHidden; j++) {
            for (int i = 0; i < numOfInput; i++) {
                System.out.println(firstLayerWeights[j][i]);
            }
        }
    }


    private void resetWeights() {
        Random random = new Random();
        for (int k = 0; k < numOfOutPut; k++) {
            biasesForOutputLayer[k] = ((double) random.nextInt(21) + 1) / 200.0;
            for (int j = 0; j < numOfHidden; j++) {
                secondLayerWeights[k][j] = ((double) random.nextInt(21) + 1) / 200.0;
            }
        }

        for (int j = 0; j < numOfHidden; j++) {
            biasesForHiddenLayer[j] = ((double) random.nextInt(21) + 1) / 200.0;
            for (int i = 0; i < numOfInput; i++) {
                firstLayerWeights[j][i] = ((double) random.nextInt(21) + 1) / 200.0;
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

    /**Train the neural network in batch mode*/
    public void train(DataSet dataSet) {
//        resetWeights();
        setWeightsForTest();
        checkCurrentWeight();

        // until population error < criterion
        do {
            double populationError = 0.0;
            double sum = 0.0;
            double[] errorsForOutputNeurons = new double[numOfOutPut];
            for (int k = 0; k < numOfOutPut; k++) {
                errorsForOutputNeurons[k] = 0.0;
            }

            // for each pattern in the batch
            for (int index = 0; index < dataSet.getInputRecords().size(); index++) {
                this.computeForward(dataSet, index);

                // for each output neuron
                for (int k = 0; k < numOfOutPut; k++) {
                    Double errorOfEachOutput = this.outputLayer.get(k).getState() - dataSet.getTeachRecords().get(index).get(k);
                    sum += (errorOfEachOutput * errorOfEachOutput);
                    errorsForOutputNeurons[k] += errorOfEachOutput;
                }

            }
            this.computeBackPropagation(errorsForOutputNeurons);

            // compute population error;
            populationError = sum / (numOfOutPut * dataSet.getTeachRecords().size());

            epoch++;

            if (epoch % 100 == 0) {
                System.out.println("Current epoches: " + epoch + " population error = " + populationError);
            } else {
                System.out.println("Current epoches: " + epoch + " population error = " + populationError + "\n");
            }

            if (populationError < criterion) {
                break;
            }
        } while ( epoch <= 10000);
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

    public NeuralNetwork(String paraFileName) {
        processParamFile(paraFileName);

        // be careful about the convention of index: W_i_j is a weight on
        // a link connecting from neuron j to current neuron i.
        this.secondLayerWeights = new double[this.numOfOutPut][this.numOfHidden];
        this.firstLayerWeights = new double[this.numOfHidden][this.numOfInput];

        this.biasesForHiddenLayer = new double[this.getNumOfHidden()];
        this.biasesForOutputLayer = new double[this.getNumOfOutPut()];

        for (int i = 0; i < this.getNumOfInput(); i++) {
            Neuron neuron = new Neuron(0, i);
            this.inputLayer.add(neuron);
        }

        for (int i = 0; i < this.getNumOfHidden(); i++){
            Neuron neuron = new Neuron(1, i);
            this.hiddenLayer.add(neuron);
        }

        for (int i = 0; i < this.getNumOfOutPut(); i++) {
            Neuron neuron = new Neuron(2, i);
            this.outputLayer.add(neuron);
        }
    }

    public int getNumOfInput() {
        return numOfInput;
    }

    public void setNumOfInput(int numOfInput) {
        this.numOfInput = numOfInput;
    }

    public int getNumOfHidden() {
        return numOfHidden;
    }

    public void setNumOfHidden(int numOfHidden) {
        this.numOfHidden = numOfHidden;
    }

    public int getNumOfOutPut() {
        return numOfOutPut;
    }

    public void setNumOfOutPut(int numOfOutPut) {
        this.numOfOutPut = numOfOutPut;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getCriterion() {
        return criterion;
    }

    public void setCriterion(double criterion) {
        this.criterion = criterion;
    }


    public double[][] getFirstLayerWeights() {
        return firstLayerWeights;
    }

    public void setFirstLayerWeights(double[][] firstLayerWeights) {
        this.firstLayerWeights = firstLayerWeights;
    }

    public double[][] getSecondLayerWeights() {
        return secondLayerWeights;
    }

    public void setSecondLayerWeights(double[][] secondLayerWeights) {
        this.secondLayerWeights = secondLayerWeights;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("NeuralNetwork:\n");
        s.append("numOfInput = ");
        s.append(numOfInput);
        s.append("\nnumOfHidden = ");
        s.append(numOfHidden);
        s.append("\nnumOfOutput = ");
        s.append(numOfOutPut);
        s.append("\nlearningRate = ");
        s.append(learningRate);
        s.append("\nmomentum = ");
        s.append(momentum);
        s.append("\ncriterion = ");
        s.append(criterion);


//        for (Neuron neuron: inputLayer) {
//            System.out.println(neuron.getState());
//        }
//
//        for (Neuron neuron: hiddenLayer) {
//            System.out.println(neuron.getState());
//        }
//
//        for (Neuron neuron: outputLayer) {
//            System.out.println(neuron.getState());
//        }

        return s.toString();
    }

}
