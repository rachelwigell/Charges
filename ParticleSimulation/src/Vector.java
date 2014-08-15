public class Vector {
	float x;
	float y;
	float z;

	public Vector(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector difference(Vector aVec){
		float x = this.x - aVec.x;
		float y = this.y - aVec.y;
		float z = this.z - aVec.z;
		return new Vector(x, y, z);
	}
	
	public Vector sum(Vector aVec){
		return new Vector(this.x + aVec.x, this.y + aVec.y, this.z + aVec.z);
	}

	public float distanceMagnitude(Vector aVec){
		float x = this.x - aVec.x;
		float y = this.y - aVec.y;
		float z = this.z - aVec.z;
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector dotProduct(float factor){
		return new Vector(this.x * factor, this.y * factor, this.z * factor);
	}

}