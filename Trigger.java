
package genetic2;

/* Actions:
    0 = acelera
    1 = freia
    2 = direita aberta
    3 = esquerda aberta
    4 = direita fechada
    5 = esquerda fechada
*/

public class Trigger {
    private int action;
    private double c1, c2, c3, c4;
    
    //aleatorio para acao X
    public Trigger(int action){
	if(action > -1) this.action = action;
	else this.action = (int) (Math.random() * 6);
	
	c1 = Math.random();
	c2 = Math.random();
	c3 = Math.random();
	c4 = Math.random();
    }
    
    public Trigger(int action, double c1, double c2, double c3, double c4){
	this.action = action;
	this.c1 = c1;
	this.c2 = c2;
	this.c3 = c3;
	this.c4 = c4;
    }
    
    public int Test(double r1, double r2, double r3, double speed){
	if(c1*r1 > 1 && c2*r2 > 1 && c3*r3 > 1 && c4*speed < 1)
	    return action;
	
	return -1;
    }

    void mutate(){
	c1 += Math.random()*c1 - c1/2;
	c2 += Math.random()*c2 - c2/2;
	c3 += Math.random()*c3 - c3/2;
	c4 += Math.random()*c4 - c4/2;
    }
}
