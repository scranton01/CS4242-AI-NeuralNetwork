import java.util.List;

public class Main {
    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(64, 43, .01);
        List<List<Double>> data = Utility.readCsv("optdigits_train.txt");
        List<List<Double>> testData = Utility.readCsv("optdigits_test.txt");
        System.out.println("learning");
        int iteration = 0;
        while (true) {
            for (int i = 0; i < data.size(); i++) {
                double answer = data.get(i).get(data.get(i).size() - 1);
                neuralNetwork.setInputOutput(data.get(i), answer);
                neuralNetwork.backPropLearning();
            }
            if (iteration % 1 == 0 && iteration != 0) {
                int count = 0;
                for (int i = 0; i < testData.size(); i++) {
                    double answer = testData.get(i).get(testData.get(i).size() - 1);
                    neuralNetwork.setInputOutput(testData.get(i), answer);
                    if (neuralNetwork.checkSolution()) {
                        count++;
                    }
                }
                double accuracy = (double) count / (double) testData.size() * 100;
                System.out.println(String.format("iteration: %d accuracy: %f", iteration, accuracy) + "%");
            }
            iteration++;
        }
    }
}
