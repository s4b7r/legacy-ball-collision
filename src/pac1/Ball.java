/**
 * 
 */
package pac1;

/**
 * @author 
 *
 */
public class Ball {
	// Positionsvektor
	private Vector pos = new Vector(0, 0);
	// Radius
	private float r = 0;
	// Geschwindigkeitsvektor
	private Vector v = new Vector(0, 0);
	// Beschleunigungsvektor
	private Vector a = new Vector(0, 0);
	// Masse
	private float m = 0;
	
	public Ball(Vector pos, float r, float m){
		this.pos.set(pos.getX(), pos.getY());
		this.r = r;
		this.v.set(0, 0);
		this.a.set(0, 0);
		this.m = m;
	}
	
	public Ball(Vector pos, Vector v, float r,  float m){
		this.pos.set(pos.getX(), pos.getY());
		this.r = r;
		this.v.set(v.getX(), v.getY());
		this.a.set(0, 0);
		this.m = m;
	}
	
	public Ball(Vector pos, Vector v, Vector a, float r,  float m){
		this.pos.set(pos.getX(), pos.getY());
		this.r = r;
		this.v.set(v.getX(), v.getY());
		this.a.set(a.getX(), a.getY());
		this.m = m;
	}
	
	public Ball(float posX, float posY, float r,  float m){
		this.pos.set(posX, posY);
		this.r = r;
		this.v.set(0, 0);
		this.a.set(0, 0);
		this.m = m;
	}
	
	public Ball(float posX, float posY, float vX, float vY, float r,  float m){
		this.pos.set(posX, posY);
		this.r = r;
		this.v.set(vX, vY);
		this.a.set(0, 0);
		this.m = m;
	}
	
	public Ball(float posX, float posY, float vX, float vY, float aX, float aY, float r,  float m){
		this.pos.set(posX, posY);
		this.r = r;
		this.v.set(vX, vY);
		this.a.set(aX, aY);
		this.m = m;
	}
	
	public void setPosition(Vector pos){
		this.pos.set(pos.getX(), pos.getY());
	}
	
	public void setPosition(float posX, float posY){
		this.pos.set(posX, posY);
	}
	
	public void setVelocity(Vector v){
		this.v.set(v.getX(), v.getY());
	}
	
	public void setVelocity(float vX, float vY){
		this.v.set(vX, vY);
	}
	
	public void setAcceleration(Vector a){
		this.a.set(a.getX(), a.getY());
	}
	
	public void setAcceleration(float aX, float aY){
		this.a.set(aX, aY);
	}
	
	public void setMass(float m){
		this.m = m;
	}
	
	public void setRadius(float r){
		this.r = r;
	}
	
	public Vector getPosition(){
		return pos;
	}
	
	public float getPositionX(){
		return pos.getX();
	}
	
	public float getPositionY(){
		return pos.getY();
	}
	
	public Vector getVelocity(){
		return v;
	}
	
	public float getVelocityX(){
		return v.getX();
	}
	
	public float getVelocityY(){
		return v.getY();
	}
	
	public Vector getAcceleration(){
		return a;
	}
	
	public float getAccelerationX(){
		return a.getX();
	}
	
	public float getAccelerationY(){
		return a.getY();
	}
	
	public float getMass(){
		return m;
	}
	
	public float getRadius(){
		return r;
	}
}
