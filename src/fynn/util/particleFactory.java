package fynn.util;

import fynn.model.*;
import org.joml.Random;
import org.joml.*;

import java.util.*;

import static fynn.MagicNumbers.*;
import static java.lang.StrictMath.*;


public class particleFactory {
	Random r;

	public particleFactory() {
		r = new Random(System.currentTimeMillis());

	}

	public Instance createInstance(int num, float pScale, float vScale) {
		 return new Instance(createParticles(num, pScale, vScale));
	}


	private ArrayList<Particle> createParticlesSpiral(int num, float pScale, float vScale) {
		ArrayList<Particle> pList = new ArrayList<>();

		while (pList.size() < num) {
			float angle = r.nextFloat() * 2.0f * 3.1415927f;
			float dist = r.nextFloat() * pScale * 5;
			dist += 10;

			Vector3f pos = new Vector3f(dist * (float) sin(angle), r.nextFloat() * pScale, dist * (float) cos(angle));
			angle += 3.1415927f / 2;

			Vector3f vel = new Vector3f((float) sqrt(dist) * (float) sin(angle) * vScale, (r.nextFloat() - 0.5f) * vScale, (float) sqrt(dist) * (float) cos(angle) * vScale);

			pList.add(new Particle(pos, vel.mul(0.1f)));
		}

		return pList;
	}

	private ArrayList<Particle> createParticlesSpiral(int num, float pScale, float vScale, float xOffset) {
		ArrayList<Particle> pList = new ArrayList<>();

		while (pList.size() < num) {
			float angle = r.nextFloat() * 2.0f * 3.14159265358979324f;
			float dist = r.nextFloat() * pScale * 5;
			dist += 10;

			Vector3f pos = new Vector3f(dist * (float) sin(angle) + xOffset * pScale, r.nextFloat() * pScale, dist * (float) cos(angle));
			angle += 3.1415927f / 2;

			Vector3f vel = new Vector3f((float) sqrt(dist) * (float) sin(angle), (r.nextFloat() - 0.5f) * vScale, (float) sqrt(dist) * (float) cos(angle) + (float) sqrt((pos.x - 0.5f) * (pos.x - 0.5f)) / 100 * vScale);

			pList.add(new Particle(pos, vel.mul(0.1f)));
		}

		return pList;
	}


	private ArrayList<Particle> createDoubleSpiral(int num, float pScale, float vScale) {
		ArrayList<Particle> pList = new ArrayList<>();
		pList.addAll(createParticlesSpiral(num / 2, pScale, vScale, 6f));
		pList.addAll(createParticlesSpiral(num / 2, pScale, vScale, -6f));
		return pList;
	}

	public Vector3f randomVector3() {
		return new Vector3f(r.nextFloat() - 0.5f, r.nextFloat() - 0.5f, r.nextFloat() - 0.5f).mul(2);
	}

	public ArrayList<Particle> createParticles(int num, float pScale, float vScale) {
		ArrayList<Particle> pList = new ArrayList<>();
		while (pList.size() < num) {
			pList.add(new Particle(randomVector3().mul(pScale * 5f), randomVector3().mul(vScale)));
		}
		return pList;
	}

	public Instance get(int instanceType, int num) {
		if (instanceType == 1) {
			return new Instance(createParticlesSpiral(num, pScale, vScale));
		} else if (instanceType == 2) {
			return new Instance(createDoubleSpiral(num, pScale, vScale));
		} else if (instanceType == 3) {
			return new Instance(createParticles(num, pScale, vScale));
		} else {
			return new Instance(createParticles(num, pScale, vScale));
		}
	}
}
