
package genetic2;

import java.util.ArrayList;

public class Car {
    private float pX, pY;
    private float maxX = 0, maxY = 0, minX = 1000, minY = 1000;
    private float speed = 0, realSpeed = 1;
    private float speedAngle = 0, realAngle = 0;
    private Gene gene;
    private float fitness = -1, area;
    private int iterations = 0;
    private boolean collided = false;

    public Car(Gene gene){
	pX = Map.getOrigin(0);
	pY = Map.getOrigin(1);
	this.gene = gene;
    }
    
    public void Reset(){
        pX = Map.getOrigin(0);
	pY = Map.getOrigin(1);
        speed = 0;
        realSpeed = 1;
        speedAngle = 0;
        realAngle = 0;
        fitness = 0;
    }
    
    public void takeAction(Map map){
        updateFitness();
	if(collided == true) return;
	
	float r0 = read(map, 0);
	float r1 = read(map, 1);
	float r2 = read(map, 2);
	
	for(Trigger t : gene.triggers){
	    int res = t.Test(r0, r1, r2, realSpeed);
	    
	    if(res == 0)
		actAccelerate();
	    else if(res == 1)
		actBreak();
	    else if(res == 2)
		actSlowRight();
	    else if(res == 3)
		actSlowLeft();
	    else if(res == 4)
		actHardRight();
	    else if(res == 5)
		actHardLeft();
	}
	
	realAngle = (speedAngle+realAngle)/2;
	realSpeed = (speed + realSpeed)/2;
	if(speed > 1) speed *= 0.9;
	
	Move();
    }
    
    private void Move(){
	pX += realSpeed*Math.cos(realAngle);
	pY += realSpeed*Math.sin(realAngle);
    }
    
    private float read(Map map, int sensor){
	float distance = 0;
	float angle = realAngle;
	float incX, incY;
	
	if(sensor == 0)
	    angle -= 0.3;
	else
	    angle += 0.3;
	
	incY = (float) Math.sin(angle);
	incX = (float) Math.cos(angle);
	
	while(!map.checkCollision(pX + incX*distance, incY*distance + pY)){
	    distance += 1;
	}
	
	return distance;
    }
    
    private void actAccelerate(){
	speed += 0.5;
    }
    private void actBreak(){
	if(speed == 0) return;
	speed -= 0.5;
    }
    private void actSlowRight(){
	speedAngle -= 0.3;
    }
    private void actSlowLeft(){
	speedAngle += 0.3;
    }
    private void actHardRight(){
	speedAngle -= 0.8;
    }
    private void actHardLeft(){
	speedAngle += 0.8;
    }
    
    public float getpX() {
	return pX;
    }

    public float getpY() {
	return pY;
    }
    
    private void updateFitness(){
	iterations++;
	
	if(iterations == 1){
	    fitness = realSpeed;
	}
	else{
	    fitness = fitness*(iterations-1) + realSpeed;
	    fitness /= iterations;
	}
	
	if(pX > maxX) maxX = pX;
	if(pY > maxY) maxY = pY;
	if(pX < minX) minX = pX;
	if(pY < minY) minY = pY;
	
	float coef = 0.0005f;
	area = (maxX-minX)*(maxY-minY)*coef;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
    
    public float getFitness(){
	return fitness + area; 
    }
    
    public Gene getGene(){
	return gene;
    }
    
    public void Collide(){
	collided = true;
	speed = 0;
	realSpeed = 0;
    }
}
