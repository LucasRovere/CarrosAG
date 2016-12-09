
package genetic2;

public class Map {
    private int source[][];
    private static float originX = (float) 0.5, originY = (float) 0.5;
    private static float size;
    public static final int msize = 10;
    
    public static float getOrigin(int axes){
	if(axes == 0)
	    return originX*size;
	else
	    return originY*size;
    }
    
    public Map(int source[][], int size){
	this.source = source;
	this.size = size;
    }
    
    public boolean checkCollision(float x, float y){
	int X = (int) (msize*x / size);
	int Y = (int) (msize*y / size);
	
	if(X > msize-1) X = msize-1;
	if(Y > msize-1) Y = msize-1;
	
	if(source[X][Y] == 0)
	    return true;
	else
	    return false;
    }
}
