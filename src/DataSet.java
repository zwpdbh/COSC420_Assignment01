import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DataSet {
    private ArrayList<ArrayList<Double>> inputRecords = new ArrayList<>();
    private ArrayList<ArrayList<Double>> teachingInputRecords = new ArrayList<>();

    public DataSet(String inFileName, String teachFileName) {
        try {
            File infile = new File(inFileName);
            File teachFile = new File(teachFileName);
            FileReader inFileReader = new FileReader(infile);
            FileReader teachFileReader = new FileReader(teachFile);
            BufferedReader inFileBufferReader = new BufferedReader(inFileReader);
            BufferedReader teachFileBufferReader = new BufferedReader(teachFileReader);

            String inputLine;
            while ((inputLine = inFileBufferReader.readLine()) != null ) {
                String[] values = inputLine.split("\\s+");
                ArrayList<Double> eachRecord = new ArrayList<>();
                for (String s: values) {
                    eachRecord.add(Double.parseDouble(s));
                }
                inputRecords.add(eachRecord);
            }

            String teachLine;
            while ((teachLine = teachFileBufferReader.readLine()) != null) {
                String[] values = teachLine.split("\\s+");
                ArrayList<Double> eachRecord = new ArrayList<>();
                for (String s: values) {
                    eachRecord.add(Double.parseDouble(s));
                }
                teachingInputRecords.add(eachRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputRecords.size() != teachingInputRecords.size()) {
            System.err.println("The records in testIn.txt and the records in testTeach.txt must be same\n");
            System.exit(1);
        }
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(inputRecords, new Random(seed));
        Collections.shuffle(teachingInputRecords, new Random(seed));
    }

    public ArrayList<ArrayList<Double>> getInputRecords() {
        return inputRecords;
    }

    public void setInputRecords(ArrayList<ArrayList<Double>> inputRecords) {
        this.inputRecords = inputRecords;
    }

    public ArrayList<ArrayList<Double>> getTeachingInputRecords() {
        return teachingInputRecords;
    }

    public void setTeachingInputRecords(ArrayList<ArrayList<Double>> teachingInputRecords) {
        this.teachingInputRecords = teachingInputRecords;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\nIndex");
        s.append(" | ");
        s.append("Input");
        s.append(" | ");
        s.append("Teach");
        s.append("\n");
        for (int index = 1; index <= inputRecords.size(); index++) {
            s.append(index);
            s.append(" | ");
            s.append(inputRecords.get(index - 1));
            s.append(" | ");
            s.append(teachingInputRecords.get(index - 1));
            s.append("\n");
        }
        return s.toString();
    }
}
