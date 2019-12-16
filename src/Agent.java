/**************************************************************************
 *                                                                        *
 *                         SimArknight ver 1.0                            *
 *                                                                        *
 *                                                                        *
 **************************************************************************/
final class Agent {
    private int level = 0;
    private String name = "";
    private String type = "";

    public Agent(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public Agent(){}

    public void setLevel(int level){
        this.level = level;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setType(String type){
        this.type = type;
    }

    public int getLevel(){
        return this.level;
    }

    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }
    @Override
    public String toString() {
        return this.level+" "+this.name+" "+this.type;
    }
}
