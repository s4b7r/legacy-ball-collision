/**
 * 
 */
package pac1;

/**
 * @author 
 *
 */
public class Vector {
	private float x = 0;
	private float y = 0;
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public Vector multiply(float scalar){
		return new Vector(x * scalar, y * scalar);
	}
	
	public float dotProduct(Vector vector){
		return x * vector.getX() + y * vector.getY();
	}
	
	public Vector add(Vector vector){
		return new Vector(x + vector.getX(), y + vector.getY()); 
	}
	
	public float getAbsolute(){
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}
