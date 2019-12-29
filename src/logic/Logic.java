package logic;

import java.util.ArrayList;
import java.util.Random;

/**************************************************************************
 *                                                                        *
 *                         SimArknight ver 2.1                            *
 *                                                                        *
 *    Class contains the logic layer for this application.                *
 *                                                                        *
 **************************************************************************/

public class Logic {
    private int Num_Sum = 0;
    private int last_lv6 = 0;
    private double[] probs_ini = {2, 8, 50, 40};
    private double[] cur_prob = {2, 8, 50, 40};
    private static final ArrayList<Agent> agentList = new FileOpe().getAgentList();
    private ArrayList<Agent> arrayList_lv3 = new ArrayList<>(), arrayList_lv4 = new ArrayList<>(), arrayList_lv5 = new ArrayList<>(), arrayList_lv6 = new ArrayList<>();
    private int lv3 = 0, lv4 = 0, lv5 = 0, lv6 = 0;
    private int getLv3 = 0, getLv4 = 0, getLv5 = 0, getLv6 = 0;

    private boolean isChangedProb = false;

    public Logic() {
        stat();
    }

    public Logic(int l6, int l5, int l4, int l3) {
        stat();
        set_Prob(l6, l5, l4, l3);
    }

    public void set_Prob(int l6, int l5, int l4, int l3) {
        if (l6 + l5 + l4 + l3 == 100) {
            probs_ini = new double[]{l6, l5, l4, l3};
            isChangedProb = true;
        }
        if(l6==2 && l5==8 && l4==50 && l3==40)
            isChangedProb = false;
    }

    public Agent roll_lv() {
        Num_Sum++;
        last_lv6++;
        Agent agent = new Agent();
        Random rol_lev = new Random();
        //insert probability here.
        if (!isChangedProb)
            cur_prob = prob(cur_prob);
        else cur_prob = probs_ini;
        int idx_lev = rol_lev.nextInt(9999) + 1;

        if (idx_lev <= cur_prob[0] * 100)
            agent.setLevel(6);
        else if (idx_lev <= (cur_prob[0] + cur_prob[1]) * 100)
            agent.setLevel(5);
        else if (idx_lev <= (cur_prob[0] + cur_prob[1] + cur_prob[2]) * 100)
            agent.setLevel(4);
        else if (idx_lev <= (cur_prob[0] + cur_prob[1] + cur_prob[2] + cur_prob[3]) * 100)
            agent.setLevel(3);
        if (agent.getLevel() == 6) {
            last_lv6 = 0;
            if (!isChangedProb)
                cur_prob = new double[]{2, 8, 50, 40};
        }
        return roll_cr(agent);
    }

    public Agent roll_lv(String name) {
        Num_Sum++;
        last_lv6++;
        Agent agent = new Agent();
        Random rol_lev = new Random();
        //insert probability here.
        if (!isChangedProb)
            cur_prob = prob(cur_prob);
        else cur_prob = probs_ini;
        int idx_lev = rol_lev.nextInt(9999) + 1;

        if (idx_lev <= cur_prob[0] * 100)
            agent.setLevel(6);
        else if (idx_lev <= (cur_prob[0] + cur_prob[1]) * 100)
            agent.setLevel(5);
        else if (idx_lev <= (cur_prob[0] + cur_prob[1] + cur_prob[2]) * 100)
            agent.setLevel(4);
        else if (idx_lev <= (cur_prob[0] + cur_prob[1] + cur_prob[2] + cur_prob[3]) * 100)
            agent.setLevel(3);
        if (agent.getLevel() == 6) {
            last_lv6 = 0;
            if (!isChangedProb)
                cur_prob = new double[]{2, 8, 50, 40};
        }
        return roll_cr(agent, name);
    }

    private Agent roll_cr(Agent agent) {
        Random rolCr = new Random();
        if (agent.getLevel() == 3) {
            getLv3++;
            int idx_cr = rolCr.nextInt(lv3);
            agent = arrayList_lv3.get(idx_cr);
        }
        if (agent.getLevel() == 4) {
            getLv4++;
            int idx_cr = rolCr.nextInt(lv4);
            agent = arrayList_lv4.get(idx_cr);
        }
        if (agent.getLevel() == 5) {
            getLv5++;
            int idx_cr = rolCr.nextInt(lv5);
            agent = arrayList_lv5.get(idx_cr);
        }
        if (agent.getLevel() == 6) {
            getLv6++;
            int idx_cr = rolCr.nextInt(lv6);
            agent = arrayList_lv6.get(idx_cr);
        }
        return agent;
    }

