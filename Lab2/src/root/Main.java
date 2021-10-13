package root;

public class Main {

    public static void main(String[] args) {
	SymbolTable st = new SymbolTable();
    if (st.getIndex("a") != 0)
        System.out.println("Error on check 1!");
    if (st.getIndex("b") != 1)
        System.out.println("Error on check 2!");
    if (st.getIndex("a") != 0)
        System.out.println("Error on check 3!");
    }
}
