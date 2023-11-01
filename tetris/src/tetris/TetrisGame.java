package tetris;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TetrisGame extends Applet implements Runnable,ActionListener {
    Thread clock;

    Image off;      //메모리 상의 가상화며
    Graphics offG;

    Random r;
    boolean[][] map;
    Color[] colorType;
    Color[][] colorMap;

    int blockType, blockPos, score, delayTime, runGame;
    int[] blockX, blockY;
    AudioClip turnAudio, deleteAudio, gameOverAudio;
    Button startBtn;
    Panel btnPanel;
    int gameBoardHeight,heightCell,gameBoardWeight,weightCell;
    public void init() {
        heightCell=25;      //테트리스 높이 칸 수
        weightCell=15;      //테트리스 너비 칸 수
        gameBoardWeight=weightCell*15;      //실질적인 너비 크기
        gameBoardHeight=heightCell*15;      //실질적인 높이 크기
        off = createImage(gameBoardWeight+1, gameBoardHeight+1);
        offG = off.getGraphics();
        offG.setColor(Color.white);
        offG.fillRect(0, 0, 192, 192);

        setLayout(new BorderLayout());
        btnPanel = new Panel();
        startBtn = new Button("시작");
        startBtn.addActionListener(this);
        btnPanel.add(startBtn);
        add("South", btnPanel);
        map = new boolean[weightCell][heightCell];
        colorMap = new Color[weightCell][heightCell];
        colorType = new Color[7];
        setColorType();
        blockX = new int[4];
        blockY = new int[4];
        blockPos = 0;
        r = new Random();
        blockType = Math.abs(r.nextInt() % 7);

        setBlockXY(blockType);
        drawMap();
        drawGrid();
        score = 0;
        delayTime = 1000;           //내려오는 속도 조절
        runGame = 0;
        addKeyListener(new MyKeyHandler());
    }
    //블록 컬러 셋팅하는 함수
    public void setColorType() {
        colorType[0] = new Color(65, 228, 82);
        colorType[1] = new Color(58, 98, 235);
        colorType[2] = new Color(128, 0, 64);
        colorType[3] = new Color(255, 35, 31);
        colorType[4] = new Color(68, 17, 111);
        colorType[5] = new Color(246, 118, 57);
        colorType[6] = new Color(224, 134, 4);
    }

    //초기 블록 모양 좌표 셋팅하는 함수
    public void setBlockXY(int type) {
        switch (type) {
            case 0:
                blockX[0] = 5;
                blockY[0] = 0;
                blockX[1] = 6;
                blockY[1] = 0;
                blockX[2] = 5;
                blockY[2] = 1;
                blockX[3] = 6;
                blockY[3] = 1;
                break;
            case 1:
                blockX[0] = 6;
                blockY[0] = 0;
                blockX[1] = 5;
                blockY[1] = 1;
                blockX[2] = 6;
                blockY[2] = 1;
                blockX[3] = 7;
                blockY[3] = 1;
                break;
            case 2:
                blockX[0] = 7;
                blockY[0] = 0;
                blockX[1] = 5;
                blockY[1] = 1;
                blockX[2] = 6;
                blockY[2] = 1;
                blockX[3] = 7;
                blockY[3] = 1;
                break;
            case 3:
                blockX[0] = 5;
                blockY[0] = 0;
                blockX[1] = 5;
                blockY[1] = 1;
                blockX[2] = 6;
                blockY[2] = 1;
                blockX[3] = 7;
                blockY[3] = 1;
                break;
            case 4:
                blockX[0] = 5;
                blockY[0] = 0;
                blockX[1] = 5;
                blockY[1] = 1;
                blockX[2] = 6;
                blockY[2] = 1;
                blockX[3] = 6;
                blockY[3] = 2;
                break;
            case 5:
                blockX[0] = 6;
                blockY[0] = 0;
                blockX[1] = 5;
                blockY[1] = 1;
                blockX[2] = 6;
                blockY[2] = 1;
                blockX[3] = 5;
                blockY[3] = 2;
                break;
            case 6:
                blockX[0] = 4;
                blockY[0] = 0;
                blockX[1] = 5;
                blockY[1] = 0;
                blockX[2] = 6;
                blockY[2] = 0;
                blockX[3] = 7;
                blockY[3] = 0;
                break;
        }
    }

    public void start() {
        if (clock == null) {
            clock = new Thread(this);
            clock.start();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(off, 0, 0, this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    //스레드 동작함수
    public void run() {
        while (true) {
            try {
                clock.sleep(delayTime);
            } catch (InterruptedException ie) {
            }
            dropBlock();            //1초마다 드랍시킨다.
            switch (runGame) {
                case 1:

                    drawBlock();
                    drawMap();
                    drawGrid();

                    break;
                case 2:
                    drawScore();
                    break;
                default:
                    drawTitle();
                    break;
            }
            repaint();
        }
    }

    public void drawScore() {
        offG.setColor(Color.WHITE);
        offG.fillRect(35, 120, 110, 70);
        offG.setColor(Color.BLACK);
        offG.drawRect(40, 125, 100, 60);
        offG.drawString("Game Over !!", 65, 150);
        offG.setColor(Color.blue);
        offG.drawString("Score : " + score, 56, 170);
    }

    public void drawTitle() {
        offG.setColor(Color.WHITE);
        offG.fillRect(45, 100, 135, 220);
        offG.setColor(Color.BLACK);
        offG.drawRect(45, 100, 136, 221);
        offG.setColor(Color.red);
        offG.drawString("테트리스!!", 80, 120);
        offG.setColor(Color.blue);
        offG.drawString("스타트 버튼을 " , 70, 150);
        offG.setColor(Color.blue);
        offG.drawString("눌러주세요.!",80,170);

        offG.setColor(Color.BLACK);
        offG.drawString("키 조작법",80,190);
        offG.drawString("↑  :블록 모양 변경",60,210);
        offG.drawString("←  :블록 왼쪽 이동",60,230);
        offG.drawString("→  :블록 오른쪽 이동",60,250);
        offG.drawString("↓  :블록 아래 이동",60,270);
        offG.drawString("space  :블록 떨구기",60,290);

    }

    //한칸 떨어뜨려주는 함수
    public void dropBlock() {
        removeBlock();
        if (checkDrop() == true) {            //떨어지는게 가능하다면 한칸씩 밑으로
            for (int i = 0; i < 4; i++) {
                blockY[i] = blockY[i] + 1;
            }
        } else if (checkDrop() == false) {          //불가능하다면 다음블록을 그린다
            drawBlock();
            nextBlock();
        }
    }
    //줄 지우는 함수
    public void delLine() {
        boolean delOK;
        for (int row = heightCell-1; row >= 0; row--) {
            delOK = true;
            for (int col = 0; col < weightCell; col++) {
                if (!map[col][row]) delOK = false;
            }
            if (delOK) {
                score += 10;
                if (score < 1000) {
                    delayTime = 1000 - score;
                } else {
                    delayTime = 0;
                }

            for (int delRow = row; delRow > 0; delRow--) {
                for (int delCol = 0; delCol < weightCell; delCol++) {
                    map[delCol][delRow] = map[delCol][delRow - 1];
                    colorMap[delCol][delRow] = colorMap[delCol][delRow - 1];
                }
            }
            for (int i = 0; i < weightCell; i++) {
                map[0][i] = false;
                colorMap[0][i] = Color.white;
            }
            row++;
        }
    }
}
    //다음 블록 랜덤으로 생성해주는 함수
    public void nextBlock(){
        blockType=Math.abs(r.nextInt()%7);
        blockPos=0;
        delLine();
        setBlockXY(blockType);
        checkGameOver();
    }
    public void checkGameOver(){
        for(int i=0;i<4;i++){
            if(map[blockX[i]][blockY[i]]){
                if(runGame==1){
                    runGame=2;
                }
            }
        }
    }
    //맵에서 블록 제거해주는 함수
    public void removeBlock(){
        for(int i=0;i<4;i++){
            map[blockX[i]][blockY[i]]=false;
            colorMap[blockX[i]][blockY[i]]=Color.white;
        }
    }
    //한칸 씩 내려오는거 체크해주는 함수
    public boolean checkDrop(){
        boolean dropOK=true;
        for(int i=0;i<4;i++){
            if((blockY[i]+1)!=heightCell){          //맨 밑에가 아니고
              if(map[blockX[i]][blockY[i]+1]==true) { //한칸아래에 블록이 있다면
                    dropOK = false; //이동을 못하닌까 false 반환
                }
            }else{  //맨 밑에라면
                dropOK=false;   //이동을 못하닌까 false 반환
            }
        }
        return dropOK;
    }
    //스페이스 눌렀을 시 이동가능한지 체크해주는 함수
    public boolean checkDrop(int moveY){
        boolean dropOK=false;
        boolean[] check;
        check=new boolean[4];
        for(int i=0;i<4;i++){
            if((blockY[i]+moveY)<heightCell) {          //맨 밑에가 아니고
                if (map[blockX[i]][blockY[i]+moveY] == true) {    //이동할려는 곳에 블록이 있다면
                    check[i] = false;
                } else {     //없다면
                    check[i] = true;
                }
            }
        }
        if(check[0]==true&&check[1]==true&&check[2]==true&&check[3]==true){
            return true;
        }else{
            return false;
        }
    }
    //블록을 그리는 함수
    public void drawBlock(){
        for(int i=0;i<4;i++){
            map[blockX[i]][blockY[i]]=true;
            colorMap[blockX[i]][blockY[i]]=colorType[blockType];
        }
    }
    //맵그리는 함수
    public void drawMap(){
        for(int i=0;i<weightCell;i++){
            for(int j=0;j<heightCell;j++){
                if(map[i][j]){
                    offG.setColor(colorMap[i][j]);
                    offG.fillRect(i*15,j*15,15,15);
                }else{
                    offG.setColor(Color.white);
                    offG.fillRect(i*15,j*15,15,15);
                }
            }
        }
    }
    public void drawGrid(){
        offG.setColor(new Color(190,190,190));
        for(int i=0;i<weightCell;i++){
            for(int j=0;j<heightCell;j++){
                offG.drawRect(i*15,j*15,15,15);
            }
        }
    }
    public void stop(){
        if((clock!=null)&&(clock.isAlive())){
            clock=null;
        }
    }

    public void actionPerformed(ActionEvent e){
        blockPos=0;
        for(int i=0;i<weightCell;i++){
            for(int j=0;j<heightCell;j++){
                map[i][j]=false;
            }
        }
        r=new Random();
        blockType=Math.abs(r.nextInt()%7);
        setBlockXY(blockType);
        drawBlock();
        drawMap();
        drawGrid();
        score=0;
        delayTime=1000;
        runGame=1;
        this.requestFocus();
    }

    // 키 이벤트 핸들러 클래스 구현
    class MyKeyHandler extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            int keyCode=(int)e.getKeyCode();
            if(keyCode==KeyEvent.VK_LEFT){
                if(checkMove(-1)){
                    for(int i=0;i<4;i++){
                        blockX[i]=blockX[i]-1;
                    }
                }
            }
            if(keyCode==KeyEvent.VK_RIGHT){
                if(checkMove(1)){
                    for(int i=0;i<4;i++){
                        blockX[i]=blockX[i]+1;
                    }
                }
            }
            if(keyCode==KeyEvent.VK_DOWN){
                removeBlock();
                if(checkDrop()==true){            //트루를 반환하면 한칸씩 이동
                    for(int i=0;i<4;i++){
                        blockY[i]=blockY[i]+1;
                    }
                }else{
                    drawBlock();
                }
            }

            if(keyCode==KeyEvent.VK_UP){
                int[] tempX=new int[4];
                int[] tempY=new int[4];
                for(int i=0;i<4;i++){
                    tempX[i]=blockX[i];
                    tempY[i]=blockY[i];
                }
                removeBlock();          //블록을 제거하고
                turnBlock();            //블록을 변경한다
                if(checkTurn()){
                    if(blockPos<4){
                        blockPos++;
                    }else{
                        blockPos=0;
                    }
                }else{
                    for(int i=0;i<4;i++){
                        blockX[i]=tempX[i];
                        blockY[i]=tempY[i];
                        map[blockX[i]][blockY[i]]=true;
                        colorMap[blockX[i]][blockY[i]]=colorType[blockType];
                    }
                }
            }
            if(keyCode==KeyEvent.VK_SPACE) {
                int[] tempX=new int[4];
                int[] tempY=new int[4];
                for(int i=0;i<4;i++){
                    tempX[i]=blockX[i];
                    tempY[i]=blockY[i];
                }
                removeBlock();
                for(int i=heightCell;i>0;i--){
                    if(checkDrop(i)==true) {
                        for(int j=0;j<4;j++){
                            blockY[j]+=i;
                        }
                        break;
                    }
                }
            }

            drawBlock();
            drawMap();
            drawGrid();
            repaint();
        }
        //한칸씩 내려갈때 이동이 가능한지 체크하는 함수
        public boolean checkTurn(){
            boolean turnOK=true;
            for(int i=0;i<4;i++){
                if((blockX[i]>0)&&(blockX[i]<weightCell)&&(blockY[i]>=0)&&(blockY[i]<heightCell)){  //블록이 맵 안에 있으면
                    if(map[blockX[i]][blockY[i]])
                        turnOK=false;
                }else{
                    turnOK=false;
                }
            }
            return turnOK;
        }
        // 블록 모양 변경시 이동가능한지 체크해주는 함수
        public boolean checkMove(int dir){
            boolean moveOK=true;
            removeBlock();
            for(int i=0;i<4;i++){
                if(((blockX[i]+dir)>=0)&&((blockX[i]+dir)<weightCell)){
                    if(map[blockX[i]+dir][blockY[i]])
                        moveOK=false;
                }else{
                    moveOK=false;
                }
            }
            if(!moveOK)
                drawBlock();
            return moveOK;
        }
        //블록 모양 변경하기 좌표 설정 함수
        public void turnBlock(){
            switch (blockType){
                case 1:
                    switch (blockPos){
                        case 0:
                            blockX[0]=blockX[0];blockY[0]=blockY[0];
                            blockX[1]=blockX[1];blockY[1]=blockY[1];
                            blockX[2]=blockX[2];blockY[2]=blockY[2];
                            blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
                            break;
                        case 1:
                            blockX[0]=blockX[0]-1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]+1;blockY[1]=blockY[1]-1;
                            blockX[2]=blockX[2]+1;blockY[2]=blockY[2]-1;
                            blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
                            break;
                        case 2:
                            blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1];blockY[1]=blockY[1]+1;
                            blockX[2]=blockX[2];blockY[2]=blockY[2]+1;
                            blockX[3]=blockX[3];blockY[3]=blockY[3]+1;
                            break;
                        case 3:
                            blockX[0]=blockX[0];blockY[0]=blockY[0];
                            blockX[1]=blockX[1]-1;blockY[1]=blockY[1];
                            blockX[2]=blockX[2]-1;blockY[2]=blockY[2];
                            blockX[3]=blockX[3]+1;blockY[3]=blockY[3]-1;
                            break;
                    }
                    break;

                case 2:
                    switch (blockPos){
                        case 0:
                            blockX[0]=blockX[0]-2;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]+1;blockY[1]=blockY[1]-1;
                            blockX[2]=blockX[2];blockY[2]=blockY[2];
                            blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
                            break;
                        case 1:
                            blockX[0]=blockX[0];blockY[0]=blockY[0];
                            blockX[1]=blockX[1];blockY[1]=blockY[1];
                            blockX[2]=blockX[2]+1;blockY[2]=blockY[2]-1;
                            blockX[3]=blockX[3]-1;blockY[3]=blockY[3]-1;
                            break;
                        case 2:
                            blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1];blockY[1]=blockY[1]+1;
                            blockX[2]=blockX[2]-1;blockY[2]=blockY[2]+2;
                            blockX[3]=blockX[3]+2;blockY[3]=blockY[3]+1;
                            break;
                        case 3:
                            blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]-1;blockY[1]=blockY[1];
                            blockX[2]=blockX[2];blockY[2]=blockY[2]-1;
                            blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
                            break;
                    }
                    break;
                case 3:
                    switch (blockPos){
                        case 0:
                            blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]+1;blockY[1]=blockY[1];
                            blockX[2]=blockX[2]-1;blockY[2]=blockY[2]+1;
                            blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
                            break;
                        case 1:
                            blockX[0]=blockX[0]-2;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]-1;blockY[1]=blockY[1]-1;
                            blockX[2]=blockX[2]+1;blockY[2]=blockY[2]-2;
                            blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
                            break;
                        case 2:
                            blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]+1;blockY[1]=blockY[1];
                            blockX[2]=blockX[2]-1;blockY[2]=blockY[2]+1;
                            blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
                            break;
                        case 3:
                            blockX[0]=blockX[0];blockY[0]=blockY[0];
                            blockX[1]=blockX[1]-1;blockY[1]=blockY[1]+1;
                            blockX[2]=blockX[2]+1;blockY[2]=blockY[2];
                            blockX[3]=blockX[3]+2;blockY[3]=blockY[3]-1;
                            break;
                    }
                    break;
                case 4:
                    switch (blockPos){
                        case 0:
                        case 2:
                            blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]+2;blockY[1]=blockY[1]-1;
                            blockX[2]=blockX[2]-1;blockY[2]=blockY[2];
                            blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
                            break;
                        case 1:
                        case 3:
                            blockX[0]=blockX[0]-1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]-2;blockY[1]=blockY[1]+1;
                            blockX[2]=blockX[2]+1;blockY[2]=blockY[2];
                            blockX[3]=blockX[3];blockY[3]=blockY[3]+1;
                            break;
                    }
                    break;
                case 5:
                    switch (blockPos){
                        case 0:
                        case 2:
                            blockX[0]=blockX[0]-1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]+1;blockY[1]=blockY[1]-1;
                            blockX[2]=blockX[2];blockY[2]=blockY[2];
                            blockX[3]=blockX[3]+2;blockY[3]=blockY[3]-1;
                            break;
                        case 1:
                        case 3:
                            blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]-1;blockY[1]=blockY[1]+1;
                            blockX[2]=blockX[2];blockY[2]=blockY[2];
                            blockX[3]=blockX[3]-2;blockY[3]=blockY[3]+1;
                            break;
                    }
                    break;
                case 6:
                    switch (blockPos){
                        case 0:
                        case 2:
                            blockX[0]=blockX[0]+2;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]+1;blockY[1]=blockY[1]+1;
                            blockX[2]=blockX[2];blockY[2]=blockY[2]+2;
                            blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+3;
                            break;
                        case 1:
                        case 3:
                            blockX[0]=blockX[0]-2;blockY[0]=blockY[0];
                            blockX[1]=blockX[1]-1;blockY[1]=blockY[1]-1;
                            blockX[2]=blockX[2];blockY[2]=blockY[2]-2;
                            blockX[3]=blockX[3]+1;blockY[3]=blockY[3]-3;
                            break;
                    }
                    break;
            }
        }
    }
}
