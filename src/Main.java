import java.util.*;
import java.io.*;
class Main {
    public static void main(String[] args) throws Exception{
        File file=new File("randomtext.txt");
        Scanner sc=new Scanner(file);
        LinkedList<String> l=new LinkedList<String>();
        while(sc.hasNextLine()) {
            String line=sc.nextLine();
            String[] words=line.split("\\s");
            for(String i:words) 
                l.add(i);
            l.add("\n");
        }
        Editor edit=new Editor(l);
        sc=new Scanner(System.in);
        while(true)
        {
            System.out.println("S - Save and Exit");
            System.out.println("F - Move Forward");
            System.out.println("B - Move Backward");
            System.out.println("b - Move to Beginning of Line");
            System.out.println("e - Move to End of Line");
            System.out.println("i - Insert");
            System.out.println("d - Delete");
            edit.print();
            edit.printCursor();
            String input=sc.nextLine();
            if(input.equals("S")) break;
            else if(input.equals("F")) edit.forward();
            else if(input.equals("B")) edit.backward();
            else if(input.equals("b")) edit.beginning();
            else if(input.equals("e")) edit.ending();
            else if(input.equals("i")) edit.insert(sc.nextLine());
            else if(input.equals("d")) edit.delete();
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            file.delete();
            file.createNewFile();
        }
        edit.writer.close();
    }
}
class Editor {
    LinkedList<String> l;
    int node,position;
    FileWriter writer;
    public Editor(LinkedList<String> l) {
        this.l=l;
        node=position=0;
    }
    public void forward(){
        String s=l.get(node);
        if(position==s.length()) {
            if(node==l.size()-1) node=0;
            else node++;
            position=0;
        }
        else position++;
        s=l.get(node);
        if(s.toString().equals("\n")) forward();
    }
    public void backward(){
        String s=l.get(node);
        if(position==0) {
            if(node==0) node=l.size()-1;
            else node--;
            s=l.get(node);
            position=s.length();
        }
        else position--;
        s=l.get(node);
        if(s.toString().equals("\n")) backward();
    }
    public void insert(String input) {
        String s=l.get(node);
        if(s.length()==0) s.append(input);
        else String nuevo = s.substring(0, position) + input + s.substring(position);
        l.set(node, nuevo);
        position += input.length();
        position+=input.length();
    }
    public void delete() {
        String s=l.get(node);
        if(position==0 && node>0){
            String prev=l.get(node-1);
            position=prev.length();
            l.set(node-1, prev + s);
            l.remove(node);
        }
        else if(position>0){
            String nuevo = s.substring(0, position-1) + s.substring(position);
            l.set(node, nuevo);
            position--;
        }
    }
    public void beginning() {
       while(node>0 && !l.get(node).toString().equals("\n")) node--;
       if(node!=0) node=node+1;
       position=0;
    }
    public void ending() {
       while(!l.get(node).toString().equals("\n")) node++;
       node=node-1;
       position=l.get(node).length();
    }
    public void print() throws Exception{
        writer= new FileWriter("randomtext.txt");
        for(String i:l) {
            writer.write(i);
            System.out.print(i);
            if(!i.equals("\n")) {
                writer.write(" ");
                System.out.print(" ");
            }
        }
    }
    public void printCursor() {
        System.out.print("Cursor At : ");
        String s=l.get(node);
        for(int i=0; i<s.length(); i++) {
            if(i==position) System.out.print("|");
            System.out.print(s.charAt(i));
        }
        if((position==0 && s.length()==0) || (position==s.length())) System.out.print("|");
        System.out.println();
    }
}
