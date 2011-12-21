package yetanotherx.redditbot.util.transformer;

import org.apache.commons.collections.Transformer;

/**
 * Apache Commons collection transformer
 * Transforms objects to doubles.
 * 
 * @author yetanotherx
 */
public class DoubleTransformer implements Transformer {

    public Object transform(Object o) {
        if( o == null ) {
            return null;
        }
        
        return Double.parseDouble(o.toString());
    }
    
}
