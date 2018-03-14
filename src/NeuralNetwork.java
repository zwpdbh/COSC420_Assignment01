import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class NeuralNetwork {
    private int numOfInput;
    private int numOfHidden;
    private int numOfOutPut;
    private double learningRate;
    private double momentum;
    private double criterion;

    private double[][] firstLayerWeights;
    private double[][] secondLayerWeights;

    private class DataSet {
        private DataSet(String inFileName, String teachFileName) {
            try {
                File infile = new File(inFileName);
                File teachFile = new File(teachFileName);
                FileReader inFileReader = new FileReader(infile);
                FileReader teachFileReader = new FileReader(teachFile);
                BufferedReader inFileBufferReader = new BufferedReader(inFileReader);
                BufferedReader teachFileBufferReader = new BufferedReader(teachFileReader);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadDataSet(String inFileName, String teachFileName) {
        DataSet dataSet = new DataSet(inFileName, teachFileName);
    }

    private void processParamFile(String paraFileName) {
        try {
            File file = new File(paraFileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null && i <= 5) {
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
        this.firstLayerWeights = new double[this.numOfInput][this.numOfHidden];
        this.secondLayerWeights =
                new double[this.numOfHidden][this.numOfOutPut];

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

        return s.toString();
    }

}
