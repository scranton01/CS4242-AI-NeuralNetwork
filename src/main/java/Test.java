import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Random random = new Random();
        System.out.println(random.nextDouble());
        System.out.println("----------");
        int x = 4;
        double y = .3;
        System.out.println(x * y);
        System.out.println("------------");
        List<Double> nums = new ArrayList<>();
        nums.add(13.2);
        nums.add(234.23);
        System.out.println(NeuralNetwork.summation(nums));
    }
}
