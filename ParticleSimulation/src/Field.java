import java.util.LinkedList;

public class Field{
	Vector dimensions;
	int numParticles;
	LinkedList<Particle> particles;
	Visual window;

	public Field(Visual window, Vector dimensions, int numParticles){
		this.window = window;
		this.dimensions = dimensions;
		this.numParticles = numParticles;
		particles = new LinkedList<Particle>();
//		for(int i = 0; i<numParticles; i++){
//			particles.add(new Particle(window));
//		}
		
		particles.add(new Particle(window, -1, new Vector(50, 50, 0), 10, new Vector(-1, -1, -0)));
		particles.add(new Particle(window, 1, new Vector(-50, -50, 0), 10, new Vector(2, 2, 0)));
		
//		particles.add(new Particle(window, 0, new Vector(400, 0, 0), 20, new Vector(-2, 0, 0)));
//		particles.add(new Particle(window, 0, new Vector(-50, 0, 0), 10, new Vector(2, 0, 0)));
	}

	public void populateForces(){
		for(Particle p: this.particles){
			p.forceOn = new Vector(0, 0, 0);
			for(Particle o: this.particles){
				Vector forceBetween = p.forceBetween(o);
				p.forceOn.x += forceBetween.x;
				p.forceOn.y += forceBetween.y;
				p.forceOn.z += forceBetween.z;
			}
			p.addDrag();
//			System.out.println("force " + p.color.x + " " + p.forceOn.x + "/" + p.forceOn.y + "/" + p.forceOn.z);
		}
	}
	
	public void drawAllParticles(){
		for(Particle p: this.particles){
			p.drawParticle();
		}
	}

	public void accelerateAllParticles(){
		for(Particle p: this.particles){
			p.accelerate();
		}
	}

	public void handleWallCollisions(){
		for(Particle p: this.particles){
			p.checkWallCollisions();
		}
	}
	
	public void moveAllParticles(){
		for(Particle p: this.particles){
			p.move();
		}
	}
	
	//make sure each pair only gets visited once
	public void handleInterCollisions(){
		int i = 0;
		for(Particle p: this.particles){
			for(int j = particles.size()-1; j > i; j--){
//				System.out.println("i " + i + " j " + j);
//				p.collideWith(particles.get(j));
				p.collisionForces(particles.get(j));
			}
			i++;
		}
	}
}