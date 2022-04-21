import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.text.DecimalFormat;
import java.util.HashMap;

public class ID3_calculation {
    HashMap<String,Double > information_gain = new HashMap<>();
    HashMap<String,String > gain_per_sub_attribute = new HashMap<>();
    Vector attributes = new Vector();
    Vector classification = new Vector();
    List<Node> node = new ArrayList<>();

    double more_than_one_zero = 0.009;
    //H(S)
    double entropy =0;
    String [][] table ;

    public ID3_calculation(String[][] table) {
        this.table = table;
    }

    public List<Node> getNode(){
        return node;
    }

    public Vector getlistofAttributes(){
        return attributes;
    }

    public HashMap<String,Double> getInformationGain(){
        return information_gain;
    }


    public  HashMap<String,String > getInformationGain_of_subAttribute(){
        return gain_per_sub_attribute;
    }


    public void calculate_attribute(){
        for(int j = 0 ; j < table[0].length-1; j++){
            if(!attributes.contains(table[0][j])){
                attributes.addElement(table[0][j]);
            }
        }
    }

    public void calculate_class(){
        for(int i = 1 ; i < table.length; i++){
            for(int j = (table[i].length-1) ; j < table[i].length; j++){
                if(!classification.contains(table[i][j])){
                    classification.addElement(table[i][j]);
                }
            }
        }
    }

    public void calculate_entropy(){
        List<Integer> total_classification_num = new ArrayList<>();
        double total_rows  = table.length - 1;
        int count =0;
        // part 1 for H(S)
        for (Object o : classification) {
            for (int i = 1; i < table.length; i++) {
                if (o.toString().equals(table[i][(table[i].length - 1)])) {
                    count++;
                }
            }
            total_classification_num.add(count);
            count = 0;
        }
        // part 2 for H(S)

        for (double ps : total_classification_num) {
            double cls_entropy = -1 * ((ps / total_rows) * log(ps / total_rows));
            entropy = entropy + cls_entropy;
        }
        DecimalFormat df2 = new DecimalFormat(".##");
        String change = df2.format(entropy);
        if(change.contains(",")) {
            change = change.replace(",", ".");
        }
        entropy = Double.parseDouble(change);
        System.out.println( "\nH( S ) = " + entropy+"\n");
    }

