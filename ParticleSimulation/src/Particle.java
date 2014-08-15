import java.util.Random;

public class Particle {
	Visual window;
	int charge;
	Vector center;
	float mass;
	float crossSectionalArea;
	int radius;
	Vector velocity;
	Vector color;
	Vector forceOn;

	final float pi = (float) 3.14;
	final float interCollisionDampening = (float) .9;
	final float wallCollisionDampening = (float) .9;
	final float dragCoefficient = (float) .001;
	final int forceCoefficient = 50000;
	final float timeContact = 100;

	public Particle(Visual window){
		Random rand = new Random();
		this.charge = rand.nextInt(3) - 1; //-1, 0, or 1;
		//		this.charge = 0;
		switch(this.charge){
		case -1:
			this.color = new Vector(0, 0, 255); //blue
			break;
		case 0:
			this.color = new Vector(200, 200, 200); //gray
			break;
		case 1:
			this.color = new Vector(255, 0, 0); //red
			break;
		}
		this.radius = rand.nextInt(5) + 5;	 //5-10
		this.mass = this.pi * this.radius * this.radius; //assuming constant density, proportional to volume
		this.crossSectionalArea = 2 * this.pi * this.radius;
		this.center = new Vector(rand.nextInt(window.fieldX)-window.fieldX/2, rand.nextInt(window.fieldY)-window.fieldY/2, rand.nextInt(window.fieldZ)-window.fieldZ/2);
		this.velocity = new Vector(rand.nextInt(4)-2, rand.nextInt(4)-2, rand.nextInt(4)-2);
		this.forceOn = new Vector(0, 0, 0); //can't initialize until field is populated with all particles
		this.window = window;
	}

	//dev tool
	public Particle(Visual window, int charge, Vector center, int radius, Vector velocity){
		this.window = window;
		this.charge = charge;
		this.center = center;
		this.radius = radius;
		this.velocity = velocity;
		switch(this.charge){
		case -1:
			this.color = new Vector(0, 0, 255); //blue
			break;
		case 0:
			this.color = new Vector(200, 200, 200); //gray
			break;
		case 1:
			this.color = new Vector(255, 0, 0); //red
			break;
		}
		this.mass = this.pi * this.radius * this.radius;
		this.crossSectionalArea = 2 * this.pi * this.radius;
		this.forceOn = new Vector(0, 0, 0);
	}

	public Vector forceBetween(Particle aParticle){
		Vector distances = this.center.difference(aParticle.center);
		float distance = this.center.distanceMagnitude(aParticle.center);

		if(distance == 0) return new Vector(0, 0, 0);

		float x = forceCoefficient * this.charge * aParticle.charge * distances.x / (distance * distance * distance);
		float y = forceCoefficient * this.charge * aParticle.charge * distances.y / (distance * distance * distance);
		float z = forceCoefficient * this.charge * aParticle.charge * distances.z / (distance * distance * distance);

		return new Vector(x, y, z);
	}

	public boolean isCollidingWith(Particle aParticle){
		float distance = this.center.distanceMagnitude(aParticle.center);
		if(distance == 0) return false;
		return distance <= (this.radius + aParticle.radius);
	}
	
	public Vector collisionVelocity(Particle aParticle){
		return this.velocity.dotProduct((this.mass - aParticle.mass) / (this.mass + aParticle.mass)).sum(
				aParticle.velocity.dotProduct(2*aParticle.mass / (this.mass + aParticle.mass)));
	}

	public void collideWith(Particle aParticle){
		if(this.isCollidingWith(aParticle)){
			Vector thisVel = this.collisionVelocity(aParticle);
			Vector aVel = aParticle.collisionVelocity(this);
			this.velocity = thisVel;
			aParticle.velocity = aVel;
		}
	}

	//model the force that occurs during the moment of collision instead of the change to velocity. but need to fix the physics here.
	public void collisionForces(Particle aParticle){
		
		if(this.isCollidingWith(aParticle)){
			this.forceOn = this.forceOn.sum(this.velocity.dotProduct(interCollisionDampening*-2*this.mass*aParticle.mass / (this.mass + aParticle.mass)).sum(
					aParticle.velocity.dotProduct(interCollisionDampening*2*this.mass*aParticle.mass / (this.mass + aParticle.mass))));
			aParticle.forceOn = aParticle.forceOn.sum(aParticle.velocity.dotProduct(interCollisionDampening*-2*this.mass*aParticle.mass / (this.mass + aParticle.mass)).sum(
					this.velocity.dotProduct(interCollisionDampening*2*this.mass*aParticle.mass / (this.mass + aParticle.mass))));
		}
//		System.out.println(this.radius + " " + this.forceOn.x);
//		System.out.println(aParticle.radius + " " + aParticle.forceOn.x);
	}

	public void drawParticle(){
		window.pushMatrix();
		window.translate((float) this.center.x, (float) this.center.y, (float) this.center.z);
		window.fill((float) this.color.x, (float) this.color.y, (float) this.color.z);
		window.sphere(this.radius);
		window.popMatrix();
	}

	public void addDrag(){
		if(this.velocity.x < 0) this.forceOn.x += dragCoefficient * this.velocity.x * this.velocity.x * this.crossSectionalArea;
		if(this.velocity.x > 0) this.forceOn.x -= dragCoefficient * this.velocity.x * this.velocity.x * this.crossSectionalArea;
		if(this.velocity.y < 0) this.forceOn.y += dragCoefficient * this.velocity.y * this.velocity.y * this.crossSectionalArea;
		if(this.velocity.y > 0) this.forceOn.y -= dragCoefficient * this.velocity.y * this.velocity.y * this.crossSectionalArea;
		if(this.velocity.z < 0) this.forceOn.z += dragCoefficient * this.velocity.z * this.velocity.z * this.crossSectionalArea;
		if(this.velocity.z > 0) this.forceOn.z -= dragCoefficient * this.velocity.z * this.velocity.z * this.crossSectionalArea;
	}

	public void accelerate(){
		this.velocity.x += this.forceOn.x/this.mass;
		this.velocity.y += this.forceOn.y/this.mass;
		this.velocity.z += this.forceOn.z/this.mass;
	}

	public void move(){
		this.center.x += this.velocity.x;
		this.center.y += this.velocity.y;
		this.center.z += this.velocity.z;
	}

	public void checkWallCollisions(){
		if((this.center.x-this.radius < -window.fieldX/2 && this.velocity.x < 0) ||
				(this.center.x+this.radius > window.fieldX/2 && this.velocity.x > 0)){
			this.velocity.x *= -wallCollisionDampening;
		}
		if((this.center.y-this.radius < -window.fieldY/2 && this.velocity.y < 0) ||
				(this.center.y+this.radius > window.fieldY/2 && this.velocity.y > 0)){
			this.velocity.y *= -wallCollisionDampening;
		}
		if((this.center.z-this.radius < -window.fieldZ/2 && this.velocity.z < 0) ||
				(this.center.z+this.radius > window.fieldZ/2 && this.velocity.z > 0)){
			this.velocity.z *= -wallCollisionDampening;
		}
	}
}