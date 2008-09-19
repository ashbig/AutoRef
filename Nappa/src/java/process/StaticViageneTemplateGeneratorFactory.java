/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

/**
 *
 * @author DZuo
 */
public class StaticViageneTemplateGeneratorFactory {
    public static ViageneTemplateGenerator makeTemplateGenerator(String s) {
        if(ViageneTemplateGenerator.BLOCKWISE.equals(s))
            return new BlockwiseTemplateGenerator();
        if(ViageneTemplateGenerator.ONEGRID.equals(s))
            return new OnegridTemplateGenerator();
        if(ViageneTemplateGenerator.MULTIPLEROI.equals(s))
            return new MultipleROITemplateGenerator();
        return null;
    }
}
