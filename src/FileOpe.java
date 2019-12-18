import java.io.*;
import java.util.ArrayList;

/**************************************************************************
 *                                                                        *
 *                         SimArknight ver 2.1                            *
 *                                                                        *
 *                                                                        *
 **************************************************************************/

public class FileOpe {
    private static final String list_src = "res/List.alist";
    private static final String rule_src = "res/upRule/^name.rlist";
    private final String cont_list = "<Agent>\r\n\t<Type>^t</Type>\r\n\t<Name>^n</Name>\r\n\t<Level>^l</Level>\r\n</Agent>\r\n";
    private final String cont_rule = "<Rule>\r\n\t<Level>^l</Level>\r\n\t<Name>^n</Name>\r\n\t<Up>^u</Up>\r\n</Rule>\r\n";

    public void creatAgentList() throws IOException {
        String[] ag6 = "推进之王/能天使/闪灵/星熊/伊芙利特/塞雷娅/银灰/夜莺/艾雅法拉/陈/安洁莉娜/斯卡蒂/黑/赫拉格/麦哲伦/莫斯提马".split("/");
        String[] ag5 = "幽灵鲨/芙兰卡/德克萨斯/凛冬/白面鸮/蓝毒/普罗旺斯/红/赫默/雷蛇/临光/夜魔/天火/初雪/拉普兰德/华法琳/守林人/狮蝎/真理/白金/陨星/可颂/梅尔/崖心/食铁兽/空/诗怀雅/格劳克斯/星极/送葬人/槐琥".split("/");
        String[] ag4 = "深海色/杜宾/梅/远山/夜烟/白雪/流星/蛇屠箱/末药/猎蜂/慕斯/砾/暗索/调香师/地灵/霜叶/清道夫/古米/角峰/缠丸/阿消/红豆/杰西卡/格雷伊/苏苏洛/桃金娘/红云".split("/");
        String[] ag3 = "芬/克洛丝/炎熔/米格鲁/芙蓉/卡缇/史都华德/香草/玫兰莎/安赛尔/梓兰/翎羽/空爆/月见夜/斑点/泡普卡".split("/");
        for (String s : ag3) {
            String cc = cont_list.replace("^n", s);
            cc = cc.replace("^l", "3");
            UpdateAgentList(cc);
            System.out.println(cc);
        }
    }

    public void updateAgentList(String type, String name, String level) throws IOException {
        if (new File(list_src).exists()) {
            System.out.println("get in");
            String rep_Tag = cont_list.replace("^n", name);
            rep_Tag = rep_Tag.replace("^l", level);
            rep_Tag = rep_Tag.replace("^t", type);
            UpdateAgentList(rep_Tag);
        }
    }

    public void updateAgentListType(String type, String name, String level) throws IOException {
        if (new File(list_src).exists()) {
            String context = readAgentList();
            String rep_Tag = cont_list.replace("^n", name);
            rep_Tag = rep_Tag.replace("^l", level);
            if (!context.contains(rep_Tag))
                System.out.println("result not exist!");
            else {
                String new_Tag = rep_Tag.replace("^t", type);
                context = context.replace(rep_Tag, new_Tag);
            }
            writeAgentList(context);
        }
    }

    /**
     * Add new data at the end of file
     * */
    private void UpdateAgentList(String content) throws IOException {
        String text = "";
        File file = new File(list_src);
        if (!file.exists()) file.createNewFile();
        text = readAgentList();
        BufferedWriter bw = new BufferedWriter(new FileWriter(list_src));
        bw.write(text + content);
        bw.flush();
        bw.close();
    }

