import lombok.Data;

import java.util.*;

@Data
public class NeuralNetwork {
    double learningRate;
    List<Node> inputLayer;
    List<Node> hiddenLayer;
    Node outputLayer;
    Node biasHidden;
    Node biasOutput;

    NeuralNetwork(int inputSize, int hiddenSize, double learningRate) {
        this.learningRate = learningRate;
        this.inputLayer = new ArrayList<>();
        this.hiddenLayer = new ArrayList<>();
        for (int i = 0; i < inputSize; i++) {
            this.inputLayer.add(new Node());
        }
        for (int i = 0; i < hiddenSize; i++) {
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
        for (int i = 0; i < this.inputLayer.size(); i++) {
            this.inputLayer.get(i).setValue(input.get(i));
        }
        this.outputLayer.setValue(output);
    }

    public void generateRandomWeight() {
        Random random = new Random();

        for (Node node : this.inputLayer) {
            for (int i = 0; i < this.hiddenLayer.size(); i++) {
                node.getWeight().add(random.nextDouble() / 100);
            }
        }
        for (Node node : this.hiddenLayer) {
            node.getWeight().add(random.nextDouble() / 100);
        }
        for (int i = 0; i < this.hiddenLayer.size(); i++) {
            biasHidden.getWeight().add(random.nextDouble() / 100);
        }
        biasOutput.getWeight().add(random.nextDouble() / 100);

    }

    public static double sigmoid(double input) {
        double result = 1.0 / (1.0 + (Math.exp(-input)));
        return result;
    }

    public void backPropLearning() {
        //first layer
        for (int i = 0; i < hiddenLayer.size(); i++) {
            double sum = 0;
            for (Node node : inputLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            sum += (biasHidden.getValue() * biasHidden.getWeight().get(i));
            this.hiddenLayer.get(i).setValue(sigmoid(sum));
        }

        //second layer
        double actual = 0;
        double sum = 0;
        for (Node node : hiddenLayer) {
            sum += (node.getValue() * node.getWeight().get(0));
        }
        sum += (biasOutput.getValue() * biasOutput.getWeight().get(0));
        actual += sigmoid(sum);


        //back propagation
        //second layer
        List<Double> updatedLayer2WeightList = new ArrayList<>();

        double deltaSecondLayer = 0;
        for (int i = 0; i < hiddenLayer.size(); i++) {
            double error = outputLayer.getValue() / 10.0 - actual;
            double sigmoidDerivative = actual * (1 - actual);
            double hiddenValue = hiddenLayer.get(i).getValue();
            deltaSecondLayer = -(error) * (sigmoidDerivative) * hiddenValue;
            double currentWeight = hiddenLayer.get(i).getWeight().get(0);
            double updatedWeight = currentWeight - (deltaSecondLayer * learningRate);//changed
            updatedLayer2WeightList.add(updatedWeight);
        }
        double biasSecondLayerWeight = biasOutput.getWeight().get(0);
        double biasSecondLayerUpdatedWeight = biasSecondLayerWeight - (deltaSecondLayer * learningRate);
        biasOutput.getWeight().set(0, biasSecondLayerUpdatedWeight);

        //first layer
        for (int i = 0; i < hiddenLayer.size(); i++) {
            for (Node node : inputLayer) {
                double errorT = -(outputLayer.getValue() / 10 - actual) * (actual * (1 - actual)) * hiddenLayer.get(i).getWeight().get(0);
                double sigmoidDerivative = hiddenLayer.get(i).getValue() * (1 - hiddenLayer.get(i).getValue());
                double input = node.getValue();
                double delta = errorT * sigmoidDerivative * input;
                double currentWeight = node.getWeight().get(i);
                double updatedWeight = currentWeight - (delta * learningRate); //changed
                node.getWeight().set(i, updatedWeight);
            }
            //update bias node
            double errorT = -(outputLayer.getValue() / 10 - actual) * (actual * (1 - actual)) * hiddenLayer.get(i).getWeight().get(0);
            double sigmoidDerivative = hiddenLayer.get(i).getValue() * (1 - hiddenLayer.get(i).getValue());
            double input = biasHidden.getValue();
            double delta = errorT * sigmoidDerivative * input;
            double currentWeight = biasHidden.getWeight().get(i);
            double updatedWeight = currentWeight - (delta * learningRate); //changed
            biasHidden.getWeight().set(i, updatedWeight);
        }

        //update second layer weights
        for (int i = 0; i < hiddenLayer.size(); i++) {
            hiddenLayer.get(i).getWeight().set(0, updatedLayer2WeightList.get(i));
        }
    }

    boolean checkSolution() {
        //first layer
        for (int i = 0; i < hiddenLayer.size(); i++) {
            double sum = 0;
            for (Node node : this.inputLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            this.hiddenLayer.get(i).setValue(sigmoid(sum));
        }
        //second layer
        double actual = 0;
        double sum = 0;
        for (Node node : this.hiddenLayer) {
            sum += (node.getValue() * node.getWeight().get(0));
        }
        actual = sigmoid(sum);
        actual = actual * 10;

        return Math.floor(actual) == outputLayer.getValue();
    }
}
