import java.util.List;

public class Main {
    public static void main (String[] args){
        System.out.println("hello world");
//        Utility.readCsv("optdigits_train.txt");
        List<List<Integer>> data = Utility.readCsv("optdigits_train.txt");

        NeuralNetwork neuralNetwork = new NeuralNetwork(64, 43, 1, .1);
        int answer = data.get(0).get(data.get(0).size()-1);
        neuralNetwork.forward(data.get(0));

        System.out.println("hey");
    }
}
