
package genetic2;

import java.util.ArrayList;

public class Gene {
    public ArrayList<Trigger> triggers = new ArrayList<>();
    private float fitness = -1;
    public double serial = Math.random();
    
    // gera aleatoriamente
    public Gene(){
	triggers.add(new Trigger(0));
	triggers.add(new Trigger(0));
	triggers.add(new Trigger(1));
	triggers.add(new Trigger(1));
	triggers.add(new Trigger(2));
	triggers.add(new Trigger(2));
	triggers.add(new Trigger(3));
	triggers.add(new Trigger(3));
	triggers.add(new Trigger(4));
	triggers.add(new Trigger(4));
	triggers.add(new Trigger(5));
	triggers.add(new Trigger(5));
    }
    
    Gene(Gene g){
	triggers.add(g.triggers.get(0));
	triggers.add(g.triggers.get(1));
	triggers.add(g.triggers.get(2));
	triggers.add(g.triggers.get(3));
	triggers.add(g.triggers.get(4));
	triggers.add(g.triggers.get(5));
	triggers.add(g.triggers.get(6));
	triggers.add(g.triggers.get(7));
	triggers.add(g.triggers.get(8));
	triggers.add(g.triggers.get(9));
    }
    
    // combina
    public void Combine(Gene g1, Gene g2){
	int size, i;
	triggers.clear();
	
	if(g1.triggers.size() > g2.triggers.size())
	    size = g1.triggers.size();
	else
	    size = g2.triggers.size();
	
	triggers.ensureCapacity(size);
	
	for(i=0; i<size; i++){
	    if(Math.random() > 0.3 && g2.triggers.size() > i){
		triggers.add(g2.triggers.get(i));
	    }
	    else if(g1.triggers.size() > i){
		triggers.add(g1.triggers.get(i));
	    }
	}
    }
    
    public void Mutate(){
	int i, mutations = (int)(Math.random()*triggers.size());
	
	for(i=0; i<mutations; i++){
	    int choice = (int) (Math.random()*triggers.size());
	    triggers.get(choice).mutate();
	}
	
	int deletions = (int)(Math.random()*triggers.size()/3);
	int insertions = (int)(Math.random()*triggers.size()/3);
	
	for(i=0; i<deletions; i++){
	    int select = (int) (triggers.size()*Math.random());
	    triggers.remove(select);
	}
	
	for(i=0; i<insertions; i++){
	    triggers.add(new Trigger(-1));
	}
    }
    
    public void setFitness(float fitness){
	this.fitness = fitness;
    }
    
    public float getFitness(){
	return fitness;
    }
}
