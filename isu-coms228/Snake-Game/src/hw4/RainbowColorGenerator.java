package hw4;

import java.awt.Color;
import java.util.Random;

import main.Config;
/**
 * Makes 6 specified colors and returns them with equal chance
 * @author James Taylor
 *
 */
public class RainbowColorGenerator implements color.ColorGenerator {
	/**
	 * The color Red
	 */
	private static final Color R = new Color(0.25f, 0.0f, 0.0f);
	/**
	 * The color Orange
	 */
	private static final Color O = new Color(0.25f, 0.125f, 0.0f);
	/**
	 * The color Yellow
	 */
	private static final Color Y = new Color(0.25f, 0.25f, 0.0f);
	/**
	 * The color Green
	 */
	private static final Color G = new Color(0.0f, 0.25f, 0.0f);
	/**
	 * The color Blue
	 */
	private static final Color B = new Color(0.0f, 0.0f, 0.25f);
	/**
	 * The color Purple
	 */
	private static final Color P = new Color(0.25f, 0.0f, 0.25f);

	private Random r = Config.RANDOM;

	
	@Override
	public Color createColor() {
		int next = r.nextInt(6);
		switch (next) {
			case 0 :
				return R;
			case 1 :
				return O;
			case 2 :
				return Y;
			case 3 :
				return G;
			case 4 :
				return B;
			default :
				return P;

		}
	}
}
