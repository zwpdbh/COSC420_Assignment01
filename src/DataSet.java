import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataSet {
    private ArrayList<ArrayList<Integer>> inputRecords = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> teachRecords = new ArrayList<>();

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
                ArrayList<Integer> eachRecord = new ArrayList<>();
                for (String s: values) {
                    eachRecord.add(Integer.parseInt(s));
                }
                inputRecords.add(eachRecord);
            }

            String teachLine;
            while ((teachLine = teachFileBufferReader.readLine()) != null) {
                String[] values = teachLine.split("\\s+");
                ArrayList<Integer> eachRecord = new ArrayList<>();
                for (String s: values) {
                    eachRecord.add(Integer.parseInt(s));
                }
                teachRecords.add(eachRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputRecords.size() != teachRecords.size()) {
            System.err.println("The records in in.txt and the records in teach.txt must be same\n");
            System.exit(1);
        }
    }

    public ArrayList<ArrayList<Integer>> getInputRecords() {
        return inputRecords;
    }

    public void setInputRecords(ArrayList<ArrayList<Integer>> inputRecords) {
        this.inputRecords = inputRecords;
    }

    public ArrayList<ArrayList<Integer>> getTeachRecords() {
        return teachRecords;
    }

    public void setTeachRecords(ArrayList<ArrayList<Integer>> teachRecords) {
        this.teachRecords = teachRecords;
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
            s.append(teachRecords.get(index - 1));
            s.append("\n");
        }
        return s.toString();
    }
}
