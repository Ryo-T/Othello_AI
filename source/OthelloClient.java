import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class OthelloClient extends JFrame{
    final static int BLACK = 1;
    final static int WHITE = -1;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;    
    private byte color;
    private byte[][] board = new byte[8][8];
    private JTextField tf;
    private JTextArea ta;
    private JLabel label;
    private OthelloCanvas canvas;
    private String username = "";
    private int x = 0;
    private int y = 0;
    private int point = 9;
             
    public OthelloClient(String host, int port, String username) {
	this.username = username;
	//setTitle(username);
	try{
	    socket = new Socket(host,port);
	    br = new BufferedReader(new InputStreamReader
	        		    (socket.getInputStream()));
	    pw = new PrintWriter(socket.getOutputStream());
	}catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
	addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent e)  {
                  System.exit(0);
              }
         });	    
	tf = new JTextField(40);
	ta = new JTextArea(18,40);
	ta.setLineWrap(true);
	ta.setEditable(false);
	label = new JLabel();

	this.setSize(640,320);
		    
	tf.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    if(tf.getText().equals("quit")){
			System.exit(0);
		    }
		    //sayMessage(tf.getText());
		    pw.println("SAY "+tf.getText());
		    pw.flush();
		    tf.setText("");
		}
	    }
	    );
	
	JPanel mainp = (JPanel)getContentPane();
	JPanel ep = new JPanel();
	JPanel wp = new JPanel();
	canvas = new OthelloCanvas(this);
	
	GridLayout gl = new GridLayout(1,2);
	gl.setHgap(5);
	mainp.setLayout(gl);
	ep.setLayout(new BorderLayout());
	ep.add(new JScrollPane(ta),BorderLayout.CENTER);
	ep.add(tf,BorderLayout.SOUTH);
	wp.setLayout(new BorderLayout());
	wp.add(label,BorderLayout.SOUTH);
	wp.add(canvas,BorderLayout.CENTER);
	mainp.add(wp);
	mainp.add(ep);

	setVisible(true);
	
	mainLoop();
    }

    void putPiece(int x, int y){
	pw.println("PUT "+x+" "+y);
	pw.flush();
    }

    byte[][] getBoard(){
	return board;
    }

    private void mainLoop(){
	try{
	    pw.println("NICK "+username);
	    pw.flush();
	    StringTokenizer stn = new StringTokenizer(br.readLine()," ",false);
	    stn.nextToken(); //START message
	    color = Byte.parseByte(stn.nextToken());
	    if(color==BLACK){
		setTitle(username+"(BLACK)");
	    }else{
		setTitle(username+"(WHITE)");
	    }

            talk(0);

	    while(true){
		String message = br.readLine();
		stn = new StringTokenizer(message," ",false);
		String com = stn.nextToken();

		if(com.equals("SAY")){
		    System.out.println(message);
		    setMessage(message.substring(4));
		    continue;
		}
		
		if(com.equals("BOARD")){
		    for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
			    board[i][j] = Byte.parseByte(stn.nextToken());
			}
		    }
		    canvas.repaint();
		    continue;
		}if(com.equals("END")){
		    label.setText(message);
		    setMessage("==System==:"+message);
                    talk(4);
		    continue;
		}if(com.equals("CLOSE")){
		    setMessage("==System==:"+message);
		    label.setText(message);
		    return;

		}if(com.equals("TURN")){
		    byte c = Byte.parseByte(stn.nextToken());
		    if(c==color){
			label.setText("Your Turn");
                        //----------------------------------
                        // write my AI process
                        //----------------------------------
                        //sleeps(3);
                        ai_statistics_2_reset();
                        ai_statistics_2();
                        talk(1);
                        //----------------------------------
		    }else{
			label.setText("Enemy Turn");
		    }
		    continue;
		}if(com.equals("ERROR")){
                    com = stn.nextToken();
		    //if(com.equals("1")) continue;
                    //if(com.equals("2")) continue;
                    if(com.equals("3")) continue;
                    if(com.equals("4")) continue;
                    ai_statistics_2();
                }
		
		//System.out.println(message);
	    }
	}catch(IOException e){
	    System.exit(0);
	}
    }

    private void setMessage(String str){
	ta.append(str+"\n");
	int len = ta.getText().length();
	ta.setCaretPosition(len); 
    }

    private void talk(int status){
        TalkText tt = new TalkText(); 
        Random rand = new Random();
        int randomNumber = rand.nextInt(11);

        if(username.equals("tnb")){
            if(status==0){
                pw.println("SAY "+tt.tnb[0][rand.nextInt(9)]);
                pw.flush();
            }else if(value()==1){
                pw.println("SAY "+tt.tnb[1][rand.nextInt(9)]);
                pw.flush();
            }else if(value()==2){
                pw.println("SAY "+tt.tnb[2][rand.nextInt(9)]);
                pw.flush();
            }else if(value()==3){
                pw.println("SAY "+tt.tnb[3][rand.nextInt(9)]);
                pw.flush();
            }else if(status==4){
                pw.println("SAY "+tt.tnb[4][rand.nextInt(9)]);
                pw.flush();
            }else{
                System.out.println("talk ERROR");
            }
       }else if(username.equals("kusoanime")){
            for(int i=0;i<10;i++){
                pw.println("SAY "+tt.kusoanime[i]);
                pw.flush();
            }
        }else if(username.equals("shacho")){
            for(int i=0;i<15;i++){
                pw.println("SAY "+tt.shacho[i]);
                pw.flush();
            }
        }else if(username.equals("maccho")){
            for(int i=0;i<15;i++){
                pw.println("SAY "+tt.maccho[i]);
                pw.flush();
            }
        }else{
            pw.println("SAY HELLO! I'm etc");
            pw.flush();
        }
        sleeps(3);
    }


    private int value(){
        int white_pice = 0;
        int black_pice = 0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(board[i][j]==-1){
                    white_pice++;
                }else if(board[i][j]==1){
                    black_pice++;
                }
            }
        }
        int sum = white_pice + black_pice;
        if(sum>20){
            if(color==WHITE){
                if(white_pice > sum/2){
                    return 2;
                }
                return 3;
            }

            if(color==BLACK){
                if(black_pice > sum/2){
                    return 2;
                }
                return 3;
            }
        }
        return 1;
    }

    //----------------------------------
    // Sleep method
    //----------------------------------
    private void sleeps(int sec){
        try {
            //System.out.println("一時停止します");
            Thread.sleep(sec * 1000);
            //System.out.println("一時停止を解除しました。");
        } catch(InterruptedException e){
            e.printStackTrace();
        } 
    }

    //----------------------------------
    // Static PUT method 1
    //----------------------------------
    private void ai_statistics(){
        StringTokenizer stn;
        String com;
        String err_num;
	for(int i=0;i<9;i++){
           for(int j=0;j<9;j++){
                try{
	            pw.println("PUT "+i+" "+j);
	            pw.flush();

	            stn = new StringTokenizer(br.readLine()," ",false);
	            com = stn.nextToken();
		    if(com.equals("ERROR")){
	                err_num = stn.nextToken();
                        continue;
                    }else{
		        System.out.println("PUT:i="+i+":j="+j);
                        return;
                    }
	        }catch(IOException e){
	            System.exit(0);
	        }
           }
        }
    }

    //----------------------------------
    // Static PUT method 2
    //----------------------------------
    private void ai_statistics_2(){
        BoardPoint boardpoint = new BoardPoint();
        //boardpoint.print();
        if(x<=7){
  	    //System.out.println("x="+x+":y="+y+" :point "+point+":board="+boardpoint.board[x][y]);
            if(boardpoint.board[x][y]==point){
  	        //System.out.println("PUT:x="+x+":j="+y+" :point "+point);
                pw.println("PUT "+x+" "+y);
                pw.flush();
            }
            x++;
        }else if(y>=7){
            x = 0;
            y = 0;
            point --;
            //System.out.println("x y reset");
        }else{
            //System.out.println("incriment y");
            x = 0;
            y++;

        }

        //System.out.println("PUT dummy");
    	pw.println("PUT 9 9");
    	pw.flush();

    }

    private void ai_statistics_2_reset(){
        x = 0;
        y = 0;
        point = 9;
    }

    public static void main(String args[]) {
	new OthelloClient(args[0],Integer.parseInt(args[1]),args[2]);
    }
}


