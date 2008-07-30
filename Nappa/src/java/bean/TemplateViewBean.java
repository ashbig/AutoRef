/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import transfer.SlidetemplateTO;

/**
 *
 * @author dzuo
 */
public class TemplateViewBean extends LayoutViewBean {
    private SlidetemplateTO template;

    public SlidetemplateTO getTemplate() {
        return template;
    }

    public void setTemplate(SlidetemplateTO template) {
        this.template = template;
        setLayout(template.getLayout());
    }
}
