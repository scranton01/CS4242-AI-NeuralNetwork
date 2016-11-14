import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class NeuralNetwork {
    int inputLayerSize;
    int hiddenLayerSize;
    int outputLayerSize;
    double learningRate;
    List<Node> inputLayer;
    List<Node> hiddenLayer;
    List<Node> outputLayer;


    NeuralNetwork(int input, int hidden, int output, double learningRate) {
        this.inputLayerSize = input;
        this.hiddenLayerSize = hidden;
        this.outputLayerSize = output;
        this.learningRate = learningRate;
        this.inputLayer = new ArrayList<>();
        this.hiddenLayer = new ArrayList<>();
        this.outputLayer = new ArrayList<>();
        for (int i = 0; i < inputLayerSize; i++) {
            inputLayer.add(new Node());
        }
        for (int i = 0; i < hiddenLayerSize; i++) {
            hiddenLayer.add(new Node());
        }
        for (int i = 0; i < outputLayerSize; i++) {
            outputLayer.add(new Node());
        }
        generateRandomWeight();
    }

    public static double sigmoid(double input) {
        return 1.0 / (1.0 + Math.exp(-input));
    }

    public void generateRandomWeight() {
        Random random = new Random();

        for (Node node : this.inputLayer) {
            List<Double> weights = new ArrayList<>();
            for (int i = 0; i < this.hiddenLayerSize; i++) {
                weights.add(random.nextDouble());
            }
            node.setWeight(weights);
        }
        for (Node node : this.hiddenLayer) {
            List<Double> weights = new ArrayList<>();
            for (int i = 0; i < this.outputLayerSize; i++) {
                weights.add(random.nextDouble());
            }
            node.setWeight(weights);
        }
    }

    public static void backPropLearning(List<Integer> input, int output) {

    }

    public void forward(List<Integer> input) {
        for (int i = 0; i < inputLayerSize; i++) {
            inputLayer.get(i).setValue(input.get(i));
        }

        for (int i = 0; i < hiddenLayerSize; i++) {
            List<Double> feed = new ArrayList<>();
            for(Node node: this.inputLayer){
                feed.add(node.getValue()*node.getWeight().get(i));
            }
            hiddenLayer.get(i).setValue(sigmoid(summation(feed)));
        }

        for(int i=0;i<outputLayerSize;i++){
            List<Double> feed = new ArrayList<>();
            for(Node node: this.hiddenLayer){
                feed.add(node.getValue()*node.getWeight().get(i));
            }
            outputLayer.get(i).setValue(sigmoid(summation(feed)));
        }
    }

    public static double summation(List<Double> input) {
        double sum = 0;
        for (Double num : input) {
            sum +=num;
        }
        return sum;
    }

}
