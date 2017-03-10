package modelo;
import java.util.Collections;

public class MazoDelJuego extends Mazo {

	public MazoDelJuego() {
		for (int i=1;i<=4;i++) 
			for (int j=1;j<=12;j++)
				cartas.add(new Carta(i,j));
		for (int i=1;i<3;i++)
			cartas.add(new Carta(5,i));
	}
	
	public void mezclar() {
		Collections.shuffle(cartas);
	}

}