    public void information_gain(){
        HashMap<String,Integer> frequency ;
        HashMap<String,List<String> > frequency_index ;
        HashMap<String,List<Classification> > classifies ;
        List<HashMap<String,List<Classification> > > listofclassifies = new ArrayList();
        List<HashMap<String,Integer>> listoffrequency  = new ArrayList();
        List<HashMap<String,List<String> >> listoffrequency_index  = new ArrayList();
        // initial values
        for(int i = 0 ; i < attributes.size(); i++ ){
            classifies = new HashMap<>();
            frequency = new HashMap<>();
            for(int j = 1 ; j < table.length; j++ ){
                if(! frequency.containsKey(table[j][i]) ){
                    frequency.put(table[j][i], 0);
                }
                List<Classification> temp = classifies.get(table[j][i]);
                if(classifies.containsKey(table[j][i]) ){
                    int flag =0;
                    for(int z =0 ; z < temp.size(); z++  ){
                        if(temp.get(z).getClassification_attributes().equals(table[j][table[j].length-1])){
                            String sub_attri = temp.get(z).getClassification_attributes();
                            int sub_attri_rep = temp.get(z).getRepetition();
                            flag =1;
                            sub_attri_rep++;
                            temp.remove(z);
                            temp.add(new Classification( sub_attri,sub_attri_rep) );
                            classifies.put(table[j][i],  temp );
                            break;
                        }
                    }
                    if(flag == 0){
                        temp.add(new Classification( table[j][table[j].length-1] ,1));
                        classifies.put(table[j][i],  temp );
                    }
                }
                else{
                    if(temp == null){
                        temp = new ArrayList<>();
                        temp.add(new Classification( table[j][table[j].length-1] ,1));
                        classifies.put(table[j][i],  temp );
                    }
                }
            }
            listofclassifies.add(classifies);
            listoffrequency.add(frequency);
        }

        // pre calculated
        for(int i = 0 ; i < attributes.size(); i++ ){
            List<String> attri = new ArrayList<>();
            frequency_index = new HashMap<>();
            for(int j = 1 ; j < table.length; j++ ){
                if(!attri.contains(table[j][i])){
                    attri.add(table[j][i]);
                }
                if( listoffrequency.get(i).containsKey(table[j][i]) ){
                    int count = listoffrequency.get(i).get(table[j][i]);
                    count++;
                    listoffrequency.get(i).put(table[j][i], count );
                    List<String> index = frequency_index.get(table[j][i]);
                    if(index==null){
                        index = new ArrayList<>();

                    }
                    index.add(table[j][ table[j].length-1 ]);
                    frequency_index.put(table[j][i], index);
                }
            }
            listoffrequency_index.add(frequency_index);
            node.add( new Node(attributes.get(i).toString() , attri , listoffrequency.get(i) ,listoffrequency_index.get(i) ,listofclassifies.get(i) ) );
        }

        System.out.println("Information Gain\n");

        // calculation
        for (Object attribute : attributes) {
            for (Node value : node) {
                if (attribute.equals(value.getAttribute())) {
                    double total_rows = table.length - 1;
                    List<String> parts = value.getList_of_attr();
                    // System.out.println(" "+ attributes.get(i) );
                    double[] sub1 = new double[parts.size()];
                    double[] sub2 = new double[parts.size()];
                    double sum = 0;
                    List<String> addd = new ArrayList<>();
                    // is ma sub attributes
                    for (int z = 0; z < parts.size(); z++) {
                        double sub_total_attribute = value.getFrequency().get(parts.get(z));
                        double sum2 = 0;
                        List<Classification> ty = value.getClassifies().get(parts.get(z));
                        //  System.out.println(" "+ parts.get(z) );
                        // initial value
                        int cc = ty.get(0).getRepetition();
                        String str = ty.get(0).getClassification_attributes();
                        for (int q = 0; q < ty.size(); q++) {
                            if (q >= 1) {
                                if (cc == ty.get(q).getRepetition()) {
                                    if (!addd.contains(ty.get(q).getClassification_attributes())) {
                                        cc = ty.get(q).getRepetition();
                                        str = ty.get(q).getClassification_attributes();
                                        addd.add(str);
                                    }
                                } else if (cc < ty.get(q).getRepetition()) {
                                    cc = ty.get(q).getRepetition();
                                    str = ty.get(q).getClassification_attributes();
                                    addd.add(str);

                                }
                            }
                            // System.out.println( sub_total_attribute +" "+ty.get(q).getClassification_attributes() +" = "+ ty.get(q).getRepetition());
                            double nu = ty.get(q).getRepetition();
                            sum2 = sum2 + (-1 * (nu / sub_total_attribute * log(nu / sub_total_attribute)));
                            //sum  = sum + (sub_total_attribute/total_rows) *  ;
                        }
                        sub1[z] = sum2;
                        sub2[z] = sub_total_attribute / total_rows;
                        gain_per_sub_attribute.put(parts.get(z), str);
                    }
                    for (int z = 0; z < parts.size(); z++) {
                        sum = sum + (sub1[z] * sub2[z]);
                        if ((sub1[z] * sub2[z]) == 0) {
                            gain_per_sub_attribute.put(parts.get(z) + "1", gain_per_sub_attribute.get(parts.get(z)));
                            gain_per_sub_attribute.put(parts.get(z), "0");
                        }
                    }

                    DecimalFormat df2 = new DecimalFormat(".###");
                    String change = df2.format(sum);
                    if (change.contains(",")) {
                        change = change.replace(",", ".");
                    }
                    sum = Double.parseDouble(change);

                    double gain = entropy - sum;
                    DecimalFormat df3 = new DecimalFormat(".####");
                    change = df3.format(gain);
                    if (change.contains(",")) {
                        change = change.replace(",", ".");
                    }
                    gain = Double.parseDouble(change);

                    if (gain == 0.0) {
                        gain = more_than_one_zero;
                        more_than_one_zero = more_than_one_zero - 0.001;
                    }
                    information_gain.put(attribute.toString(), gain);
                    System.out.println(attribute + " = " + gain);
                    // if more than one value is
                }
            }
        }
    }

    static double log(double x)
    {
        return Math.log(x) / Math.log(2);
    }
}
