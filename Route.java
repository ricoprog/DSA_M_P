package com.example.rico.aitmaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Route extends AppCompatActivity {
    private TextView t1 , t2 , t3 ;
    private static final String[] add = new String[]{
            "Vishwesvariya Boys Hostel" , "Abdul Kalam Boys Hostel" , "Boys Mess" , "Cricket Ground" , "Food Court" , "Shopping Complex" , "Kalpana Hostel" , "Academic Block" , "Central Gazeebo" , "Aryabhatta Centre" , "MG ROad" , "Main Gate" , "Badminton Court" , "Open Air Cafeteria"
    };
    private static final int[] pos = new int[]{
            3 , 15 , 1 , 14 , 6 , 7 , 12 , 19 , 20 , 9 , 13 , 16 , 10 , 20
            };
    public double coordinates[][] = {{} , {18.604631, 73.876124},{18.604998, 73.875962} , {18.604829, 73.875387} , {18.605108, 73.874634} , {18.604683, 73.874594} , {18.604697, 73.874217} , {18.604551, 73.874292}, {18.606065, 73.874663} , {18.606195, 73.875055} , {18.606214, 73.876095} ,{18.606937, 73.874326} ,{18.607418, 73.874655} ,{18.605596, 73.874643} , {18.605538, 73.875372}, {18.605533, 73.876010} , {18.606975, 73.873911} , {18.606591, 73.874519} , {18.606159, 73.874927} , {18.606914, 73.874801} , {18.606326, 73.874772}};
    public int connect[][] = {{} , {2} , {3,15} , {2,14,4} , {5,13,3} , {4,6,7} , {5} , {7} , {17,18,13} , {10,18} , {9,15} , {16,17,12} , {11,19} , {8,4} , {18,15,3} , {14,10,2} , {11} , {11,8,19,20} , {9,8,20} , {17,12} , {17,18}};
    public double[][] dist = new double[21][21];
    public double[] wt = new double[21] ;
    public int n , f1 , d1 ;
    public double min ;
    public int[] parent = new int[21] ;
    public boolean[] visit = new boolean[21] ;
    public StringBuilder u = new StringBuilder()  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route2);
        t1 = (TextView) findViewById(R.id.t1) ;
        t2 = (TextView) findViewById(R.id.t2) ;
        t3 = (TextView) findViewById(R.id.t3) ;
        String fin = getIntent().getStringExtra("Start") ;
        String des = getIntent().getStringExtra("dest") ;
        for(int i =0 ;i<add.length ; i++){
            if(add[i].equals(fin)){
                f1 = pos[i] ;
            }
            if(add[i].equals(des)){
                d1 = pos[i] ;
            }
        }
        String s = Integer.toString(f1) ;
        String t = Integer.toString(d1) ;
        t1.setText(s);
        t2.setText(t);
        for(int i=0 ; i<=20 ; i++){
            wt[i] = 10000000 ;
            for(int j=0; j<=20 ; j++){
                dist[i][j] = 1000000 ;
            }
        }
        for(int i =1 ;i<=20 ; i++){
            for(int j=0 ; j<connect[i].length ; j++){
                dist[i][connect[i][j]] = haversine(coordinates[i][0] , coordinates[i][1] , coordinates[connect[i][j]][0] , coordinates[connect[i][j]][1]);
            }
        }
        wt[f1] = 0 ;
        visit[f1] = false ;
        dijkstra(f1);
        int p1 = d1 ;
        while(true){
            for(int i = 0 ;i<pos.length ; i++){
                if(pos[i]==p1){
                    StringBuilder v = new StringBuilder() ;
                    v.append(add[i]) ;
                    if(p1!=f1)
                        u.append(v.reverse()).append(">-");
                    else
                        u.append(v.reverse());
                }
            }
            if(p1==f1){
                break;
            }
            p1 = parent[p1] ;
        }
        u.reverse() ;
        t3.setText(u);
    }

    private void dijkstra(int node){
        while(node!=d1) {
            for (int i = 0; i < connect[node].length; i++) {
                if(!visit[connect[node][i]]) {
                    if(wt[connect[node][i]] > wt[node] + dist[node][connect[node][i]]){
                        wt[connect[node][i]] = wt[node] + dist[node][connect[node][i]] ;
                        parent[connect[node][i]] = node ;
                    }
                }
            }
            node = findmin() ;
            visit[node]=true ;
        }
    }

    private int findmin() {
        min=100000 ;
        for(int i=1 ; i<21 ; i++){
            if(!visit[i] && wt[i]<min){
                min = wt[i] ;
                n = i ;
            }
        }
        return n ;
    }

    private double haversine(double lat1, double long1, double lat2, double long2) {
        final int R1 = 6371 ;
        double latdist = torad(lat2 - lat1) ;
        double londist = torad(long2-long1) ;
        double a = Math.sin(latdist / 2) * Math.sin(latdist / 2) +
                Math.cos(torad(lat1)) * Math.cos(torad(lat2)) *
                        Math.sin(londist / 2) * Math.sin(londist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R1*c ;

    }
    private double torad(double v){ return v*Math.PI/180 ;}
}
