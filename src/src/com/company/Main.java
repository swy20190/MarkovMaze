package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Main {


    public static void main(String[] args) {
        JFrame mainWindow = new JFrame("Markov Show");
        mainWindow.setSize(500,700);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);

        GridLayout layout = new GridLayout(8,5);
        JPanel panel = new JPanel(layout);

        ArrayList<JLabel> grids = new ArrayList<>();
        for(int i=0;i<25;i++){
            JLabel label = new JLabel(i+"",SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setBackground(Color.white);
            label.setOpaque(true);
            grids.add(label);
        }
        int[] startIndex = {0};
        grids.get(0).setBackground(Color.BLUE);//start
        grids.get(8).setBackground(Color.YELLOW);//Gold!
        grids.get(15).setBackground(Color.GRAY);
        grids.get(17).setBackground(Color.GRAY);
        grids.get(22).setBackground(Color.GRAY);
        grids.get(24).setBackground(Color.RED);//end
        for(int i=0;i<25;i++){
            panel.add(grids.get(i));
        }
        JButton next = new JButton("NEXT");
        JLabel blank = new JLabel("");
        JLabel nnn = new JLabel("N");
        JLabel equals = new JLabel("=");
        next.setEnabled(false);


        //counters
        int[] count={0};
        JLabel showN = new JLabel(count[0]+"");
        panel.add(next);
        panel.add(blank);
        panel.add(nnn);
        panel.add(equals);
        panel.add(showN);
        int[] stepCount = {0};

        //gamma
        double[] gammaa = {0.25};

        //maze editor
        JButton edit = new JButton("APPLY");
        JLabel blank1 = new JLabel("");
        JTextField editX = new JTextField("enter x");
        JTextField editY = new JTextField("enter y");
        JTextField editAttr = new JTextField("enter attribute");
        JButton start = new JButton("PLAY");
        JButton restart = new JButton("REPLAY");
        JButton showRoute = new JButton("SHOW ROUTE");
        JLabel totalStep = new JLabel("Total steps:");
        JLabel stepCountShow  =new JLabel(stepCount[0]+"");
        panel.add(edit);
        panel.add(blank1);
        panel.add(editX);
        panel.add(editY);
        panel.add(editAttr);
        panel.add(start);
        panel.add(restart);
        panel.add(showRoute);
        panel.add(totalStep);
        panel.add(stepCountShow);


        double[] values = new double[25];
        double[] rewards = new double[25];//奖励值
        for(int i=0;i<25;i++)
            values[i] = 0;
        rewards[8]=0.25;//gold
        rewards[15]=-1;
        rewards[17]=-1;
        rewards[22]=-1;
        rewards[24]=1;//end
        for(int i=0;i<25;i++){
            if(rewards[i]==1||rewards[i]==-1){
                grids.get(i).setText(rewards[i]+"");
            }else {
                grids.get(i).setText(values[i] + "");
            }
        }



        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = Integer.parseInt(editX.getText());
                int y = Integer.parseInt(editY.getText());
                int attr = Integer.parseInt(editAttr.getText());//0 for empty,1 for enter,2 for exit, 3 for gold, 4 for hole, 5 for stone
                switch (attr){
                    case 0:
                        values[x*5+y]=0;
                        rewards[x*5+y]=0;
                        grids.get(x*5+y).setBackground(Color.white);
                        break;
                    case 1:
                        values[x*5+y]=0;
                        rewards[x*5+y]=0;
                        grids.get(x*5+y).setBackground(Color.BLUE);
                        startIndex[0]=x*5+y;                      //there should be only one start!
                        break;
                    case 2:
                        values[x*5+y]=0;
                        rewards[x*5+y]=1;
                        grids.get(x*5+y).setBackground(Color.RED);
                        break;
                    case 3:
                        values[x*5+y]=0;
                        rewards[x*5+y]=0.25;
                        grids.get(x*5+y).setBackground(Color.YELLOW);
                        break;
                    case 4:
                        values[x*5+y]=0;
                        rewards[x*5+y]=-1;
                        grids.get(x*5+y).setBackground(Color.GRAY);
                        break;
                    case 5:
                        values[x*5+y]=2;
                        rewards[x*5+y]=2;
                        grids.get(x*5+y).setBackground(Color.BLACK);
                    default:
                        break;
                }
                for(int i=0;i<25;i++)
                    if(rewards[i]==1||rewards[i]==-1)
                        grids.get(i).setText(rewards[i]+"");
                    else {
                        grids.get(i).setText(values[i] + "");
                    }
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit.setEnabled(false);
                next.setEnabled(true);
            }
        });



        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //CalcuNext cal = new CalcuNext();

                nextValues(values,rewards,gammaa[0]);
                for(int i=0;i<25;i++)
                    if(rewards[i]==1||rewards[i]==-1){
                        grids.get(i).setText(rewards[i]+"");
                    }else {
                        grids.get(i).setText(values[i] + "");
                    }
                count[0]++;
                showN.setText(count[0]+"");
            }
        });

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<25;i++){
                    values[i]=0;
                    rewards[i]=0;
                    grids.get(i).setBackground(Color.white);
                    grids.get(i).setText("0.0");
                }
                count[0]=0;
                stepCount[0]=0;
                showN.setText(count[0]+"");
                stepCountShow.setText(stepCount[0]+"");
                edit.setEnabled(true);
                next.setEnabled(false);
            }
        });

        showRoute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepCount[0]=0;
                for(int i=0;i<25;i++)
                    if(rewards[i]==1||rewards[i]==-1)
                        grids.get(i).setText(rewards[i]+"");
                    else {
                        grids.get(i).setText(values[i] + "");
                    }
                int k = startIndex[0];
                double gamma = gammaa[0];
                while(rewards[k]!=1){   //not end!
                    stepCount[0]++;
                    int step = stepCount[0];
                    grids.get(k).setText(step+"");
                    if(rewards[k]==-1)
                        k=startIndex[0];  //dead!
                    else{ //not dead!
                        double up = 0.0;
                        double down = 0.0;
                        double left = 0.0;
                        double right = 0.0;
                        int i = k/5;
                        int j = k%5;
                        //up
                        if(checkBorder(i-1,j-1)&&rewards[k-6]!=2) {
                            up += 0.2 * (gamma*values[k - 6]+rewards[k-6]);
                            left+=0.2*(gamma*values[k-6]+rewards[k-6]);
                        }
                        else {
                            up += 0.2 * (gamma*values[k]+rewards[k]);
                            left+=0.2*(gamma*values[k]+rewards[k]);
                        }
                        if(checkBorder(i-1,j)&&rewards[k-5]!=2)
                            up+=0.6*(gamma*values[k-5]+rewards[k-5]);
                        else
                            up+=0.6*(gamma*values[k]+rewards[k]);
                        if(checkBorder(i-1,j+1)&&rewards[k-4]!=2) {
                            up += 0.2 * (gamma*values[k - 4]+rewards[k-4]);
                            right+=0.2*(gamma*values[k-4]+rewards[k-4]);
                        }
                        else {
                            up += 0.2 * (gamma*values[k]+rewards[k]);
                            right+=0.2*(gamma*values[k]+rewards[k]);
                        }
                        //down
                        if(checkBorder(i+1,j-1)&&rewards[k+4]!=2) {
                            down += 0.2 * (gamma*values[k + 4]+rewards[k+4]);
                            left+=0.2*(gamma*values[k+4]+rewards[k+4]);
                        }
                        else {
                            down += 0.2 * (gamma*values[k]+rewards[k]);
                            left+=0.2*(gamma*values[k]+rewards[k]);
                        }
                        if(checkBorder(i+1,j)&&rewards[k+5]!=2)
                            down+=0.6*(gamma*values[k+5]+rewards[k+5]);
                        else
                            down+=0.6*(gamma*values[k]+rewards[k]);
                        if(checkBorder(i+1,j+1)&&rewards[k+6]!=2) {
                            down += 0.2 * (gamma*values[k + 6]+rewards[k+6]);
                            right+=0.2*(gamma*values[k+6]+rewards[k+6]);
                        }
                        else {
                            down += 0.2 * (gamma*values[k]+rewards[k]);
                            right+=0.2*(gamma*values[k]+rewards[k]);
                        }
                        //left
                        if(checkBorder(i,j-1)&&rewards[k-1]!=2)
                            left+=0.6*(gamma*values[k-1]+rewards[k-1]);
                        else
                            left+=0.6*(gamma*values[k]+rewards[k]);
                        //right
                        if(checkBorder(i,j+1)&&rewards[k+1]!=2)
                            right+=0.6*(gamma*values[k+1]+rewards[k+1]);
                        else
                            right+=0.6*(gamma*values[k]+rewards[k]);
                        double maxReward = maxInFour(up,down,right,left);
                        if(maxReward==up){   //go up
                            double seed = Math.random();
                            if(seed<0.2){
                                if(checkBorder(i-1,j-1)&&rewards[k-6]!=2)
                                    k=k-6;
                            }else if(seed<0.8){
                                if(checkBorder(i-1,j)&&rewards[k-5]!=2)
                                    k=k-5;
                            }else{
                                if(checkBorder(i-1,j+1)&&rewards[k-4]!=2)
                                    k=k-4;
                            }
                        }
                        else if(maxReward==down){  //go down
                            double seed = Math.random();
                            if(seed<0.2){
                                if(checkBorder(i+1,j-1)&&rewards[k+4]!=2)
                                    k=k+4;
                            }else if(seed<0.8){
                                if(checkBorder(i+1,j)&&rewards[k+5]!=2)
                                    k=k+5;
                            }else{
                                if(checkBorder(i+1,j+1)&&rewards[k+6]!=2)
                                    k=k+6;
                            }
                        }
                        else if(maxReward==left){  //go left
                            double seed = Math.random();
                            if(seed<0.2){
                                if(checkBorder(i-1,j-1)&&rewards[k-6]!=2)
                                    k=k-6;
                            }else if(seed<0.8){
                                if(checkBorder(i,j-1)&&rewards[k-1]!=2)
                                    k=k-1;
                            }else{
                                if(checkBorder(i+1,j-1)&&rewards[k+4]!=2)
                                    k=k+4;
                            }
                        }
                        else{  //go right
                            double seed = Math.random();
                            if(seed<0.2){
                                if(checkBorder(i-1,j+1)&&rewards[k-4]!=2)
                                    k=k-4;
                            }else if(seed<0.8){
                                if(checkBorder(i,j+1)&&rewards[k+1]!=2)
                                    k=k+1;
                            }else{
                                if(checkBorder(i+1,j+1)&&rewards[k+6]!=2)
                                    k=k+6;
                            }
                        }
                    }
                }
                stepCountShow.setText(stepCount[0]+1+"");
            }
        });

        mainWindow.setContentPane(panel);
        mainWindow.setVisible(true);
    }

   public static void nextValues(double[] values,double[] rewards,double gamma){
        double[] newVal = new double[25];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                int k = i*5+j;
                if(rewards[k]==1||rewards[k]==-1||rewards[k]==2)
                    newVal[k]=0;
                else{
                    double up=0;
                    double down=0;
                    double left = 0;
                    double right = 0;
                    //up
                    if(checkBorder(i-1,j-1)&&rewards[k-6]!=2) {
                        up += 0.2 * (gamma*values[k - 6]+rewards[k-6]);
                        left+=0.2*(gamma*values[k-6]+rewards[k-6]);
                    }
                    else {
                        up += 0.2 * (gamma*values[k]+rewards[k]);
                        left+=0.2*(gamma*values[k]+rewards[k]);
                    }
                    if(checkBorder(i-1,j)&&rewards[k-5]!=2)
                        up+=0.6*(gamma*values[k-5]+rewards[k-5]);
                    else
                        up+=0.6*(gamma*values[k]+rewards[k]);
                    if(checkBorder(i-1,j+1)&&rewards[k-4]!=2) {
                        up += 0.2 * (gamma*values[k - 4]+rewards[k-4]);
                        right+=0.2*(gamma*values[k-4]+rewards[k-4]);
                    }
                    else {
                        up += 0.2 * (gamma*values[k]+rewards[k]);
                        right+=0.2*(gamma*values[k]+rewards[k]);
                    }
                    //down
                    if(checkBorder(i+1,j-1)&&rewards[k+4]!=2) {
                        down += 0.2 * (gamma*values[k + 4]+rewards[k+4]);
                        left+=0.2*(gamma*values[k+4]+rewards[k+4]);
                    }
                    else {
                        down += 0.2 * (gamma*values[k]+rewards[k]);
                        left+=0.2*(gamma*values[k]+rewards[k]);
                    }
                    if(checkBorder(i+1,j)&&rewards[k+5]!=2)
                        down+=0.6*(gamma*values[k+5]+rewards[k+5]);
                    else
                        down+=0.6*(gamma*values[k]+rewards[k]);
                    if(checkBorder(i+1,j+1)&&rewards[k+6]!=2) {
                        down += 0.2 * (gamma*values[k + 6]+rewards[k+6]);
                        right+=0.2*(gamma*values[k+6]+rewards[k+6]);
                    }
                    else {
                        down += 0.2 * (gamma*values[k]+rewards[k]);
                        right+=0.2*(gamma*values[k]+rewards[k]);
                    }
                    //left
                    if(checkBorder(i,j-1)&&rewards[k-1]!=2)
                        left+=0.6*(gamma*values[k-1]+rewards[k-1]);
                    else
                        left+=0.6*(gamma*values[k]+rewards[k]);
                    //right
                    if(checkBorder(i,j+1)&&rewards[k+1]!=2)
                        right+=0.6*(gamma*values[k+1]+rewards[k+1]);
                    else
                        right+=0.6*(gamma*values[k]+rewards[k]);
                    newVal[k]=maxInFour(up,down,left,right);
                }
            }
        }
       System.arraycopy(newVal, 0, values, 0, 25);
   }



    public static boolean checkBorder(int i,int j){
        return 0<=i&&i<=4&&0<=j&&j<=4;
    }

    public static double maxInFour(double d1,double d2,double d3,double d4){
        double result = d1;
        if(result<d2)
            result=d2;
        if(result<d3)
            result = d3;
        if(result<d4)
            result = d4;
        return  result;
    }

}
