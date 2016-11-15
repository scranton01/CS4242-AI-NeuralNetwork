import lombok.Data;

import java.util.*;

@Data
public class NeuralNetwork {
    int inputLayerSize;
    int hiddenLayerSize;
    int outputLayerSize;
    double learningRate;
    List<Node> inputLayer;
    List<Node> hiddenLayer;
    Node outputLayer;


    NeuralNetwork(int input, int hidden, int output, double learningRate) {
        this.inputLayerSize = input;
        this.hiddenLayerSize = hidden;
        this.outputLayerSize = output;
        this.learningRate = learningRate;
        this.inputLayer = new ArrayList<>();
        this.hiddenLayer = new ArrayList<>();
        for (int i = 0; i < this.inputLayerSize; i++) {
            this.inputLayer.add(new Node());
        }
        for (int i = 0; i < this.hiddenLayerSize; i++) {
            this.hiddenLayer.add(new Node());
        }
        outputLayer = new Node();

        generateRandomWeight();
    }

    public void setInputOutput(List<Double> input, double output) {
        for (int i = 0; i < this.inputLayerSize; i++) {
            this.inputLayer.get(i).setValue(input.get(i));
        }
        this.outputLayer.setValue(output);
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


    public static double sigmoid(double input) {
//        double result = Math.exp(input) / (denominator);
//        return result;
        return 1.0 / (1.0 + (Math.exp(-input)));
    }

    public static double sigmoidDerivative(double input) {
        double y = sigmoid(input);
        return y * (1 - y);
    }

    //https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
    public void forward() {
        //first layer
//        double denominator = findDenominator(inputLayer);
        for (int i = 0; i < hiddenLayerSize; i++) {
            double sum = 0;
            for (Node node : this.inputLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            this.hiddenLayer.get(i).setValue(sigmoid(sum));
        }

        //second layer
//        denominator = findDenominator(hiddenLayer);
        double errorTotal = 0;
        double actual = 0;
        for (int i = 0; i < outputLayerSize; i++) {
            double sum = 0;
            for (Node node : this.hiddenLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            actual = sigmoid(sum);
//            errorTotal +=(1/2)*Math.pow((outputLayer.get(i).getValue()-actual),2);
//            double errorOutput = -(outputLayer.get(i).getValue()-actual);
//            double errorInput = actual*(1-actual);
//            double errorWeight = ()
        }

        //back propagation
        //first layer
        List<Double> updatedWeightList = new ArrayList<>();
        for (int i = 0; i < this.hiddenLayerSize; i++) {
            double delta = -(outputLayer.getValue() - actual) * actual * (1 - actual) * this.hiddenLayer.get(i).getValue();
            double currentWeight = this.hiddenLayer.get(i).getWeight().get(0);
            double updatedWeight = currentWeight - (delta * this.learningRate);
            updatedWeightList.add(updatedWeight);
        }




    }


    public static double summation(List<Double> input) {
        double sum = 0;
        for (Double num : input) {
            sum += num;
        }
        return sum;
    }

    public static double findDenominator(List<Node> nodeList) {
        double denominator = 0;
        for (int i = 0; i < nodeList.get(0).getWeight().size(); i++) {
            double aggregatedSum = 0;
            for (Node node : nodeList) {
                aggregatedSum += node.getValue() * node.getWeight().get(i);
            }
            denominator += Math.exp(aggregatedSum);
        }
        return denominator;
    }

}
