package StockMarket;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eaverdeja on 19/06/17.
 */
public class StockServerImpl extends StockServerPOA {
    private final Object mutex = new Object();
    private Map<String, Float> myStock;

    public StockServerImpl() {
        myStock = new HashMap<String, Float>();
    }

    public StockServerImpl(File file) {
        //TODO ler arquivo e criar hashMap com as ações
        myStock = new HashMap<String, Float>();
    }

    @Override
    public float getStockValue(String symbol) throws UnknownSymbol {
        synchronized (mutex) {
            if(myStock.containsKey(symbol)) {
                return myStock.get(symbol);
            } else {
                return 0f;
            }
        }
    }

    @Override
    public String[] getStockSymbols() {
        synchronized (mutex) {
            return myStock.keySet().toArray(new String[0]);
        }
    }
}
