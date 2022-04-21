import java.util.HashMap;
import java.util.List;

public class Node {

String attribute;
List<String> list_of_attr;
HashMap<String,Integer> frequency;
HashMap<String,List<String> > frequency_index ;
HashMap<String,List<Classification> > classifies ;

    public Node(String attribute, List<String> list_of_attr, HashMap<String, Integer> frequency, HashMap<String, List<String>> frequency_index, HashMap<String, List<Classification>> classifies) {
        this.attribute = attribute;
        this.list_of_attr = list_of_attr;
        this.frequency = frequency;
        this.frequency_index = frequency_index;
        this.classifies = classifies;
    }

    public String getAttribute() {
        return attribute;
    }

    public List<String> getList_of_attr() {
        return list_of_attr;
    }

    public HashMap<String, Integer> getFrequency() {
        return frequency;
    }

    public HashMap<String, List<Classification>> getClassifies() {
        return classifies;
    }

}