    private Agent roll_cr(Agent agent, String name) {
        ArrayList<Rule> rules = new FileOpe().getRule(name);
        ArrayList<Agent> upAgents = new ArrayList<>();
        ArrayList<Agent> upOthers = new ArrayList<>();
        ArrayList<Agent> pickedUp = new ArrayList<>();
        int up_rate = 0;
        Random upI = new Random();
        for (Rule r : rules) {
            if (r.getLevel() == agent.getLevel()) {
                up_rate = r.getUp();
                if (agent.getLevel() == 3)
                    pickedUp = arrayList_lv3;
                if (agent.getLevel() == 4)
                    pickedUp = arrayList_lv4;
                if (agent.getLevel() == 5)
                    pickedUp = arrayList_lv5;
                if (agent.getLevel() == 6)
                    pickedUp = arrayList_lv6;
                for (Agent upAgent : pickedUp){
                    for(String pickName : r.names){
                        if(upAgent.getName().equals(pickName))
                            upAgents.add(upAgent);
                        else
                            upOthers.add(upAgent);
                    }
                }
            }
        }
        int upNums = upI.nextInt(99);
        Random rolCr = new Random();
        if(upAgents.size()>0){
            if (agent.getLevel() == 3) {
                getLv3++;
                agent = roll_up(upAgents,upOthers,agent, up_rate);
            }
            if (agent.getLevel() == 4) {
                getLv4++;
                agent = roll_up(upAgents,upOthers,agent, up_rate);
            }
            if (agent.getLevel() == 5) {
                getLv5++;
                agent = roll_up(upAgents,upOthers,agent, up_rate);
            }
            if (agent.getLevel() == 6) {
                getLv6++;
                agent = roll_up(upAgents,upOthers,agent, up_rate);
            }
        }else{
            agent = roll_cr(agent);
        }
        return agent;
    }

    private Agent roll_up(ArrayList<Agent> upAgents, ArrayList<Agent> upOthers, Agent agent, int up){
        Random upI = new Random();
        Random rolCr = new Random();
        int upNums = upI.nextInt(99)+1;
        if(upNums <= up){
            int idx_cr = rolCr.nextInt(upAgents.size());
            agent = upAgents.get(idx_cr);
        }
        else{
            int idx_cr = rolCr.nextInt(upOthers.size());
            agent = upOthers.get(idx_cr);
        }
        return agent;
    }

    private double[] prob(double[] probs) {
        if (last_lv6 > 50) {
            int p_up = (last_lv6 - 49) * 2;
            double p_up_ind = (p_up - probs[0]) / (probs[1] + probs[2] + probs[3]);
            probs[0] = p_up;
            probs[1] = Math.round((probs[1] - p_up_ind * probs[1]) * 1000.0) / 1000.0;
            probs[2] = Math.round((probs[2] - p_up_ind * probs[2]) * 1000.0) / 1000.0;
            probs[3] = Math.round((probs[3] - p_up_ind * probs[3]) * 1000.0) / 1000.0;
        }
        return probs;
    }

    private void stat() {
        for (Agent a : agentList) {
            if (a.getLevel() == 3) {
                lv3++;
                arrayList_lv3.add(a);
            }
            if (a.getLevel() == 4) {
                lv4++;
                arrayList_lv4.add(a);
            }
            if (a.getLevel() == 5) {
                lv5++;
                arrayList_lv5.add(a);
            }
            if (a.getLevel() == 6) {
                lv6++;
                arrayList_lv6.add(a);
            }
        }
    }

    public void reSet() {
        Num_Sum = 0;
        last_lv6 = 0;
        getLv3 = 0;
        getLv4 = 0;
        getLv5 = 0;
        getLv6 = 0;
    }

    public double[] getStat() {
        return new double[]{getLv3, getLv4, getLv5, getLv6, Num_Sum, last_lv6, cur_prob[0], cur_prob[1], cur_prob[2], cur_prob[3]};
    }

    public double[] getInitProb() {
        return new double[]{cur_prob[0], cur_prob[1], cur_prob[2], cur_prob[3]};
    }

    public double[] getCost() {
        double[] costs = new double[3];
        costs[0] = Num_Sum * 600;
        costs[1] = Math.round((costs[0] / 180) * 100.0) / 100.0;
        costs[2] = Num_Sum * 20;
        return costs;
    }

    public static void main(String[] args) {
        Logic l = new Logic();
        l.set_Prob(100,0,0,0);
        for (int i = 0; i < 100; i++) {
            System.out.println(l.roll_lv("洁哥不要啊啊啊啊").toString() + " " + l.last_lv6 + " " + l.cur_prob[0] + " " + l.cur_prob[1] + " " + l.cur_prob[2] + " " + l.cur_prob[3]);
        }
        //System.out.println(l.Num_Sum);
        //System.out.println(l.getLv3 + " " + l.getLv4 + " " + l.getLv5 + " " + l.getLv6 + " ");
        //System.out.println(l.getCost()[0] + " " + l.getCost()[1] + " " + l.getCost()[2]);
    }
}
//TODO: handel the data from Rules and add the rule into the roll agents.