    /**
     * Write / Rewrite a file, will remove all previous data in file
     * */
    private void writeAgentList(String content) throws IOException {
        File file = new File(list_src);
        if (!file.exists()) file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(list_src));
        bw.write(content);
        bw.flush();
        bw.close();
    }

    private String readAgentList() throws IOException {
        return readAList(list_src);
    }

    /**
     * Return the data in file
     *
     * @param address a location string
     * @return text contain all data in file*/
    private String readAList(String address) throws IOException {
        String text = "";
//        File file = new File(address);
//        Long filelength = file.length();
        //FileInputStream in = new FileInputStream(file);
        InputStream in = this.getClass().getResourceAsStream(address);
        byte[] filecontent = new byte[in.available()];
        text = new String(filecontent, 0, in.read(filecontent));
        in.close();
        return text;
    }

    private void readALink(String address) throws IOException{
        InputStream in = this.getClass().getResourceAsStream(address);
    }

    /**
     * Get the Agent data in file and package in Agent
     *
     * @return out an ArrayList with Agent*/
    public ArrayList<Agent> getAgentList() {
        ArrayList<Agent> out = new ArrayList<>();
        try {
            String agentList = readAgentList();
            String[] agents = agentList.split("</Agent>");
            for (int i = 0; i < agents.length; i++) {
                Agent agent = new Agent();
                agents[i] = agents[i].replace("<Agent>", "").replace("\t", "");
                String[] agentsInfo = agents[i].split("\r\n");
                for (String agentInfo : agentsInfo) {
                    if (agentInfo.contains("Type"))
                        agent.setType(agentInfo.replace("<Type>", "").replace("</Type>", ""));
                    if (agentInfo.contains("Name"))
                        agent.setName(agentInfo.replace("<Name>", "").replace("</Name>", ""));
                    if (agentInfo.contains("Level"))
                        agent.setLevel(Integer.parseInt(agentInfo.replace("<Level>", "").replace("</Level>", "")));
                }
                out.add(agent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        out.remove(out.size() - 1);
        return out;
    }

    /**
     * Set prob rules, record data in files
     *
     * @param name a file name
     * @param names a 2-D array contain the agents name and level for up prob
     * @param up prob up*/
    public void setRule(String name, String[][] names, int[] up) {
        String items = "";
        for (int i = 0; i < names.length; i++) {
            String item = cont_rule.replace("^l", names[i][0]).replace("^u", up[i] + "");
            String namelist = "";
            for (int j = 1; j < names[i].length; j++) {
                if (j == names[i].length - 1)
                    namelist += names[i][j];
                else
                    namelist += names[i][j] + " ";
            }
            item = item.replace("^n", namelist);
            items += item;
        }
        try {
            UpdateFile(name, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpdateFile(String name, String content) throws IOException {
        String location = rule_src.replace("^name", name);
        String text = "";
        File file = new File(location);
        if (!file.exists()) file.createNewFile();
        text = readAList(location);
        BufferedWriter bw = new BufferedWriter(new FileWriter(location));
        System.out.println(text + content);
        bw.write(text + content);
        bw.flush();
        bw.close();
    }

    /**
     * Get the rule in file
     *
     * @param name the file name
     * @return rules which the ArrayList contain Rules*/
    public ArrayList<Rule> getRule(String name) {
        ArrayList<Rule> rules = new ArrayList<>();
        int level = 0, up = 0;
        String[] names = new String[]{};
        String location = rule_src.replace("^name", name);
        try {
            String context = readAList(location);
            String[] items = context.split("</Rule>\r\n");
            for (String item : items) {
                item = item.replace("<Rule>", "").replace("\t", "");
                String[] detail = item.split("\r\n");
                for (String details : detail) {
                    if (details.contains("Level"))
                        level = Integer.parseInt(details.replace("<Level>", "").replace("</Level>", ""));
                    if (details.contains("Name"))
                        names = details.replace("<Name>", "").replace("</Name>", "").split(" ");
                    if (details.contains("Up"))
                        up = Integer.parseInt(details.replace("<Up>", "").replace("</Up>", ""));
                }
                rules.add(new Rule(up, names, level));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rules;
    }

    public static void main(String[] args) throws IOException {
        FileOpe f = new FileOpe();
//        //f.creatAgentList();
//        f.updateAgentList("xf", "苇草", "5");
//        f.updateAgentList("jw", "布洛卡", "5");
//        //System.out.println(f.cont);
//        ArrayList<Agent> test = f.getAgentList();
//        for(Agent a : test){
//            System.out.println(a.toString());
//        }
        //f.setRule("test",new String[][]{{"6","能天使"},{"5","布洛卡","苇草"}}, new int[]{50,50});
        for (Rule rule : f.getRule("test")) {
            System.out.println(rule.toString());
        }
    }
}
