import java.util.List;

public class Main {
    public static void main (String[] args){
        System.out.println("hello world");
//        Utility.readCsv("optdigits_train.txt");
        List<List<Double>> data = Utility.readCsv("optdigits_train.txt");

        NeuralNetwork neuralNetwork = new NeuralNetwork(64, 43, 1, .1);
        double answer = data.get(5).get(data.get(0).size()-1);
        neuralNetwork.setInputOutput(data.get(5),answer);
        neuralNetwork.forward();


        System.out.println("hey");
    }
}
