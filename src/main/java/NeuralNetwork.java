import lombok.Data;

import java.util.*;


@Data
public class NeuralNetwork {
    double learningRate;
    List<Node> inputLayer;
    List<Node> hiddenLayer;
    List<Node> outputLayer;
    Node biasHidden;
    Node biasOutput;

    NeuralNetwork(int inputSize, int hiddenSize, double learningRate) {
        this.learningRate = learningRate;
        this.inputLayer = new ArrayList<>();
        for (int i = 0; i < inputSize; i++) {
            this.inputLayer.add(new Node());
        }
        this.hiddenLayer = new ArrayList<>();
        for (int i = 0; i < hiddenSize; i++) {
            this.hiddenLayer.add(new Node());
        }
        this.outputLayer = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.outputLayer.add(new Node());
        }
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
        for (int i = 0; i < this.outputLayer.size(); i++) {
            if (i == output) {
                this.outputLayer.get(i).setValue(1);
            } else {
                this.outputLayer.get(i).setValue(0);
            }
        }
    }

    public void generateRandomWeight() {
        Random random = new Random();

        for (Node node : this.inputLayer) {
            for (int i = 0; i < this.hiddenLayer.size(); i++) {
                node.getWeight().add(random.nextDouble() / 100);
            }
        }
        for (Node node : this.hiddenLayer) {
            for (Node outputNode : this.outputLayer) {
                node.getWeight().add(random.nextDouble() / 100);
            }
        }
        for (int i = 0; i < this.hiddenLayer.size(); i++) {
            biasHidden.getWeight().add(random.nextDouble() / 100);
        }
        for (int i = 0; i < this.outputLayer.size(); i++) {
            biasOutput.getWeight().add(random.nextDouble() / 100);
        }
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
        List<Double> actual = new ArrayList<>();
        for (int i = 0; i < outputLayer.size(); i++) {
            double sum = 0;
            for (Node node : hiddenLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            sum += (biasOutput.getValue() * biasOutput.getWeight().get(i));
            actual.add(sigmoid(sum));
        }

        //back propagation
        //second layer
        List<Double> updatedLayer2WeightList = new ArrayList<>();
        double deltaSecondLayer = 0;
        for (int i = 0; i < outputLayer.size(); i++) {
            for (Node node : hiddenLayer) {
                double error = outputLayer.get(i).getValue() - actual.get(i);
                double sigmoidDerivative = actual.get(i) * (1 - actual.get(i));
                double hiddenValue = node.getValue();
                deltaSecondLayer = -(error * sigmoidDerivative * hiddenValue);
                double currentWeight = node.getWeight().get(i);
                double updatedWeight = currentWeight - (deltaSecondLayer * learningRate);//changed
                updatedLayer2WeightList.add(updatedWeight);
            }
            double biasSecondLayerWeight = biasOutput.getWeight().get(i);
            double biasSecondLayerUpdatedWeight = biasSecondLayerWeight - (deltaSecondLayer * learningRate);
            biasOutput.getWeight().set(0, biasSecondLayerUpdatedWeight);
        }
        //first layer
        for (int i = 0; i < hiddenLayer.size(); i++) {
            for (Node node : inputLayer) {
                double errorT = 0;
                for (int j = 0; j < outputLayer.size(); j++) {
                    errorT += -(outputLayer.get(j).getValue() - actual.get(j)) * (actual.get(j) * (1 - actual.get(j))) * hiddenLayer.get(i).getWeight().get(j);
                }
                double sigmoidDerivative = hiddenLayer.get(i).getValue() * (1 - hiddenLayer.get(i).getValue());
                double input = node.getValue();
                double delta = errorT * sigmoidDerivative * input;
                double currentWeight = node.getWeight().get(i);
                double updatedWeight = currentWeight - (delta * learningRate); //changed
                node.getWeight().set(i, updatedWeight);
            }
            //update bias node
            double errorT = 0;
            for (int j = 0; j < outputLayer.size(); j++) {
                errorT += -(outputLayer.get(j).getValue() - actual.get(j)) * (actual.get(j) * (1 - actual.get(j))) * hiddenLayer.get(i).getWeight().get(j);
            }
            double sigmoidDerivative = hiddenLayer.get(i).getValue() * (1 - hiddenLayer.get(i).getValue());
            double input = biasHidden.getValue();
            double delta = errorT * sigmoidDerivative * input;
            double currentWeight = biasHidden.getWeight().get(i);
            double updatedWeight = currentWeight - (delta * learningRate); //changed
            biasHidden.getWeight().set(i, updatedWeight);
        }

        //update second layer weights
        int weightIndex = 0;
        for (int i = 0; i < outputLayer.size(); i++) {
            for (Node node : hiddenLayer) {
                node.getWeight().set(i, updatedLayer2WeightList.get(weightIndex++));
            }
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
        List<Double> actual = new ArrayList<>();
        for (int i = 0; i < outputLayer.size(); i++) {
            double sum = 0;
            for (Node node : hiddenLayer) {
                sum += (node.getValue() * node.getWeight().get(i));
            }
            sum += (biasOutput.getValue() * biasOutput.getWeight().get(i));
            actual.add(sigmoid(sum));
        }
        int bestGuess = 0;
        for (int i = 0; i < actual.size(); i++) {
            if (actual.get(bestGuess) < actual.get(i)) {
                bestGuess = i;
            }
        }
        int expected = 0;
        for (int i = 0; i < outputLayer.size(); i++) {
            if (outputLayer.get(expected).getValue() < outputLayer.get(i).getValue()) {
                expected = i;
            }
        }
//        System.out.println(String.format("expected: %d actual: %d", expected, bestGuess));
        return bestGuess == expected;
    }
}
