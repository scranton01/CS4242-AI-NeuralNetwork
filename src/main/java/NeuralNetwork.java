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
    Node biasHidden;
    Node biasOutput;


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
        biasHidden = new Node();
        biasOutput = new Node();
        biasHidden.setValue(1);
        biasOutput.setValue(1);

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
            for (int i = 0; i < this.hiddenLayerSize; i++) {
                node.getWeight().add(random.nextDouble());
            }
        }
        for (Node node : this.hiddenLayer) {
            for (int i = 0; i < this.outputLayerSize; i++) {
                node.getWeight().add(random.nextDouble());
            }
        }
        for (int i = 0; i < this.hiddenLayerSize; i++) {
            biasHidden.getWeight().add(random.nextDouble());
        }
        for (int i = 0; i < this.outputLayerSize; i++) {
            biasOutput.getWeight().add(random.nextDouble());
        }
    }


    public static double sigmoid(double input) {
        double result = 1.0 / (1.0 + (Math.exp(-input))) * 9;
        return result;
    }

    //  https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
    public void backPropLearning() {
        //first layer
        for (int i = 0; i < hiddenLayerSize; i++) {
            double sum = 0;
            for (Node node : this.inputLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            sum += (biasHidden.getValue() * biasHidden.getWeight().get(i));
            this.hiddenLayer.get(i).setValue(sigmoid(sum));
        }

        //second layer
//        double errorTotal = 0;
        double actual = 0;
        for (int i = 0; i < outputLayerSize; i++) {
            double sum = 0;
            for (Node node : this.hiddenLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            sum += (biasOutput.getValue() * biasOutput.getWeight().get(i));
            actual = sigmoid(sum);
        }

        //back propagation
        //second layer
        List<Double> updatedLayer2WeightList = new ArrayList<>();

        double deltaSecondLayer = 0;
        for (int i = 0; i < this.hiddenLayerSize; i++) {
            deltaSecondLayer = (-(outputLayer.getValue() - actual)) * (actual * (1 - actual)) * this.hiddenLayer.get(i).getValue();
            double currentWeight = this.hiddenLayer.get(i).getWeight().get(0);
            double updatedWeight = currentWeight - (deltaSecondLayer * this.learningRate);//changed
            System.out.println(String.format("Second layer: %f -> %f", currentWeight, updatedWeight));
            updatedLayer2WeightList.add(updatedWeight);
        }
        double biasSecondLayerWeight = this.biasOutput.getWeight().get(0);
        double biasSecondLayerUpdatedWeight = biasSecondLayerWeight - (deltaSecondLayer * this.learningRate);
        biasOutput.getWeight().set(0, biasSecondLayerUpdatedWeight);

        //first layer
        for (int i = 0; i < this.hiddenLayerSize; i++) {
            for (Node node : this.inputLayer) {
                double errorT = -(outputLayer.getValue() - actual) * (actual * (1 - actual)) * hiddenLayer.get(i).getWeight().get(0);
                double sigmoidDerivative = hiddenLayer.get(i).getValue() * (1 - hiddenLayer.get(i).getValue());
                double input = node.getValue();
                double delta = errorT * sigmoidDerivative * input;
                double currentWeight = node.getWeight().get(i);
                double updatedWeight = currentWeight - (delta * this.learningRate); //changed
                System.out.println(String.format("First layer: %f -> %f", currentWeight, updatedWeight));
                node.getWeight().set(i, updatedWeight);
            }
            //update bias node
            double errorT = -(outputLayer.getValue() - actual) * (actual * (1 - actual)) * hiddenLayer.get(i).getWeight().get(0);
            double sigmoidDerivative = hiddenLayer.get(i).getValue() * (1 - hiddenLayer.get(i).getValue());
            double input = biasHidden.getValue();
            double delta = errorT * sigmoidDerivative * input;
            double currentWeight = biasHidden.getWeight().get(i);
            double updatedWeight = currentWeight - (delta * this.learningRate); //changed
            System.out.println(String.format("First layer: %f -> %f", currentWeight, updatedWeight));
            biasHidden.getWeight().set(i, updatedWeight);
        }

        //update second layer weights
        for (int i = 0; i < this.hiddenLayerSize; i++) {
            hiddenLayer.get(i).getWeight().set(0, updatedLayer2WeightList.get(i));
        }
    }

    boolean checkSolution() {
        //first layer
        for (int i = 0; i < hiddenLayerSize; i++) {
            double sum = 0;
            for (Node node : this.inputLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            this.hiddenLayer.get(i).setValue(sigmoid(sum));
        }
        //second layer
        double actual = 0;
        for (int i = 0; i < outputLayerSize; i++) {
            double sum = 0;
            for (Node node : this.hiddenLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            actual = sigmoid(sum);
        }
        return Math.round(actual) == Math.round(outputLayer.getValue());
    }


}
