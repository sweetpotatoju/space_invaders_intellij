package spaceinvaders;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.function.Consumer;

public class Ranking extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public Ranking() {
        setTitle("Ranking");
        setSize(800, 600);

        // 랭킹 데이터 테이블 생성
        String[] columnNames = {"Rank", "ID", "Best Score"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        // 스크롤 패널에 테이블 추가
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        // Firebase에서 랭킹 데이터 가져오기
        FirebaseTool firebaseTool = new FirebaseTool();
        firebaseTool.GetAllUserBestScore(new Consumer<ArrayList<HashMap<String, String>>>() {
            @Override
            public void accept(ArrayList<HashMap<String, String>> userAllBestScore) {
                // 랭킹 데이터를 테이블에 추가
                for (int i = 0; i < userAllBestScore.size(); i++) {
                    HashMap<String, String> userData = userAllBestScore.get(i);
                    String rank = Integer.toString(i + 1);
                    String id = userData.get("id");
                    String bestScore = userData.get("bestscore");

                    Vector<String> row = new Vector<String>();
                    row.add(rank);
                    row.add(id);
                    row.add(bestScore);
                    model.addRow(row);
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Ranking();
    }
}