class OthelloCanvas extends JPanel {
    private final static int startx = 20;
    private final static int starty = 10;
    private final static int gap = 30;
    private OthelloClient oc;
    private byte[][] board;

    OthelloCanvas(OthelloClient oc){
	this.oc = oc;
	this.board=oc.getBoard();
	this.addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
		    Point p = e.getPoint();
		    putPiece((p.x-startx)/gap,(p.y-starty)/gap);
		}
	    });
    }

    private void putPiece(int x, int y){
	oc.putPiece(x,y);
    }


    public void paintComponent(Graphics g){
	g.setColor(new Color(0,180,0));
	g.fillRect(startx,starty,gap*8,gap*8);

	g.setColor(Color.BLACK);
	for(int i=0;i<9;i++){
	    g.drawLine(startx,starty+i*gap,startx+8*gap,starty+i*gap);
	    g.drawLine(startx+i*gap,starty,startx+i*gap,starty+8*gap);
	}
	for(int i=0;i<8;i++){
	    for(int j=0;j<8;j++){
		if(board[i][j]==OthelloClient.BLACK){
		    g.setColor(Color.BLACK);
		    g.fillOval(startx+gap*i,starty+gap*j,gap,gap);
		}else if(board[i][j]==OthelloClient.WHITE){
		    g.setColor(Color.WHITE);
		    g.fillOval(startx+gap*i,starty+gap*j,gap,gap);
		}
	    }
	}
    }
}
