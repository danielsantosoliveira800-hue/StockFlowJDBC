package util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatadorUtil {

    private static final NumberFormat FORMATADOR_MOEDA =
            NumberFormat.getCurrencyInstance(new Locale("pt","BR"));

    private FormatadorUtil(){
    }

    public static String formatadorMoeda(double valor){
        return FORMATADOR_MOEDA.format(valor);
    }
}
