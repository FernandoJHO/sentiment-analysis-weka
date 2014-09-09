package cl.usach.diinf.tallerbd.sa.clean;

import java.util.List;
import java.util.Locale;
/**
 * Interfaz que representa el comportamiento de un tokenizador
 * @author rvasquez
 *
 */
public interface Tokenizer{
	/**
	 * Obtiene los tokens desde un string
	 * @param text texto a tokenizar
	 * @return la versión tokenizada del texto
	 */
	List<String> getStrings(String text);

	/**
	 * Obtiene el lenguaje del tokenizador utilizado
	 * @return
	 */
	Locale getLocale();
}
