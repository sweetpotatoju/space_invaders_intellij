package spaceinvaders;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.function.Consumer;

public class Ranking extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private FirebaseTool firebaseTool;
    private GlobalStorage globalStorage;
    public Ranking() {

        setTitle("Ranking");
        setSize(500, 400);

        firebaseTool = FirebaseTool.getInstance();
        globalStorage = GlobalStorage.getInstance();
        // 랭킹 데이터 테이블 생성
        String[] columnNames = {"Rank", "ID", "Best Score"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // 컬럼 크기 자동 조정
        table.setEnabled(false); // 테이블 클릭 비활성화

        // 폰트 사이즈 설정
        table.setFont(new Font("Arial", Font.PLAIN, 16));

        // 셀들의 텍스트 정렬 설정
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // 스크롤 패널에 테이블 추가
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table); // 테이블이 스크롤 가능한 패널에 표시되도록 설정
        getContentPane().add(scrollPane, BorderLayout.CENTER); // 가운데 정렬

        // Firebase에서 랭킹 데이터 가져오기

        firebaseTool.getAllUserBestScore(new Consumer<ArrayList<HashMap<String, String>>>() {
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