/**
 * */
final class Rule{
    int up = 0;
    String[] names = new String[]{};
    int level = 0;

    public Rule(int up, String[] names, int level){
        this.level = level;
        this.names = names;
        this.up = up;
    }

    public int getUp(){
        return this.up;
    }

    public int getLevel(){
        return this.level;
    }

    public String[] getNames(){
        return this.names;
    }

    @Override
    public String toString() {
        String names = "Up: "+getUp()+" Level: "+getLevel()+" ";
        for(String name : this.names){
            names+=name+" ";
        }
        return names;
    }
}