import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class GenerateTree {

    Vector list_of_attr;
    List<Node> node;
    HashMap<String,Double > information_gain; 
    HashMap<String,String > information_gain_subAttribute; 
    TreeNode tree_node = new TreeNode();
    HashMap <String , List<String>> main_attr = new HashMap<>();
    List<String> sort;


    public GenerateTree(Vector list_of_attr, List<Node> node , HashMap<String,Double > information_gain , HashMap<String,String > information_gain_subAttribute ){
        this.list_of_attr = list_of_attr;
        this.node = node;
        this.information_gain = information_gain;
        this.information_gain_subAttribute = information_gain_subAttribute;
    }
    
    public void display_attribute() {
        System.out.println("\nAll attributes of dataset\n");
        for(int i =0 ; i < sort.size(); i++){
            System.out.print((i+1)+" "+sort.get(i)+" ");
        }
    }
    
    public void create_tree() {
        System.out.println("\nGenerating or Creating Tree .... \n");
        sort = new ArrayList();
        double aa =0;
        String loc ="";
        // for sorting , finding root
        for(int i = 0 ; i < information_gain.size(); i++ ){
            for(String jj:  information_gain.keySet() ){
               if(!sort.contains(jj)){
                   if(aa < information_gain.get(jj) ){
                       aa = information_gain.get(jj);
                       loc = jj;
                   }
               }
           }
            if(!loc.equals("")){
              sort.add(loc);
            }
          aa=0;
          loc = "";
        }

       System.out.println(sort);
       // get all main or sub attribute stored
        for (Node value : node) {
            String main = value.getAttribute();
            List<String> rel = value.getList_of_attr();
            main_attr.put(main, rel);
        }
       // set root
       tree_node.Set_root(sort.get(0));
       int count =1; 
       // traverse and create tree
       for(int i = 0 ; i < sort.size()-1 ; i++){
           String parent =  sort.get(i);
           List<String> rel = main_attr.get(parent);
           List<String> child = new ArrayList<>();
           // setting childs
           for (String s : rel) {
               if (this.information_gain_subAttribute.get(s).equals("0")) {
                   child.add(information_gain_subAttribute.get((s + "1")));
               } else {
                   if (count < sort.size() - 1) {
                       child.add(sort.get(count));
                       count++;
                   } else {
                       child.add(information_gain_subAttribute.get(s));
                   }
               }
           }
           tree_node.add_parent_child(parent, child);
           tree_node.add_relation(parent, rel);
       }
    }
    
    
    public void display_tree(){
        System.out.println("\n\nRoot == "+ tree_node.get_root() + "\n");
   	    System.out.println(tree_node.get_root());
        go_for_child(tree_node.get_root() , "" );
    }


    public void go_for_child(String parent, String space) {
    	 List<String> rel = tree_node.getRelation(parent);
         List<String> child = tree_node.getChild(parent);
    	 if(child != null) {
    		 int c = rel.size();
    		 for(int i = 0 ; i < c ; i++) {
    			 System.out.println(space+" "+rel.get(i)+":");
    			 System.out.println(space+"   "+child.get(i));
    			 if(tree_node.getChild(child.get(i)) != null )  {
    				 String temp = space+"     ";
    				 go_for_child( child.get(i) ,  temp );
    			 }
    		 }
    	 }
    }
}
