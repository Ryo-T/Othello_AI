public class BoardPoint{

    byte[][] board ={
	    {9,2,8,7,7,8,2,9},
	    {2,1,3,4,4,3,1,2},
	    {8,3,6,5,5,6,3,8},
	    {7,4,5,0,0,5,4,7},
	    {7,4,5,0,0,5,4,7},
	    {8,3,6,5,5,6,3,8},
	    {2,1,3,4,4,3,1,2},
	    {9,2,8,7,7,8,2,9}
    };

    public void print(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
              System.out.print(board[i][j]+" ");
            }
            System.out.println("");
        }

    }
}
