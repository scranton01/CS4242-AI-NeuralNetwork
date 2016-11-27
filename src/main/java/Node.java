import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    double value;
    List<Double> weight;

    Node() {
        this.weight = new ArrayList<>();
    }
}
